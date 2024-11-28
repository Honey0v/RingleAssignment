package com.example.ringleassignment.study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateLessonDto {
    private Long tutorId;
    private Long studentId;
    private LocalDate date;
    private LocalTime startTime;
    private Integer duration;
}