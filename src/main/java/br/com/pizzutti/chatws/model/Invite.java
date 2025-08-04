package br.com.pizzutti.chatws.model;

import br.com.pizzutti.chatws.enums.InviteStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "invite")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invite {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private InviteStatusEnum status;

    @Column(name = "id_user_from")
    private Long idUserFrom;

    @Column(name = "id_user_to")
    private Long idUserTo;

    @Column(name = "id_room")
    private Long idRoom;
}
