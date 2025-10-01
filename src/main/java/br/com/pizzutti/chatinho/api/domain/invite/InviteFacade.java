package br.com.pizzutti.chatinho.api.domain.invite;

import br.com.pizzutti.chatinho.api.domain.room.RoomGetDto;
import br.com.pizzutti.chatinho.api.domain.user.UserGetDto;
import br.com.pizzutti.chatinho.api.infra.service.FilterOperationEnum;
import br.com.pizzutti.chatinho.api.domain.member.MemberService;
import br.com.pizzutti.chatinho.api.domain.room.RoomService;
import br.com.pizzutti.chatinho.api.domain.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class InviteFacade {

    private final InviteService inviteService;
    private final RoomService roomService;
    private final MemberService memberService;
    private final UserService userService;

    public InviteFacade(InviteService inviteService,
                        RoomService roomService,
                        MemberService memberService,
                        UserService userService) {
        this.inviteService = inviteService;
        this.userService = userService;
        this.memberService = memberService;
        this.roomService = roomService;
    }

    public <U> InviteFacade filter(String property, U value, FilterOperationEnum operation) {
        this.inviteService.filter(property, value, operation);
        return this;
    }

    public InviteGetAggregateDto create(InvitePostDto invitePostDto) {
        this.validationsRoom(invitePostDto);
        this.validationsMember(invitePostDto);
        this.validationsInvite(invitePostDto);
        var invite = this.inviteService.create(invitePostDto);
        return this.inviteAggregateDtoFromInvite(invite);
    }

    @Transactional
    public InviteGetAggregateDto accept(Long id) {
        var invite = this.inviteService.findById(id);
        if (!invite.getStatus().equals(InviteStatusEnum.PENDING))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Convite não está pendente!");
        this.memberService.create(invite.getIdRoom(), invite.getIdUserTo());
        invite.setStatus(InviteStatusEnum.ACCEPTED);
        return inviteAggregateDtoFromInvite(this.inviteService.update(invite));
    }

    public InviteGetAggregateDto reject(Long id) {
        var invite = this.inviteService.findById(id);
        if (!invite.getStatus().equals(InviteStatusEnum.PENDING))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Convite não está pendente!");
        invite.setStatus(InviteStatusEnum.REJECTED);
        return inviteAggregateDtoFromInvite(this.inviteService.update(invite));
    }

    private InviteGetAggregateDto inviteAggregateDtoFromInvite(Invite invite) {
        return InviteGetAggregateDto.builder()
            .id(invite.getId())
            .createdAt(invite.getCreatedAt())
            .status(invite.getStatus())
            .from(UserGetDto.fromUser(this.userService.findById(invite.getIdUserTo())))
            .to(UserGetDto.fromUser(this.userService.findById(invite.getIdUserFrom())))
            .room(RoomGetDto.fromRoom(this.roomService.findById(invite.getIdRoom())))
            .build();
    }

    private void validationsRoom(InvitePostDto invitePostDto) {
        var room = this.roomService.findById(invitePostDto.idRoom());

        if (!room.getIdOwner().equals(invitePostDto.idUserFrom()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente donos de sala podem criar convites!");

        if (room.getIdOwner().equals(invitePostDto.idUserTo()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível criar um convite para o dona da sala!");
    }

    private void validationsMember(InvitePostDto invitePostDto) {
        var listMember = this.memberService
                .filter("idRoom", invitePostDto.idRoom(), FilterOperationEnum.EQUAL)
                .filter("idUser", invitePostDto.idUserTo(), FilterOperationEnum.EQUAL)
                .find();

        if (!listMember.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível criar convite para um membro da sala!");
    }

    private void validationsInvite(InvitePostDto invitePostDto) {
        var listInvite = this.inviteService
                .filter("idUserTo", invitePostDto.idUserTo(), FilterOperationEnum.EQUAL)
                .filter("status", "PENDING", FilterOperationEnum.EQUAL)
                .find();

        if (!listInvite.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe convite para este usuário!");

    }

    public List<InviteGetAggregateDto> listInvite(Long idUserFrom, Long idUserTo, Long idRoom, InviteStatusEnum status) {
        return this.inviteService
                .filter("status", Objects.isNull(status) ? null : status.toString(), FilterOperationEnum.EQUAL)
                .filter("idUserFrom", idUserFrom, FilterOperationEnum.EQUAL)
                .filter("idUserTo", idUserTo, FilterOperationEnum.EQUAL)
                .filter("idRoom", idRoom, FilterOperationEnum.EQUAL)
                .find()
                .stream()
                .map(this::inviteAggregateDtoFromInvite)
                .toList();
    }
}
