package br.com.pizzutti.chatws.repository;

import br.com.pizzutti.chatws.model.Totem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TotemRepository extends JpaRepository<Totem, Long>, JpaSpecificationExecutor<Totem> {
}
