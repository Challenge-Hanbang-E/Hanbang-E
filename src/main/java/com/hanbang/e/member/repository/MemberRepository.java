package com.hanbang.e.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbang.e.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
