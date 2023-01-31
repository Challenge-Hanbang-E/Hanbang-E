package com.hanbang.e.order.service;

import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;
import com.hanbang.e.order.dto.OrderReq;
import com.hanbang.e.order.entity.Orders;
import com.hanbang.e.order.repository.OrderRepository;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
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

    @BeforeEach
    public void setup() {
        /* 상품 정보 생성 */
        Product product1 = new Product("맥북프로", 1000000L, "img", 100, 0, true);
        productRepository.saveAndFlush(product1);

        /* 회원 정보 생성 */
        Member member = new Member("yj0718@gmail.com", "dPwls12!", "address");
        memberRepository.saveAndFlush(member);
    }
    @AfterEach
    public void delete() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("주문생성 동시에 100개의요청")
    public void insertOrderAtSameTime() throws InterruptedException {

        Product product = productRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);

        /* 요청 정보 생성 */
        OrderReq request = new OrderReq(1);

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderService.insertOrder(member.getMemberId(), product.getProductId(), request);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Long result = orderRepository.count();
        assertThat(result).isEqualTo(100L);
    }

    @Test
    @DisplayName("주문취소 동시에 100개의요청")
    public void deleteOrderAtSameTime() throws InterruptedException {
        /* 상품 정보 생성 */
        Product product = productRepository.findAll().get(0);

        /* 회원 정보 생성 */
        Member member = memberRepository.findAll().get(0);

        /* 주문 정보 생성 */
        for (int i = 0; i < 100; i++) {
            Orders orders = new Orders("제주도", 1, 1000000L + i, member, product);
            orderRepository.saveAndFlush(orders);
        }

        List<Orders> ordersList = orderRepository.findAll();

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            Long orderId = ordersList.get(i).getOrderId();
            executorService.submit(() -> {
                try {
                    orderService.deleteOrder(member.getMemberId(), orderId);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        assertThat(orderRepository.count()).isEqualTo(0);
        assertThat(product.getStock()).isEqualTo(100);
        assertThat(product.getSales()).isEqualTo(0);
    }
}
