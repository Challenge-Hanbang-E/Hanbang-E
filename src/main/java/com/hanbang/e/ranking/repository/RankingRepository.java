package com.hanbang.e.ranking.repository;

import com.hanbang.e.ranking.entity.Ranking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
}