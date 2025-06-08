package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.dto.CreateUserDto;
import br.com.pizzutti.chatws.model.User;
import br.com.pizzutti.chatws.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(String login, String password) {
        var user = User.builder()
                .login(login)
                .password(passwordEncoder.encode(password))
                .createdAt(LocalDateTime.now())
                .build();

        return this.userRepository.save(user);
    }


}
