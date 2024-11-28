package com.example.ringleassignment.study.repository;

import com.example.ringleassignment.study.entity.Lesson;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @EntityGraph(attributePaths = {"tutor", "student"})
    List<Lesson> findByDate(LocalDate date);

    @EntityGraph(attributePaths = {"tutor", "student"})
    List<Lesson> findByStudent_MemberId(Long studentId);
}

