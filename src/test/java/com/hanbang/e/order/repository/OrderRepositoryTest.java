package com.hanbang.e.order.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;
import com.hanbang.e.order.entity.Orders;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ProductRepository productRepository;


	private Member member;
	private Product product1;
	private Product product2;
	private Product product3;

	@BeforeEach
	public void data() {
		product1 = new Product("화장품", 23000L, "http://화장품.jpg", 0, 100, false);
		productRepository.save(product1);

		product2 = new Product("노트북 파우치", 34000L, "http://노트북_파우치.jpg", 50, 50, true);
		productRepository.save(product2);

		product3 = new Product("제로콜라세트", 12000L, "http://제로콜라세트.jpg", 20, 80, true);
		productRepository.save(product3);

		member = new Member("mina@naver.com", "12345", "제주도");
		memberRepository.save(member);
	}

	@DisplayName("주문 목록 최신순 조회")
	@Test
	public void findOrdersByMemberOrderByCreatedAtDesc() {
		/* given - 데이터 준비 */
		Orders order1 = Orders.builder()
			.destination(member.getAddress())
			.quantity(3)
			.productPrice(23000L)
			.member(member)
			.product(product1)
			.build();
		orderRepository.save(order1);

		Orders order2 = Orders.builder()
			.destination(member.getAddress())
			.quantity(2)
			.productPrice(36000L)
			.member(member)
			.product(product2)
			.build();
		orderRepository.save(order2);

		Orders order3 = Orders.builder()
			.destination(member.getAddress())
			.quantity(5)
			.productPrice(12000L)
			.member(member)
			.product(product3)
			.build();
		orderRepository.save(order3);


		/* when - 테스트 실행 */
		List<Orders> orderList = orderRepository.findOrdersByMemberOrderByCreatedAtDesc(member);

		/* then - 검증 */
		assertThat(orderList.get(0).getCreatedAt().isAfter(orderList.get(1).getCreatedAt())).isTrue();
		assertThat(orderList.get(1).getCreatedAt().isAfter(orderList.get(2).getCreatedAt())).isTrue();

		assertThat(orderList.get(0).getOrderId() > orderList.get(1).getOrderId() &&
			orderList.get(1).getOrderId() > orderList.get(2).getOrderId()).isTrue();

	}

}
