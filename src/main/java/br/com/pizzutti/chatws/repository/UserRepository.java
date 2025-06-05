package br.com.pizzutti.chatws.repository;

import br.com.pizzutti.chatws.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
