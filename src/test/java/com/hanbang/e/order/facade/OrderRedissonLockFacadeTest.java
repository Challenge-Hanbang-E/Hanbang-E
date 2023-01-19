package com.hanbang.e.order.facade;

import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;
import com.hanbang.e.order.dto.OrderReq;
import com.hanbang.e.order.entity.Orders;
import com.hanbang.e.order.repository.OrderRepository;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;
import org.hibernate.criterion.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderRedissonLockFacadeTest {

    @Autowired
    private OrderRedissonLockFacade orderRedissonLockFacade;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 동시에_100개의요청() throws InterruptedException {

        /* 상품 정보 생성 */
        Product product = new Product("맥북프로", 1000000L, "img", 100, 0, true);
        productRepository.saveAndFlush(product);

        /* 회원 정보 생성 */
        Member member = new Member("yj0718@gmail.com", "dPwls12!", "address");
        memberRepository.saveAndFlush(member);

        /* 주문 정보 생성 */
        Orders orders = new Orders("제주도", 1, 1000000L, member, product);
        orderRepository.saveAndFlush(orders);

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++){
            executorService.submit(() -> {
                try {
                    Long key = 1L;
                    Long memberId = 1L;
                    Long productId = 1L;
                    OrderReq orderReq = new OrderReq(1);
                    orderRedissonLockFacade.insertOrder(key, memberId, productId, orderReq);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        Product product2 = productRepository.findById(1L).orElseThrow();

        // 100 - (100 * 1) = 0
        assertEquals(0, product2.getStock());

    }


}
