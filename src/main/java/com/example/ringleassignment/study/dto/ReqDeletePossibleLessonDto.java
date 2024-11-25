package com.example.ringleassignment.study.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqDeletePossibleLessonDto {

    private Long possibleLessonId; // 삭제할 수업 가능한 시간대 ID
}
