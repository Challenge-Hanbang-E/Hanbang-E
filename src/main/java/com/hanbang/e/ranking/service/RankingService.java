package com.hanbang.e.ranking.service;

import com.hanbang.e.ranking.entity.Ranking;
import com.hanbang.e.ranking.repository.RankingRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    @Transactional(readOnly = true)
    public List<Ranking> rankingList() {
        return rankingRepository.findAll();
    }
}