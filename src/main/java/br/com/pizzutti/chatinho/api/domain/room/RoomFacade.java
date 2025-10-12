package br.com.pizzutti.chatinho.api.domain.room;

import br.com.pizzutti.chatinho.api.domain.message.MessageGetAggregateDto;
import br.com.pizzutti.chatinho.api.domain.message.MessageGetAggregatePageDto;
import br.com.pizzutti.chatinho.api.domain.user.UserGetDto;
import br.com.pizzutti.chatinho.api.infra.service.FilterDirectionEnum;
import br.com.pizzutti.chatinho.api.infra.service.FilterOperationEnum;
import br.com.pizzutti.chatinho.api.domain.member.Member;
import br.com.pizzutti.chatinho.api.domain.message.Message;
import br.com.pizzutti.chatinho.api.domain.member.MemberService;
import br.com.pizzutti.chatinho.api.domain.message.MessageService;
import br.com.pizzutti.chatinho.api.domain.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class RoomFacade {

    private final RoomService roomService;
    private final MemberService memberService;
    private final UserService userService;
    private final MessageService messageService;

    public RoomFacade(RoomService roomService,
                      MemberService memberService,
                      UserService userService,
                      MessageService messageService) {
        this.roomService = roomService;
        this.memberService = memberService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @Transactional
    public RoomGetAggregateDto create(RoomPostDto roomPostDto, Long owner) {
        var room = this.roomService.create(roomPostDto, owner);
        var member = this.memberService.create(room.getId(), owner);
        var user = this.userService.findById(member.getIdUser());
        return RoomGetAggregateDto.builder()
                .id(room.getId())
                .name(room.getName())
                .owner(UserGetDto.fromUser(user))
                .members(List.of(UserGetDto.fromUser(user)))
                .createdAt(room.getCreatedAt())
                .build();
    }

    public void delete(Long idRoom, Long idUser) {
        var room = this.roomService.findById(idRoom);
        if (!room.getIdOwner().equals(idUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente donos podem excluir sala!");
        }
        this.roomService.delete(room.getId());
    }

    @Transactional
    public void leave(Long idRoom, Long idUser) {
        var room = this.roomService.findById(idRoom);
        var member = this.memberService
                .filter("idRoom", room.getId(), FilterOperationEnum.EQUAL)
                .filter("idUser", idUser, FilterOperationEnum.EQUAL)
                .findOne();

        this.memberService.delete(member.getId());

        if (room.getIdOwner().equals(idUser)) {
            var members = this.memberService
                    .filter("idRoom", room.getId(), FilterOperationEnum.EQUAL)
                    .filter("idUser", idUser, FilterOperationEnum.DIFFERENT_OF)
                    .orderBy("createdAt", FilterDirectionEnum.ASC)
                    .find();

            if (members.isEmpty()) {
                this.roomService.delete(room.getId());
            } else {
                room.setIdOwner(members.getFirst().getIdUser());
                this.roomService.update(room);
            }
        }
    }

    public List<RoomGetDto> findAllByUser(Long idUser) {
        var members = this.memberService.filter("idUser", idUser, FilterOperationEnum.EQUAL).find();
        var idRooms = members.stream().map(Member::getIdRoom).toList();
        return this.roomService.filter("id", idRooms, FilterOperationEnum.IN)
                .find()
                .stream()
                .map(RoomGetDto::fromRoom)
                .toList();
    }

    public RoomGetAggregateDto findById(Long idRoom) {
        var room = this.roomService.findById(idRoom);
        var owner = this.userService.findById(room.getIdOwner());
        var members = this.memberService.filter("idRoom", room.getId(), FilterOperationEnum.EQUAL).find();
        var users = members.stream().map(m -> UserGetDto.fromUser(this.userService.findById(m.getIdUser()))).toList();
        return RoomGetAggregateDto.builder()
                .id(room.getId())
                .name(room.getName())
                .owner(UserGetDto.fromUser(owner))
                .members(users)
                .createdAt(room.getCreatedAt())
                .build();
    }

    public MessageGetAggregatePageDto findMessages(Long idRoom, Integer page, Integer perPage) {
        var room = this.roomService.findById(idRoom);
        var pageableMessages = this.messageService
                .filter("idRoom", idRoom, FilterOperationEnum.EQUAL)
                .orderBy("createdAt", FilterDirectionEnum.DESC)
                .find(page, perPage);
        return MessageGetAggregatePageDto
                .builder()
                .data(this.getListMessageAggregateDto(pageableMessages, room))
                .page(pageableMessages.getNumber() + 1)
                .perPage(pageableMessages.getSize())
                .totalPages(pageableMessages.getTotalPages())
                .records(pageableMessages.getNumberOfElements())
                .totalRecords(pageableMessages.getTotalElements())
                .build();
    }

    private List<MessageGetAggregateDto> getListMessageAggregateDto(Page<Message> pageableMessages, Room room) {
        return pageableMessages.getContent()
                .stream()
                .map(message -> MessageGetAggregateDto.builder()
                        .room(RoomGetDto.fromRoom(room))
                        .user(UserGetDto.fromUser(this.userService.findById(message.getIdUser())))
                        .content(message.getContent())
                        .type(message.getType())
                        .createdAt(message.getCreatedAt())
                        .build())
                .toList();
    }
}
