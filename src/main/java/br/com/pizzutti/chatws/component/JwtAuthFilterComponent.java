package br.com.pizzutti.chatws.component;

import br.com.pizzutti.chatws.config.PublicRoutesConfig;
import br.com.pizzutti.chatws.dto.AdviceDto;
import br.com.pizzutti.chatws.enums.AdviceEnum;
import br.com.pizzutti.chatws.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilterComponent extends OncePerRequestFilter {

    private final AntPathMatcher matcher;
    private final TokenService tokenService;
    private final ObjectMapper mapper;

    public JwtAuthFilterComponent(TokenService tokenService,
                                  ObjectMapper mapper) {
        this.tokenService = tokenService;
        this.mapper = mapper;
        this.matcher = new AntPathMatcher();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (isPublicPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            var user = tokenService.validateToken(token);
            var auth = new UsernamePasswordAuthenticationToken(user, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            var adviceDto = AdviceDto.builder()
                    .path(request.getRequestURI())
                    .status(401)
                    .timestamp(TimeComponent.getInstance().now())
                    .code(AdviceEnum.INVALID_CREDENTIALS)
                    .errors(List.of(e.getLocalizedMessage()))
                    .build();
            response.setStatus(adviceDto.status());
            response.setContentType("application/json");
            response.getWriter().write(mapper.writeValueAsString(adviceDto));
        }
    }

    private boolean isPublicPath(String path) {
        return PublicRoutesConfig.PATHS.stream().anyMatch(p -> matcher.match(p, path));
    }
}
