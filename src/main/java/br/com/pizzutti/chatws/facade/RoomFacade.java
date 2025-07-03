package br.com.pizzutti.chatws.facade;

import br.com.pizzutti.chatws.dto.RoomAggregateDto;
import br.com.pizzutti.chatws.dto.RoomInsertDto;
import br.com.pizzutti.chatws.dto.UserDto;
import br.com.pizzutti.chatws.service.MemberService;
import br.com.pizzutti.chatws.service.RoomService;
import br.com.pizzutti.chatws.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomFacade {

    private final RoomService roomService;
    private final MemberService memberService;
    private final UserService userService;

    public RoomFacade(RoomService roomService,
                      MemberService memberService,
                      UserService userService) {
        this.roomService = roomService;
        this.memberService = memberService;
        this.userService = userService;
    }

    @Transactional
    public RoomAggregateDto create(RoomInsertDto roomInsertDto, Long owner) {
        var room = this.roomService.create(roomInsertDto, owner);
        var member = this.memberService.create(room.getId(), owner);
        var user = this.userService.findById(member.getUser());
        return RoomAggregateDto.builder()
                .id(room.getId())
                .name(room.getName())
                .owner(UserDto.fromUser(user))
                .members(List.of(UserDto.fromUser(user)))
                .createdAt(room.getCreatedAt())
                .build();
    }

    public RoomAggregateDto findById(Long id) {
        var room = this.roomService.findById(id);
        var owner = this.userService.findById(room.getOwner());
        var members = this.memberService.findByRoom(room.getId());
        var users = members.stream().map(m -> UserDto.fromUser(this.userService.findById(m.getUser()))).toList();
        return RoomAggregateDto.builder()
                .id(room.getId())
                .name(room.getName())
                .owner(UserDto.fromUser(owner))
                .members(users)
                .createdAt(room.getCreatedAt())
                .build();
    }

}
