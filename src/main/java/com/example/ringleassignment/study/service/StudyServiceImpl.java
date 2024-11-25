package com.example.ringleassignment.study.service;

import com.example.ringleassignment.study.dto.ReqCreatePossibleLessonDto;
import com.example.ringleassignment.study.dto.ReqDeletePossibleLessonDto;
import com.example.ringleassignment.study.dto.ResCreatePossibleLessonDto;
import com.example.ringleassignment.study.dto.Role;
import com.example.ringleassignment.study.entity.Member;
import com.example.ringleassignment.study.entity.PossibleLesson;
import com.example.ringleassignment.study.repository.MemberRepository;
import com.example.ringleassignment.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

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


}
