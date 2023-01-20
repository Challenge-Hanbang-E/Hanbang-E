package com.hanbang.e.order.service;

import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;
import com.hanbang.e.order.entity.Orders;
import com.hanbang.e.order.repository.OrderRepository;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;
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
public class OrderServiceDecreaseLockTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("주문취소 동시에 100개의요청")
    public void deleteOrderAtSameTime() throws InterruptedException {

        /* 상품 정보 생성 */
        Product product = new Product("맥북프로", 1000000L, "img", 0, 100, true);
        productRepository.save(product);

        /* 회원 정보 생성 */
        Member member = new Member("yj0718@gmail.com", "dPwls12!", "address");
        memberRepository.save(member);

        /* 주문 정보 생성 */
        for(int i=0; i<100; i++){
            Orders orders = new Orders("제주도", 1, 1000000L, member, product);
            orderRepository.save(orders);
        }

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long memberId = 1L;
        Long orderId = 0L;
        for(Long i=0L; i<threadCount; i++){
            orderId++;
            Long finalOrderId = orderId;
            executorService.submit(() -> {
                try {
                    orderService.deleteOrder(memberId, finalOrderId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        Product result = productRepository.findAll().get(0);

        assertThat(result.getStock()).isEqualTo(100);
        assertThat(result.getSales()).isEqualTo(0);
    }
}
