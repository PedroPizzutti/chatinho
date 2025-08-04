package br.com.pizzutti.chatws.repository;

import br.com.pizzutti.chatws.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InviteRepository extends JpaRepository<Invite, Long>, JpaSpecificationExecutor<Invite> {
}
