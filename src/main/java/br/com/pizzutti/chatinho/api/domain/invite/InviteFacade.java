package br.com.pizzutti.chatinho.api.domain.invite;

import br.com.pizzutti.chatinho.api.domain.room.RoomDto;
import br.com.pizzutti.chatinho.api.domain.user.UserDto;
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

    public InviteAggregateDto create(InviteInputDto inviteInputDto) {
        this.validationsRoom(inviteInputDto);
        this.validationsMember(inviteInputDto);
        this.validationsInvite(inviteInputDto);
        var invite = this.inviteService.create(inviteInputDto);
        return this.inviteAggregateDtoFromInvite(invite);
    }

    @Transactional
    public InviteAggregateDto accept(Long id) {
        var invite = this.inviteService.findById(id);
        if (!invite.getStatus().equals(InviteStatusEnum.PENDING))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Convite não está pendente!");
        this.memberService.create(invite.getIdRoom(), invite.getIdUserTo());
        invite.setStatus(InviteStatusEnum.ACCEPTED);
        return inviteAggregateDtoFromInvite(this.inviteService.update(invite));
    }

    public InviteAggregateDto reject(Long id) {
        var invite = this.inviteService.findById(id);
        if (!invite.getStatus().equals(InviteStatusEnum.PENDING))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Convite não está pendente!");
        invite.setStatus(InviteStatusEnum.REJECTED);
        return inviteAggregateDtoFromInvite(this.inviteService.update(invite));
    }

    private InviteAggregateDto inviteAggregateDtoFromInvite(Invite invite) {
        return InviteAggregateDto.builder()
            .id(invite.getId())
            .createdAt(invite.getCreatedAt())
            .status(invite.getStatus())
            .from(UserDto.fromUser(this.userService.findById(invite.getIdUserTo())))
            .to(UserDto.fromUser(this.userService.findById(invite.getIdUserFrom())))
            .room(RoomDto.fromRoom(this.roomService.findById(invite.getIdRoom())))
            .build();
    }

    private void validationsRoom(InviteInputDto inviteInputDto) {
        var room = this.roomService.findById(inviteInputDto.idRoom());

        if (!room.getIdOwner().equals(inviteInputDto.idUserFrom()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente donos de sala podem criar convites!");

        if (room.getIdOwner().equals(inviteInputDto.idUserTo()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível criar um convite para o dona da sala!");
    }

    private void validationsMember(InviteInputDto inviteInputDto) {
        var listMember = this.memberService
                .filter("idRoom", inviteInputDto.idRoom(), FilterOperationEnum.EQUAL)
                .filter("idUser", inviteInputDto.idUserTo(), FilterOperationEnum.EQUAL)
                .find();

        if (!listMember.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível criar convite para um membro da sala!");
    }

    private void validationsInvite(InviteInputDto inviteInputDto) {
        var listInvite = this.inviteService
                .filter("idUserTo", inviteInputDto.idUserTo(), FilterOperationEnum.EQUAL)
                .filter("status", "PENDING", FilterOperationEnum.EQUAL)
                .find();

        if (!listInvite.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe convite para este usuário!");

    }

    public List<InviteAggregateDto> listInvite(Long idUserFrom, Long idUserTo, Long idRoom, InviteStatusEnum status) {
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
