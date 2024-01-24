package com.blog.api.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private LocalDateTime createAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Session> sessions = new ArrayList<>();

    public Session addSession() {
        Session session = Session.builder()
                .user(this)
                .build();
        sessions.add(session);
        return session;
    }

    @Builder
    public User(String name, String email, String password, LocalDateTime createAt) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createAt = LocalDateTime.now();
    }
}
