package com.example.ringleassignment.study.service;

import com.example.ringleassignment.common.handler.CustomException;
import com.example.ringleassignment.common.handler.StatusCode;
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
import java.util.*;
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
        // 튜터 멤버 조회 및 검증
        Member tutor = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));

        if (!tutor.getRole().equals(Role.TUTOR)) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        // 새로운 가능 수업 생성 및 저장
        PossibleLesson possibleLesson = dto.toEntity(tutor);
        PossibleLesson savedLesson = studyRepository.save(possibleLesson);

        return new ResCreatePossibleLessonDto(savedLesson.getPossibleLessonId());
    }

    @Override
    @Transactional
    public void deletePossibleLesson(ReqDeletePossibleLessonDto reqDeletePossibleLessonDto, Long memberId) {
        // 삭제할 수업 조회 및 검증
        PossibleLesson possibleLesson = studyRepository.findById(reqDeletePossibleLessonDto.getPossibleLessonId())
                .orElseThrow(() -> new CustomException(StatusCode.NOT_EXIST));

        if (!possibleLesson.getTutor().getMemberId().equals(memberId)) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        // 특정 튜터의 수업 삭제
        studyRepository.deleteByPossibleLessonIdAndTutor_MemberId(possibleLesson.getPossibleLessonId(), memberId);
    }

    @Override
    public ResGetPossibleLessonDto getAvailableLessonTimes(ReqGetPossibleLessonDto reqGetPossibleLessonDto) {
        int lessonDurationMinutes = reqGetPossibleLessonDto.getLessonDurationMinutes();

        if (lessonDurationMinutes != 30 && lessonDurationMinutes != 60) {
            throw new CustomException(StatusCode.MALFORMED);
        }

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalDate requestDate = reqGetPossibleLessonDto.getDate();

        // 가능한 수업 시간대 조회
        List<PossibleLesson> possibleLessons = studyRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(requestDate, requestDate);
        if (requestDate.isEqual(today)) {
            possibleLessons = possibleLessons.stream()
                    .filter(lesson -> lesson.getEndTime().isAfter(now))
                    .collect(Collectors.toList());
        }

        // 예약된 수업 정보 조회
        List<Lesson> reservedLessons = lessonRepository.findByDate(requestDate);
        Map<Long, List<LocalTime[]>> tutorReservedTimes = reservedLessons.stream()
                .collect(Collectors.groupingBy(
                        lesson -> lesson.getTutor().getMemberId(),
                        Collectors.mapping(
                                lesson -> new LocalTime[]{lesson.getStartTime(), lesson.getStartTime().plusMinutes(lesson.getDuration())},
                                Collectors.toList()
                        )
                ));

        // 가능한 시간 계산
        Set<LocalTime> availableTimes = new HashSet<>();
        for (PossibleLesson lesson : possibleLessons) {
            Member tutor = lesson.getTutor();
            List<LocalTime[]> reservedTimes = tutorReservedTimes.getOrDefault(tutor.getMemberId(), new ArrayList<>());

            LocalTime currentTime = lesson.getStartTime();
            while (!currentTime.plusMinutes(lessonDurationMinutes).isAfter(lesson.getEndTime())) {
                final LocalTime currentStartTime = currentTime;
                LocalTime currentEndTime = currentStartTime.plusMinutes(lessonDurationMinutes);

                // 해당 튜터의 예약된 시간과 겹치는지 확인
                boolean isConflict = reservedTimes.stream()
                        .anyMatch(times ->
                                currentStartTime.isBefore(times[1]) && currentEndTime.isAfter(times[0])
                        );

                if (!isConflict) {
                    availableTimes.add(currentTime); // Set에 추가
                }

                currentTime = currentTime.plusMinutes(lessonDurationMinutes);
            }
        }

        // Set에서 List로 변환 후 정렬
        List<LocalTime> sortedAvailableTimes = availableTimes.stream()
                .sorted()
                .collect(Collectors.toList());

        return new ResGetPossibleLessonDto(sortedAvailableTimes);
    }




    @Override
    @Transactional(readOnly = true)
    public ResGetAvailableTutorsDto getAvailableTutors(ReqGetAvailableTutorsDto reqGetAvailableTutorsDto) {
        LocalDate date = reqGetAvailableTutorsDto.getDate();
        LocalTime requestStartTime = reqGetAvailableTutorsDto.getStartTime();
        int duration = reqGetAvailableTutorsDto.getDuration();
        LocalTime requestEndTime = requestStartTime.plusMinutes(duration);

        // 요청 날짜에 해당하는 가능한 수업 조회
        List<PossibleLesson> possibleLessons = studyRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date);

        // 이미 예약된 수업 정보 가져오기
        List<Lesson> reservedLessons = lessonRepository.findByDate(date);

        // 가능한 튜터 필터링
        List<Member> availableTutors = possibleLessons.stream()
                .filter(lesson -> {
                    Member tutor = lesson.getTutor();

                    // 해당 튜터의 예약된 시간 가져오기
                    List<Lesson> tutorReservedLessons = reservedLessons.stream()
                            .filter(reserved -> reserved.getTutor().getMemberId().equals(tutor.getMemberId()))
                            .collect(Collectors.toList());

                    // 가능한 시간대에서 예약된 시간을 제외
                    LocalTime startTime = lesson.getStartTime();
                    LocalTime endTime = lesson.getEndTime();

                    for (Lesson reserved : tutorReservedLessons) {
                        LocalTime reservedStart = reserved.getStartTime();
                        LocalTime reservedEnd = reservedStart.plusMinutes(reserved.getDuration());

                        // 요청 시간대와 예약된 시간대가 겹치지 않아야 함
                        if (requestEndTime.isAfter(reservedStart) && requestStartTime.isBefore(reservedEnd)) {
                            return false; // 예약된 시간대와 겹치는 경우 필터링
                        }
                    }

                    // 요청 시간대가 가능한 시간대 내에 있어야 함
                    return !requestStartTime.isBefore(startTime) && !requestEndTime.isAfter(endTime);
                })
                .map(PossibleLesson::getTutor)
                .distinct()
                .collect(Collectors.toList());

        return new ResGetAvailableTutorsDto(availableTutors);
    }




    @Override
    @Transactional
    public ResCreateLessonDto createLesson(ReqCreateLessonDto reqCreateLessonDto) {
        try {
            // 튜터 및 학생 조회
            Member tutor = memberRepository.findById(reqCreateLessonDto.getTutorId())
                    .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));
            Member student = memberRepository.findById(reqCreateLessonDto.getStudentId())
                    .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));

            // 새로운 수업 생성 및 저장
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
            // 동시성 문제 발생 시 예외 처리
            throw new CustomException(StatusCode.CONFLICT);
        }
    }

    @Override
    public ResGetMyLessonsDto getMyLessons(ReqGetMyLessonDto reqGetMyLessonDto) {
        // 학생의 모든 수업 조회
        List<Lesson> lessons = lessonRepository.findByStudent_MemberId(reqGetMyLessonDto.getStudentId());

        // 수업 정보를 Response 객체로 변환
        List<Lesson> lessonDetails = lessons.stream()
                .map(lesson -> new Lesson(
                        lesson.getLessonId(),
                        lesson.getTutor(),
                        lesson.getStudent(),
                        lesson.getDate(),
                        lesson.getStartTime(),
                        lesson.getDuration()))
                .toList();

        return new ResGetMyLessonsDto(lessonDetails);
    }
}
