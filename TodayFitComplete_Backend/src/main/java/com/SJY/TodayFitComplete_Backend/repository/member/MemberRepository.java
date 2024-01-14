package com.SJY.TodayFitComplete_Backend.repository.member;

import com.SJY.TodayFitComplete_Backend.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

}
