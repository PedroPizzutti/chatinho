package br.com.pizzutti.chatws.repository;

import br.com.pizzutti.chatws.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByLogin(String login);
}
