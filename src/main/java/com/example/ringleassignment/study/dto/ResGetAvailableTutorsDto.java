package com.example.ringleassignment.study.dto;

import com.example.ringleassignment.study.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ResGetAvailableTutorsDto {

    private List<Member> tutors;

    public ResGetAvailableTutorsDto(List<Member> tutors) {
        this.tutors = tutors;
    }
}
