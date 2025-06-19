package br.com.pizzutti.chatws.facade;

import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.model.Message;
import br.com.pizzutti.chatws.model.User;
import br.com.pizzutti.chatws.service.MessageService;
import br.com.pizzutti.chatws.service.UserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageFacade {

    private Map<Long, User> listUser;
    private final MessageService messageService;
    private final UserService userService;

    public MessageFacade(MessageService messageService,
                         UserService userService) {
        this.listUser = new HashMap<>();
        this.messageService = messageService;
        this.userService = userService;
    }

    public Message create(MessageDto messageDto) {
        var user = this.userService.findByLogin(messageDto.user());
        var message = Message.builder()
                .createdAt(messageDto.createdAt())
                .userId(user.getId())
                .content(messageDto.content())
                .build();
        return this.messageService.create(message);
    }

    public Page<MessageDto> findLatest() {
        var pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        var page = this.messageService.findAll(pageable);
        var messages = page.getContent().stream().map(this::getMessageDto).toList();
        return new PageImpl<>(messages.reversed(), pageable, page.getTotalElements());
    }

    private MessageDto getMessageDto(Message message) {
        User user = null;
        if (listUser.containsKey(message.getUserId())) {
            user = listUser.get(message.getUserId());
        } else {
            user = this.userService.findById(message.getUserId());
            this.listUser.put(user.getId(), user);
        }
        return MessageDto.builder()
                .createdAt(message.getCreatedAt())
                .content(message.getContent())
                .nick(user.getNickname())
                .user(user.getLogin())
                .build();
    }

}
