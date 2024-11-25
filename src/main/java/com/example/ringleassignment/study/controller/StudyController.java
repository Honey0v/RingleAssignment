package com.example.ringleassignment.study.controller;

import com.example.ringleassignment.common.dto.Message;
import com.example.ringleassignment.common.handler.StatusCode;
import com.example.ringleassignment.study.dto.ReqCreatePossibleLessonDto;
import com.example.ringleassignment.study.dto.ReqDeletePossibleLessonDto;
import com.example.ringleassignment.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
