package br.com.pizzutti.chatws.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "totem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Totem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expires_in")
    private Integer expiresIn;

    @Column(name = "guid")
    private String guid;

    @Column(name = "is_used")
    private Boolean used;
}
