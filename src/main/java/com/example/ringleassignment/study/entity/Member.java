package com.example.ringleassignment.study.entity;

import com.example.ringleassignment.study.dto.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public Member(Long memberId, String name, Role role) {
        this.memberId = memberId;
        this.name = name;
        this.role = role;
    }
}
