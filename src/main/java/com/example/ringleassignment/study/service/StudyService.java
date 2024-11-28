package com.example.ringleassignment.study.service;

import com.example.ringleassignment.study.dto.*;

public interface StudyService {
    ResCreatePossibleLessonDto createPossibleLesson(ReqCreatePossibleLessonDto reqCreatePossibleLessonDto, Long memberId);
    void deletePossibleLesson(ReqDeletePossibleLessonDto reqDeletePossibleLessonDto, Long memberId);
    ResGetPossibleLessonDto getAvailableLessonTimes(ReqGetPossibleLessonDto reqGetPossibleLessonDto);
    ResGetAvailableTutorsDto getAvailableTutors(ReqGetAvailableTutorsDto reqGetAvailableTutorsDto);
    ResCreateLessonDto createLesson(ReqCreateLessonDto reqCreateLessonDto);
    ResGetMyLessonsDto getMyLessons(ReqGetMyLessonDto reqGetMyLessonDto);
}
