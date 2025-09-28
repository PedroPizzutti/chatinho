package br.com.pizzutti.chatinho.api.infra.config.security;

import br.com.pizzutti.chatinho.api.domain.user.UserService;
import br.com.pizzutti.chatinho.api.infra.config.communication.AdviceDto;
import br.com.pizzutti.chatinho.api.infra.config.communication.AdviceEnum;
import br.com.pizzutti.chatinho.api.domain.token.TokenService;
import br.com.pizzutti.chatinho.api.infra.service.TimeService;
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
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AntPathMatcher matcher;
    private final TokenService tokenService;
    private final UserService userService;
    private final ObjectMapper mapper;

    public JwtAuthFilter(TokenService tokenService,
                         UserService userService,
                         ObjectMapper mapper) {
        this.tokenService = tokenService;
        this.userService = userService;
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
            var idUser = tokenService.validateAccessToken(token);
            var user = this.userService.findById(Long.parseLong(idUser));
            var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            var adviceDto = AdviceDto.builder()
                    .path(request.getRequestURI())
                    .status(401)
                    .timestamp(TimeService.now())
                    .code(AdviceEnum.INVALID_CREDENTIALS)
                    .errors(List.of(e.getLocalizedMessage()))
                    .build();
            response.setStatus(adviceDto.status());
            response.setContentType("application/json");
            response.getWriter().write(mapper.writeValueAsString(adviceDto));
        }
    }

    private boolean isPublicPath(String path) {
        return PublicRoutes.PATHS.stream().anyMatch(p -> matcher.match(p, path));
    }
}
