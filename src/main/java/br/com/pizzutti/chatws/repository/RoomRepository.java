package br.com.pizzutti.chatws.repository;

import br.com.pizzutti.chatws.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
