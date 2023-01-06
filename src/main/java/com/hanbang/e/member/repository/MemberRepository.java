package com.hanbang.e.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbang.e.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}