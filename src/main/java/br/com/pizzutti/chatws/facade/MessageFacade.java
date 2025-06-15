package br.com.pizzutti.chatws.facade;

import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.model.Message;
import br.com.pizzutti.chatws.service.MessageService;
import br.com.pizzutti.chatws.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageFacade {

    private final MessageService messageService;
    private final UserService userService;

    public MessageFacade(MessageService messageService,
                         UserService userService) {
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

    public Page<MessageDto> findLatest(Pageable pageable) {
        return this.messageService.findAll(pageable)
                .map(message -> {
                    var user = this.userService.findById(message.getUserId());
                    return MessageDto.builder()
                            .createdAt(message.getCreatedAt())
                            .content(message.getContent())
                            .nick(user.getNickname())
                            .user(user.getLogin())
                            .build();

                    }
                );
    }

}
