package com.example.ringleassignment.study.dto;

import com.example.ringleassignment.study.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResGetMyLessonsDto {
    private List<Lesson> lessons;
}
