package com.example.ringleassignment.study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ResGetPossibleLessonDto {
    private List<LocalTime> availableTimes; // 가능한 시간 목록
}
