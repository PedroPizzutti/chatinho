package br.com.pizzutti.chatws.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "login")
    private String login;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;
}
