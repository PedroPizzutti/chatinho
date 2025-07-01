package br.com.pizzutti.chatws.repository;

import br.com.pizzutti.chatws.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByRoom(Long room, Pageable pageable);
}
