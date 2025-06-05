package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.dto.CreateUserDto;
import br.com.pizzutti.chatws.model.User;
import br.com.pizzutti.chatws.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(CreateUserDto createUserDto) {
        var user = User.builder()
                .login(createUserDto.login())
                .password(createUserDto.password())
                .createdAt(LocalDateTime.now())
                .build();

        return this.userRepository.save(user);
    }


}
