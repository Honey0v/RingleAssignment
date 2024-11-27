package com.example.ringleassignment.study.controller;

import com.example.ringleassignment.common.dto.Message;
import com.example.ringleassignment.common.handler.StatusCode;
import com.example.ringleassignment.study.dto.*;
import com.example.ringleassignment.study.entity.Member;
import com.example.ringleassignment.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/study")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyServiceImpl;

    @PostMapping("/possible-lesson")
    public ResponseEntity<Message> createPossibleLesson(
            @RequestBody ReqCreatePossibleLessonDto reqCreatePossibleLessonDto,
            @RequestParam Long memberId) {
        return ResponseEntity.ok(new Message(StatusCode.OK, studyServiceImpl.createPossibleLesson(reqCreatePossibleLessonDto, memberId)));
    }

    @DeleteMapping("/possible-lesson")
    public ResponseEntity<Message> deletePossibleLesson(
            @RequestBody ReqDeletePossibleLessonDto reqDeletePossibleLessonDto,
            @RequestParam Long memberId) {
        studyServiceImpl.deletePossibleLesson(reqDeletePossibleLessonDto, memberId);
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @GetMapping("/possible-lessons")
    public ResponseEntity<Message> getPossibleLessons(
            @RequestBody ReqGetPossibleLessonDto reqGetPossibleLessonDto) {
        return ResponseEntity.ok(new Message(StatusCode.OK, studyServiceImpl.getAvailableLessonTimes(reqGetPossibleLessonDto)));
    }

    @GetMapping("/available-tutors")
    public ResponseEntity<Message> getAvailableTutors(@RequestBody ReqGetAvailableTutorsDto reqGetAvailableTutorsDto) {
        return ResponseEntity.ok(new Message(StatusCode.OK, studyServiceImpl.getAvailableTutors(reqGetAvailableTutorsDto)));
    }

    @PostMapping("/lesson")
    public ResponseEntity<Message> createLesson(@RequestBody ReqCreateLessonDto reqCreateLessonDto) {
        return ResponseEntity.ok(new Message(StatusCode.OK, studyServiceImpl.createLesson(reqCreateLessonDto)));
    }
}
