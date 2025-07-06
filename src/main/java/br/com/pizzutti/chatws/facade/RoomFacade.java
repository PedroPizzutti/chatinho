package br.com.pizzutti.chatws.facade;

import br.com.pizzutti.chatws.dto.*;
import br.com.pizzutti.chatws.model.Message;
import br.com.pizzutti.chatws.model.Room;
import br.com.pizzutti.chatws.service.MemberService;
import br.com.pizzutti.chatws.service.MessageService;
import br.com.pizzutti.chatws.service.RoomService;
import br.com.pizzutti.chatws.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public RoomAggregateDto create(RoomInsertDto roomInsertDto, Long owner) {
        var room = this.roomService.create(roomInsertDto, owner);
        var member = this.memberService.create(room.getId(), owner);
        var user = this.userService.findById(member.getIdUser());
        return RoomAggregateDto.builder()
                .id(room.getId())
                .name(room.getName())
                .owner(UserDto.fromUser(user))
                .members(List.of(UserDto.fromUser(user)))
                .createdAt(room.getCreatedAt())
                .build();
    }

    public List<RoomDto> findAllByUser(Long idUser) {
        return this.roomService.findAllByUser(idUser)
                .stream()
                .map(RoomDto::fromRoom)
                .toList();
    }

    public RoomAggregateDto findById(Long idRoom) {
        var room = this.roomService.findById(idRoom);
        var owner = this.userService.findById(room.getIdOwner());
        var members = this.memberService.findByRoom(room.getId());
        var users = members.stream().map(m -> UserDto.fromUser(this.userService.findById(m.getIdUser()))).toList();
        return RoomAggregateDto.builder()
                .id(room.getId())
                .name(room.getName())
                .owner(UserDto.fromUser(owner))
                .members(users)
                .createdAt(room.getCreatedAt())
                .build();
    }

    public MessageAggregatePageDto findMessages(Long idRoom, Integer page, Integer perPage) {
        var room = this.roomService.findById(idRoom);
        var pageableMessages = this.messageService.findLatestByIdRoom(idRoom, page, perPage);
        return MessageAggregatePageDto
                .builder()
                .data(this.getListMessageAggregateDto(pageableMessages, room))
                .page(pageableMessages.getNumber() + 1)
                .perPage(pageableMessages.getSize())
                .totalPages(pageableMessages.getTotalPages())
                .records(pageableMessages.getNumberOfElements())
                .totalRecords(pageableMessages.getTotalElements())
                .build();
    }

    private List<MessageAggregateDto> getListMessageAggregateDto(Page<Message> pageableMessages, Room room) {
        return pageableMessages.getContent()
                .stream()
                .map(message -> {
                    return MessageAggregateDto.builder()
                            .room(RoomDto.fromRoom(room))
                            .user(UserDto.fromUser(this.userService.findById(message.getIdUser())))
                            .content(message.getContent())
                            .type(message.getType())
                            .createdAt(message.getCreatedAt())
                            .build();
                })
                .toList();
    }
}
