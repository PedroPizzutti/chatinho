package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.component.TimeComponent;
import br.com.pizzutti.chatws.dto.UserInputDto;
import br.com.pizzutti.chatws.dto.UserDto;
import br.com.pizzutti.chatws.dto.LoginDto;
import br.com.pizzutti.chatws.enums.FilterOperationEnum;
import br.com.pizzutti.chatws.model.User;
import br.com.pizzutti.chatws.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService extends FilterService<User> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public <U> UserService filter(String property, U value, FilterOperationEnum operation) {
        super.filter(property, value, operation);
        return this;
    }

    public UserDto create(UserInputDto userInputDto) {
        var user = User.builder()
                .login(userInputDto.login())
                .nickname(userInputDto.nickname())
                .password(passwordEncoder.encode(userInputDto.password()))
                .createdAt(TimeComponent.getInstance().now())
                .build();

        this.userRepository.saveAndFlush(user);
        return UserDto.fromUser(user);
    }

    public User findByLogin(String login) {
        var spec = super.reset().filter("login", login, FilterOperationEnum.EQUAL).specification();
        return this.userRepository
                .findOne(spec)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuário não encontrado!"));
    }

    public User findById(Long id) {
        return this.userRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuário não encontrado!"));
    }

    public UserDto login(LoginDto loginDto) {
        var spec = super.reset().filter("login", loginDto.login(), FilterOperationEnum.EQUAL).specification();
        var user = this.userRepository
                .findOne(spec)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuário/senha inválido!"));

        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuário/senha inválido!");
        }

        return UserDto.fromUser(user);
    }
}
