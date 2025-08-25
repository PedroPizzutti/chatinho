package br.com.pizzutti.chatinho.api.domain.totem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TotemRepository extends JpaRepository<Totem, Long>, JpaSpecificationExecutor<Totem> {
}
