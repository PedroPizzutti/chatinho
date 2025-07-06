package br.com.pizzutti.chatws.repository;

import br.com.pizzutti.chatws.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    SELECT * FROM room
                     WHERE id IN (SELECT id_room FROM member WHERE id_user = :idUser)
            """
    )
    public List<Room> findAllByIdUser(Long idUser);

}
