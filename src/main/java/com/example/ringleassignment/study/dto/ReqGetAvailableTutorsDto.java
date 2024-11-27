package com.example.ringleassignment.study.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ReqGetAvailableTutorsDto {
    private LocalDate date;      // 요청 날짜
    private LocalTime startTime; // 시작 시간
    private int duration;        // 수업 길이 (분 단위)
}
