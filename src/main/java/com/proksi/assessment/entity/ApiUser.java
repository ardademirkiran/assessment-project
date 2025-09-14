package com.proksi.assessment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proksi.assessment.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_user")
@Getter
@Setter
public class ApiUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role role;
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;
}
