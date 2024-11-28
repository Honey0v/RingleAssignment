package com.example.ringleassignment.study.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lesson", indexes = {
        @Index(name = "idx_lesson_date", columnList = "date"),
        @Index(name = "idx_lesson_tutor_id", columnList = "tutor_id"),
        @Index(name = "idx_lesson_student_id", columnList = "student_id")
})
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Member tutor;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Member student;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private Integer duration;

    @Version // 낙관적 잠금을 위한 버전 관리
    private Integer version;

    @Builder
    public Lesson(Long lessonId, Member tutor, Member student, LocalDate date, LocalTime startTime, Integer duration) {
        this.lessonId = lessonId;
        this.tutor = tutor;
        this.student = student;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
    }
}
