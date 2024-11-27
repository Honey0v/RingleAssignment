package com.example.ringleassignment.study.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ReqCreateLessonDto {
    private Long tutorId;
    private Long studentId;
    private LocalDate date;
    private LocalTime startTime;
    private Integer duration;

    public ReqCreateLessonDto(Long tutorId, Long studentId, LocalDate date, LocalTime startTime, Integer duration) {
        this.tutorId = tutorId;
        this.studentId = studentId;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
    }
}