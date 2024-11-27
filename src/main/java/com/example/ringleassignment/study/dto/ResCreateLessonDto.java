package com.example.ringleassignment.study.dto;

import lombok.Getter;

@Getter
public class ResCreateLessonDto {
    private final Long lessonId;

    public ResCreateLessonDto(Long lessonId) {
        this.lessonId = lessonId;
    }
}