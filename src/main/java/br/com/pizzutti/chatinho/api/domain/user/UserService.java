package br.com.pizzutti.chatinho.api.domain.user;

import br.com.pizzutti.chatinho.api.infra.service.TimeService;
import br.com.pizzutti.chatinho.api.infra.service.FilterOperationEnum;
import br.com.pizzutti.chatinho.api.infra.service.FilterService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService extends FilterService<User> implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        super();
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public <U> UserService filter(String property, U value, FilterOperationEnum operation) {
        super.filter(property, value, operation);
        return this;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return this.userRepository
                    .findOne(super.filter("login", username, FilterOperationEnum.EQUAL).specification())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuário/senha inválido!"));
        } finally {
            super.reset();
        }
    }

    public User create(UserPostDto userPostDto) {
        var user = User.builder()
                .login(userPostDto.login())
                .nickname(userPostDto.nickname())
                .password(passwordEncoder.encode(userPostDto.password()))
                .createdAt(TimeService.now())
                .build();
        return this.userRepository.saveAndFlush(user);
    }

    public User findById(Long id) {
        return this.userRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuário não encontrado!"));
    }

    public List<User> find() {
        try {
            return this.userRepository.findAll(super.specification());
        } finally {
            super.reset();
        }
    }
}
