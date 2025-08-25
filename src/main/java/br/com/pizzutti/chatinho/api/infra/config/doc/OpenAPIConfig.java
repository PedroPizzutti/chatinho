package br.com.pizzutti.chatinho.api.infra.config.doc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        var info = new Info()
                .title("chat-ws")
                .version("1.0")
                .description("Serviço de chat via web service");

        return new OpenAPI().info(info);
    }

}
