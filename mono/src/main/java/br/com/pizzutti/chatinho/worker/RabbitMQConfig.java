package br.com.pizzutti.chatinho.worker;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public RabbitAdmin createAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue createQueueMessagesWs() {
        return QueueBuilder.durable("ws.messages").build();
    }

    @Bean
    public Queue createQueueMessagesErrors() {
        return QueueBuilder.durable("ws.errors").build();
    }


    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory,
                                               Jackson2JsonMessageConverter messageConverter) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setExchange("");
        rabbitTemplate.setRoutingKey("ws.messages");
        return rabbitTemplate;
    }
}
