package br.com.pizzutti.chatws.model;

import br.com.pizzutti.chatws.enums.MessageEnum;
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

    @Column(name = "room")
    private Long room;

    @Column(name = "user")
    private Long user;
}
