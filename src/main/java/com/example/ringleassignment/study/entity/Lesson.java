package com.example.ringleassignment.study.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Member member;

}
