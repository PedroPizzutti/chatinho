package br.com.pizzutti.chatinho.api.domain.room;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "room")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "id_user")
    private Long idOwner;
}
