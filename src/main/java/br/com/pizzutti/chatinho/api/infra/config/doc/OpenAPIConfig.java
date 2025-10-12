package br.com.pizzutti.chatinho.api.infra.config.doc;

import br.com.pizzutti.chatinho.api.infra.config.communication.AdviceEnum;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

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

    @Bean
    public OpenApiCustomizer globalResponses() {
        var adviceResponses = new HashMap<String, ApiResponse>();
        for (var advice : AdviceEnum.values()) {
            if (advice.toHttpStatus() < 100) continue;
            var schema = new MediaType().schema(new Schema<>().$ref("#/components/schemas/AdviceDto"));
            var content = new Content().addMediaType("application/json", schema);
            adviceResponses.put(
                    String.valueOf(advice.toHttpStatus()),
                    new ApiResponse().description(advice.name()).content(content)
            ) ;
        }
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                        var responses = operation.getResponses();
                        adviceResponses.forEach(responses::addApiResponse);
                })
        );
    }

}
