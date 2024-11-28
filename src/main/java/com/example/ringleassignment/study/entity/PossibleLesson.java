package com.example.ringleassignment.study.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_possible_lesson_tutor", columnList = "member_id"),
        @Index(name = "idx_possible_lesson_start_date", columnList = "startDate"),
        @Index(name = "idx_possible_lesson_end_date", columnList = "endDate"),
        @Index(name = "idx_possible_lesson_start_time", columnList = "startTime"),
        @Index(name = "idx_possible_lesson_end_time", columnList = "endTime")
})
public class PossibleLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long possibleLessonId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member tutor; // 해당 가능한 시간대가 속한 튜터

    @Column(nullable = false)
    private LocalDate startDate; // 가능한 시작 날짜

    @Column(nullable = false)
    private LocalDate endDate; // 가능한 종료 날짜

    @Column(nullable = false)
    private LocalTime startTime; // 가능한 시작 시간

    @Column(nullable = false)
    private LocalTime endTime; // 가능한 종료 시간

    @Builder
    public PossibleLesson(Member tutor, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        this.tutor = tutor;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
