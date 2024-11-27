package com.example.ringleassignment.study.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReqGetPossibleLessonDto {
    private LocalDate date; // 조회할 날짜
    private int lessonDurationMinutes; // 수업 길이 (30 또는 60분)

    public ReqGetPossibleLessonDto(LocalDate date, int lessonDurationMinutes) {
        this.date = date;
        this.lessonDurationMinutes = lessonDurationMinutes;
    }
}
