package br.com.pizzutti.chatws.repository;

import br.com.pizzutti.chatws.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByRoom(Long room);
}
