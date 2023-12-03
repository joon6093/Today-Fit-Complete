package com.SJY.TodayFitComplete_Backend.repository;

import com.SJY.TodayFitComplete_Backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

}
