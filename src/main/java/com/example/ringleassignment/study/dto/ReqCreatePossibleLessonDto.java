package com.example.ringleassignment.study.dto;

import com.example.ringleassignment.study.entity.PossibleLesson;
import com.example.ringleassignment.study.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreatePossibleLessonDto {

    private LocalDate startDate; // 시작 날짜
    private LocalDate endDate;   // 종료 날짜
    private LocalTime startTime; // 시작 시간
    private LocalTime endTime;   // 종료 시간

    public PossibleLesson toEntity(Member tutor) {
        return PossibleLesson.builder()
                .tutor(tutor)
                .startDate(startDate)
                .endDate(endDate)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}