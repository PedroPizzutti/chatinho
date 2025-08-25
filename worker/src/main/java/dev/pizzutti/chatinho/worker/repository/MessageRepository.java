package dev.pizzutti.chatinho.worker.repository;

import dev.pizzutti.chatinho.worker.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
