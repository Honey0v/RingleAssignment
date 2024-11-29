package com.example.ringleassignment.study.controller;

import com.example.ringleassignment.common.dto.Message;
import com.example.ringleassignment.common.handler.StatusCode;
import com.example.ringleassignment.study.dto.*;
import com.example.ringleassignment.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/study")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;

    @PostMapping("/possible-lesson")
    public ResponseEntity<Message> createPossibleLesson( // 튜터가 수업 가능한 시간대 추가
            @RequestBody ReqCreatePossibleLessonDto reqCreatePossibleLessonDto,
            @RequestParam Long memberId) {
        return ResponseEntity.ok(new Message(StatusCode.OK, studyService.createPossibleLesson(reqCreatePossibleLessonDto, memberId)));
    }

    @DeleteMapping("/possible-lesson")
    public ResponseEntity<Message> deletePossibleLesson( // 튜터가 추가한 시간대 삭제
            @RequestBody ReqDeletePossibleLessonDto reqDeletePossibleLessonDto,
            @RequestParam Long memberId) {
        studyService.deletePossibleLesson(reqDeletePossibleLessonDto, memberId);
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @GetMapping("/possible-lessons")
    public ResponseEntity<Message> getPossibleLessons( // 학생이 이용 가능한 시간대 조회
            @RequestBody ReqGetPossibleLessonDto reqGetPossibleLessonDto) {
        return ResponseEntity.ok(new Message(StatusCode.OK, studyService.getAvailableLessonTimes(reqGetPossibleLessonDto)));
    }

    @GetMapping("/available-tutors") // 학생이 시간대를 바탕으로 수업 가능한 튜터를 조회
    public ResponseEntity<Message> getAvailableTutors(@RequestBody ReqGetAvailableTutorsDto reqGetAvailableTutorsDto) {
        return ResponseEntity.ok(new Message(StatusCode.OK, studyService.getAvailableTutors(reqGetAvailableTutorsDto)));
    }

    @PostMapping("/lesson") // 수강신청
    public ResponseEntity<Message> createLesson(@RequestBody ReqCreateLessonDto reqCreateLessonDto) {
        return ResponseEntity.ok(new Message(StatusCode.OK, studyService.createLesson(reqCreateLessonDto)));
    }

    @GetMapping("/my-lessons") // 신청한 수업들을 조회
    public ResponseEntity<Message> getMyLessons(@RequestBody ReqGetMyLessonDto reqGetMyLessonDto) {
        return ResponseEntity.ok(new Message(StatusCode.OK, studyService.getMyLessons(reqGetMyLessonDto)));
    }
}
