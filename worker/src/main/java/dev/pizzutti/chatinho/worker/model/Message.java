package dev.pizzutti.chatinho.worker.model;

import dev.pizzutti.chatinho.worker.dto.MessageDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "content", columnDefinition = "TEXT")
        private String content;

        @Column(name = "type")
        private String type;

        @Column(name = "id_room")
        private Long idRoom;

        @Column(name = "id_user")
        private Long idUser;

        public static Message fromMessageDto(MessageDto messageDto) {
            return Message.builder()
                    .createdAt(messageDto.createdAt())
                    .type(messageDto.type())
                    .content(messageDto.content())
                    .idRoom(messageDto.idRoom())
                    .idUser(messageDto.idUser())
                    .build();
        }

}
