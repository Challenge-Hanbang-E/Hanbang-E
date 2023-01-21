package com.hanbang.e.member.service;

import com.hanbang.e.member.dto.MemberCreateReq;
import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;
import com.hanbang.e.order.dto.OrderReq;
import com.hanbang.e.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Redisson Lock 테스트")
public class memberServiceDecreaseLockTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("동시에 100명의 회원이 회원가입")
    public void insertOrderAtSameTime() throws InterruptedException {

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++){
            int finalI = i;
            executorService.submit(() -> {
                try {
                    MemberCreateReq newMember = MemberCreateReq.builder()
                            .email("user" + finalI + "@naver.com")
                            .password("dPwls12!")
                            .address("제주")
                            .build();
                    memberService.signup(newMember);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        long result = memberRepository.count();
        assertThat(result).isEqualTo(100L);
    }
}
