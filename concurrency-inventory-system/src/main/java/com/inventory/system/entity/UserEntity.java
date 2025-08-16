package com.inventory.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name = "password", nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;
}
