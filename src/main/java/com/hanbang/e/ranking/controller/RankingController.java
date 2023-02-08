package com.hanbang.e.ranking.controller;

import com.hanbang.e.ranking.entity.Ranking;
import com.hanbang.e.ranking.service.RankingService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/api/rankingList")
    public List<Ranking> rankingList() {
        return rankingService.rankingList();
    }
}