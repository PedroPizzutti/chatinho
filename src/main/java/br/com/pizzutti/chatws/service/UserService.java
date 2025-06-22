package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.dto.UserCreateDto;
import br.com.pizzutti.chatws.dto.UserCreatedDto;
import br.com.pizzutti.chatws.dto.UserLoginDto;
import br.com.pizzutti.chatws.model.User;
import br.com.pizzutti.chatws.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TimeService timeService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TimeService timeService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.timeService = timeService;
    }

    public UserCreatedDto create(UserCreateDto userCreateDto) {
        var user = User.builder()
                .login(userCreateDto.login())
                .nickname(userCreateDto.nickname())
                .password(passwordEncoder.encode(userCreateDto.password()))
                .createdAt(this.timeService.now())
                .build();

        this.userRepository.saveAndFlush(user);
        return UserCreatedDto.fromUser(user);
    }

    public User findByLogin(String login) {
        return this.userRepository
                .findByLogin(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuário não encontrado!"));
    }

    public User findById(Long id) {
        return this.userRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuário não encontrado!"));
    }

    public UserCreatedDto login(UserLoginDto userLoginDto) {
        var user = this.userRepository
                .findByLogin(userLoginDto.login())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuário/senha inválido!"));

        if (!passwordEncoder.matches(userLoginDto.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuário/senha inválido!");
        }

        return UserCreatedDto.fromUser(user);
    }
}
