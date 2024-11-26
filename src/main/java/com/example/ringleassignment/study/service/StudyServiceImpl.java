package com.example.ringleassignment.study.service;

import com.example.ringleassignment.study.dto.*;
import com.example.ringleassignment.study.entity.Lesson;
import com.example.ringleassignment.study.entity.Member;
import com.example.ringleassignment.study.entity.PossibleLesson;
import com.example.ringleassignment.study.repository.LessonRepository;
import com.example.ringleassignment.study.repository.MemberRepository;
import com.example.ringleassignment.study.repository.StudyRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final LessonRepository lessonRepository;

    @Override
    @Transactional
    public ResCreatePossibleLessonDto createPossibleLesson(ReqCreatePossibleLessonDto dto, Long memberId) {
        Member tutor = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("튜터 정보가 잘못되었습니다."));

        if (!tutor.getRole().equals(Role.TUTOR)) {
            throw new IllegalArgumentException("해당 멤버는 튜터 역할이 아닙니다.");
        }

        PossibleLesson possibleLesson = dto.toEntity(tutor);
        PossibleLesson savedLesson = studyRepository.save(possibleLesson);

        return new ResCreatePossibleLessonDto(savedLesson.getPossibleLessonId());
}


    @Override
    @Transactional
    public void deletePossibleLesson(ReqDeletePossibleLessonDto reqDeletePossibleLessonDto, Long memberId) {
        PossibleLesson possibleLesson = studyRepository.findById(reqDeletePossibleLessonDto.getPossibleLessonId())
                .orElseThrow(() -> new IllegalArgumentException("해당 수업이 존재하지 않습니다."));

        if (!possibleLesson.getTutor().getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        studyRepository.deleteByPossibleLessonIdAndTutor_MemberId(possibleLesson.getPossibleLessonId(), memberId);
    }

    @Override
    public ResGetPossibleLessonDto getAvailableLessonTimes(ReqGetPossibleLessonDto reqGetPossibleLessonDto) {
        int lessonDurationMinutes = reqGetPossibleLessonDto.getLessonDurationMinutes();

        if (lessonDurationMinutes != 30 && lessonDurationMinutes != 60) {
            throw new IllegalArgumentException("수업 길이는 30분 또는 60분만 가능합니다.");
        }

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalDate requestDate = reqGetPossibleLessonDto.getDate();

        // 날짜 비교
        List<PossibleLesson> possibleLessons;
        if (requestDate.isEqual(today)) {
            possibleLessons = studyRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(requestDate, requestDate)
                    .stream()
                    .filter(lesson -> lesson.getEndTime().isAfter(now)) // 오늘 날짜에서는 현재 시간 이후만 가능
                    .collect(Collectors.toList());
        } else {
            possibleLessons = studyRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(requestDate, requestDate);
        }

        List<LocalTime> availableTimes = new ArrayList<>();
        for (PossibleLesson lesson : possibleLessons) {
            LocalTime currentTime = lesson.getStartTime();
            while (!currentTime.plusMinutes(lessonDurationMinutes).isAfter(lesson.getEndTime())) {
                if (requestDate.isEqual(today) && currentTime.isBefore(now)) {
                    currentTime = currentTime.plusMinutes(lessonDurationMinutes);
                    continue; // 현재 시간 이전은 제외
                }
                availableTimes.add(currentTime);
                currentTime = currentTime.plusMinutes(lessonDurationMinutes);
            }
        }

        return new ResGetPossibleLessonDto(availableTimes);
    }

    @Override
    @Transactional(readOnly = true)
    public ResGetAvailableTutorsDto getAvailableTutors(ReqGetAvailableTutorsDto reqGetAvailableTutorsDto) {
        LocalDate date = reqGetAvailableTutorsDto.getDate();
        LocalTime time = reqGetAvailableTutorsDto.getStartTime();

        List<PossibleLesson> possibleLessons = studyRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date);

        List<Member> availableTutors = possibleLessons.stream()
                .filter(lesson -> !time.plusMinutes(reqGetAvailableTutorsDto.getDuration()).isAfter(lesson.getEndTime()) &&
                        !time.isBefore(lesson.getStartTime()))
                .map(lesson -> {
                    Member tutor = lesson.getTutor();
                    return new Member(tutor.getMemberId(), tutor.getName());
                })
                .distinct() // 중복 튜터 제거
                .collect(Collectors.toList());

        return new ResGetAvailableTutorsDto(availableTutors);
    }

    @Override
    @Transactional
    public ResCreateLessonDto createLesson(ReqCreateLessonDto reqCreateLessonDto) {
        try {
            Member tutor = memberRepository.findById(reqCreateLessonDto.getTutorId())
                    .orElseThrow(() -> new IllegalArgumentException("튜터가 존재하지 않습니다."));
            Member student = memberRepository.findById(reqCreateLessonDto.getStudentId())
                    .orElseThrow(() -> new IllegalArgumentException("학생이 존재하지 않습니다."));

            Lesson lesson = Lesson.builder()
                    .tutor(tutor)
                    .student(student)
                    .date(reqCreateLessonDto.getDate())
                    .startTime(reqCreateLessonDto.getStartTime())
                    .duration(reqCreateLessonDto.getDuration())
                    .build();

            Lesson savedLesson = lessonRepository.save(lesson);
            return new ResCreateLessonDto(savedLesson.getLessonId());
        } catch (OptimisticLockException e) {
            // 낙관적 잠금 예외 처리
            throw new IllegalStateException("동시에 수강 신청을 시도하여 충돌이 발생했습니다. 다시 시도해주세요.", e);
        }
    }


}
