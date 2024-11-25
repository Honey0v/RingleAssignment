package com.example.ringleassignment.study.repository;

import com.example.ringleassignment.study.entity.PossibleLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<PossibleLesson, Long> {
    void deleteByPossibleLessonIdAndTutor_MemberId(Long possibleLessonId, Long memberId);

}
