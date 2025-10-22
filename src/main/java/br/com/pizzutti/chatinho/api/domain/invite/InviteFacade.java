package br.com.pizzutti.chatinho.api.domain.invite;

import br.com.pizzutti.chatinho.api.domain.room.RoomGetDto;
import br.com.pizzutti.chatinho.api.domain.user.UserGetDto;
import br.com.pizzutti.chatinho.api.infra.service.FilterDirectionEnum;
import br.com.pizzutti.chatinho.api.infra.service.FilterOperationEnum;
import br.com.pizzutti.chatinho.api.domain.member.MemberService;
import br.com.pizzutti.chatinho.api.domain.room.RoomService;
import br.com.pizzutti.chatinho.api.domain.user.UserService;
import br.com.pizzutti.chatinho.api.infra.service.TimeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public InviteGetAggregateDto create(InvitePostDto invitePostDto, Long idUserFrom) {
        this.validationsRoom(invitePostDto, idUserFrom);
        this.validationsMember(invitePostDto);
        this.validationsInvite(invitePostDto);

        var invite = Invite.builder()
                .createdAt(TimeService.now())
                .idRoom(invitePostDto.idRoom())
                .idUserTo(invitePostDto.idUserTo())
                .idUserFrom(idUserFrom)
                .status(InviteStatusEnum.PENDING)
                .build();

        return this.inviteAggregateDtoFromInvite(this.inviteService.create(invite));
    }

    public List<InviteGetAggregateDto> get(InviteFilterDto filterDto) {
        return this.inviteService
                .filter("status", Optional.ofNullable(filterDto.status()).map(Object::toString).orElse(""), FilterOperationEnum.EQUAL)
                .filter("idUserFrom", filterDto.idUserFrom(), FilterOperationEnum.EQUAL)
                .filter("idUserTo", filterDto.idUserTo(), FilterOperationEnum.EQUAL)
                .orderBy("createdAt", FilterDirectionEnum.DESC)
                .find()
                .stream()
                .map(this::inviteAggregateDtoFromInvite)
                .toList();
    }

    @Transactional
    public void patchStatus(Long id, Long idUser, InviteStatusEnum status) {
        var invite = this.inviteService.findById(id);

        if (!invite.getIdUserTo().equals(idUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este convite não é seu!");
        }

        if (!invite.getStatus().equals(InviteStatusEnum.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Convite não está pendente!");
        }

        if (status.equals(InviteStatusEnum.ACCEPTED)) {
            this.memberService.create(invite.getIdRoom(), invite.getIdUserTo());
        }
        invite.setStatus(status);
        this.inviteService.update(invite);
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

    private void validationsRoom(InvitePostDto invitePostDto, Long idUserFrom) {
        var room = this.roomService.findById(invitePostDto.idRoom());

        if (!room.getIdOwner().equals(idUserFrom)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente donos de sala podem criar convites!");
        }

        if (room.getIdOwner().equals(invitePostDto.idUserTo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Não é possível criar um convite para o dono da sala!"
            );
        }
    }

    private void validationsMember(InvitePostDto invitePostDto) {
        var listMember = this.memberService
                .filter("idRoom", invitePostDto.idRoom(), FilterOperationEnum.EQUAL)
                .filter("idUser", invitePostDto.idUserTo(), FilterOperationEnum.EQUAL)
                .find();

        if (!listMember.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Não é possível criar convite para um membro da sala!");
        }
    }

    private void validationsInvite(InvitePostDto invitePostDto) {
        var listInvite = this.inviteService
                .filter("idRoom", invitePostDto.idRoom(), FilterOperationEnum.EQUAL)
                .filter("idUserTo", invitePostDto.idUserTo(), FilterOperationEnum.EQUAL)
                .filter("status", "PENDING", FilterOperationEnum.EQUAL)
                .find();

        if (!listInvite.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe convite para este usuário!");

    }
}
