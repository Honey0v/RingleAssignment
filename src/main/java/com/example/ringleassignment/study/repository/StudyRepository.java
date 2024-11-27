package com.example.ringleassignment.study.repository;

import com.example.ringleassignment.study.entity.PossibleLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<PossibleLesson, Long> {
    void deleteByPossibleLessonIdAndTutor_MemberId(Long possibleLessonId, Long memberId);
    List<PossibleLesson> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);

}
