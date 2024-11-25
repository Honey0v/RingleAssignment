package com.example.ringleassignment.study.repository;

import com.example.ringleassignment.study.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
