package br.com.pizzutti.chatinho.api.domain.invite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InviteRepository extends JpaRepository<Invite, Long>, JpaSpecificationExecutor<Invite> {
}
