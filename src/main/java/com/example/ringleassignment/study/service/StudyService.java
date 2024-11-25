package com.example.ringleassignment.study.service;

import com.example.ringleassignment.study.dto.ReqCreatePossibleLessonDto;
import com.example.ringleassignment.study.dto.ReqDeletePossibleLessonDto;
import com.example.ringleassignment.study.dto.ResCreatePossibleLessonDto;
import com.example.ringleassignment.study.entity.PossibleLesson;

import java.util.List;

public interface StudyService {
    ResCreatePossibleLessonDto createPossibleLesson(ReqCreatePossibleLessonDto reqCreatePossibleLessonDto, Long memberId);

    void deletePossibleLesson(ReqDeletePossibleLessonDto reqDeletePossibleLessonDto, Long memberId);
}
