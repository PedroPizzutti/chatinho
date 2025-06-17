package br.com.pizzutti.chatws.controller;

import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.facade.MessageFacade;
import br.com.pizzutti.chatws.model.Message;
import br.com.pizzutti.chatws.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/message")
public class MessageController {

    private final MessageFacade messageFacade;

    public MessageController(MessageFacade messageFacade) {
        this.messageFacade = messageFacade;
    }

    @GetMapping("latest")
    public ResponseEntity<Page<MessageDto>> listLatest(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.messageFacade.findLatest(pageable));
    }

}
