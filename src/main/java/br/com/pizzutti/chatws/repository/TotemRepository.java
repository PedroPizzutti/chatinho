package br.com.pizzutti.chatws.repository;

import br.com.pizzutti.chatws.model.Totem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TotemRepository extends JpaRepository<Totem, Long> {
    Optional<Totem> findByGuid(String guid);
}
