package br.com.pizzutti.chatinho.api.domain.message;

import br.com.pizzutti.chatinho.api.infra.config.communication.PageDto;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class MessageAggregatePageDto extends PageDto<MessageAggregateDto> {}
