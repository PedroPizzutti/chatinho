package br.com.pizzutti.chatws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ApiConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
            auth -> {
                auth.requestMatchers("v1/auth/**", "v1/ws/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated();
            }
        ).csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }
}
