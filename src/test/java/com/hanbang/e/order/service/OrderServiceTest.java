package com.hanbang.e.order.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.hanbang.e.common.dto.ResponseDto;
import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;
import com.hanbang.e.order.dto.OrderReq;
import com.hanbang.e.order.entity.Orders;
import com.hanbang.e.order.repository.OrderRepository;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;



@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private ProductRepository productRepository;

	private Member member;

	@BeforeEach
	public void memberData() {

		Long fakeMemberId = 1L;

		member = new Member("mina@naver.com", "12345", "제주도");
		ReflectionTestUtils.setField(member, "memberId", fakeMemberId);

		when(memberRepository.findById(fakeMemberId))
			.thenReturn(Optional.of(member));

	}

	@DisplayName("주문 성공 경우")
	@Test
	public void insertOrder_success() {

		/* given - 데이터 준비 */
		Long fakeMemberId = 1L;
		Long fakeProductId = 1L;
		OrderReq orderReq = new OrderReq(5);


		/* stub - 가짜 객체 행동 정의 */
		String productName = "맥북 프로";
		Long price = 8720L;
		String img = "https://맥북프로.jpg";
		int stock = 5;
		int sales = 95;
		Boolean onSale = true;

		Product product = new Product(productName, price, img, stock, sales, onSale);
		ReflectionTestUtils.setField(product, "productId", fakeProductId);
		Optional<Product> fakeProductOP = Optional.of(product);
		when(productRepository.findById(fakeProductId)).thenReturn(fakeProductOP);

		int beforeStock = fakeProductOP.get().getStock();
		int beforeSales = fakeProductOP.get().getSales();

		Long fakeOrderId = 1L;
		int orderQuantity = orderReq.getQuantity();
		Orders order = orderReq.toEntity(member, product, orderQuantity);
		when(orderRepository.save(any())).thenReturn(order);


		/* when - 테스트 실행 */
		ResponseDto response = orderService.insertOrder(fakeMemberId, fakeProductId, orderReq);

		int afterStock = fakeProductOP.get().getStock();
		int afterSales = fakeProductOP.get().getSales();


		/* then - 검증 */
		// product.sell() 정상 동작 확인
		assertThat(afterStock).isEqualTo(beforeStock - orderQuantity);
		assertThat(afterSales).isEqualTo(beforeSales + orderQuantity);
		// order 확인
		assertThat(order.getDestination()).isEqualTo(member.getAddress());
		assertThat(order.getQuantity()).isEqualTo(orderQuantity);
		assertThat(order.getProductPrice()).isEqualTo(product.getPrice());
		assertThat(order.getTotalPrice()).isEqualTo(product.getPrice() * orderQuantity);
		assertThat(order.getMember()).isEqualTo(member);
		assertThat(order.getProduct()).isEqualTo(product);
		// 결과 확인
		assertThat(response.getResult()).isEqualTo("success");
		assertThat(response.getMsg()).isEqualTo("주문 성공");

	}

	@DisplayName("현재 판매하는 상품이 아닌 경우, onSale=false")
	@Test
	public void insertOrder_onSaleCheck() {

		/* given - 데이터 준비 */
		Long fakeMemberId = 1L;
		Long fakeProductId = 1L;
		OrderReq orderReq = new OrderReq(2);

		/* stub - 가짜 객체 행동 정의 */
		// 고정 값
		String productName = "맥북 프로";
		Long price = 8720L;
		String img = "https://맥북프로.jpg";
		// 변동 값
		int stock = 100;
		int sales = 0;
		Boolean onSale = false;

		Product product = new Product(productName, price, img, stock, sales, onSale);
		ReflectionTestUtils.setField(product, "productId", fakeProductId);
		Optional<Product> fakeProductOP = Optional.of(product);
		when(productRepository.findById(fakeProductId)).thenReturn(fakeProductOP);

		/* when & then - 테스트 실행 및 검증 */
		assertThatThrownBy(() -> orderService.insertOrder(fakeMemberId, fakeProductId, orderReq))
			.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> orderService.insertOrder(fakeMemberId, fakeProductId, orderReq))
			.hasMessageContaining("현재 판매하는 상품이 아닙니다.");

	}

	@DisplayName("현재 재고가 없는 상품인 경우, stock=0")
	@Test
	public void insertOrder_stockCheck() {

		/* given - 데이터 준비 */
		Long fakeMemberId = 1L;
		Long fakeProductId = 1L;
		OrderReq orderReq = new OrderReq(2);

		/* stub - 가짜 객체 행동 정의 */
		// 고정 값
		String productName = "맥북 프로";
		Long price = 8720L;
		String img = "https://맥북프로.jpg";
		// 변동 값
		int stock = 0;
		int sales = 100;
		Boolean onSale = true;

		Product product = new Product(productName, price, img, stock, sales, onSale);
		ReflectionTestUtils.setField(product, "productId", fakeProductId);
		Optional<Product> fakeProductOP = Optional.of(product);
		when(productRepository.findById(fakeProductId)).thenReturn(fakeProductOP);

		/* when & then - 테스트 실행 및 검증 */
		assertThatThrownBy(() -> orderService.insertOrder(fakeMemberId, fakeProductId, orderReq))
			.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> orderService.insertOrder(fakeMemberId, fakeProductId, orderReq))
			.hasMessageContaining("현재 재고가 없는 상품입니다.");

	}

	@DisplayName("현재 재고보다 주문수량이 많이 들어 온 경우, stock - quantity < 0")
	@Test
	public void insertOrder_stockAndQuantityCheck() {

		/* given - 데이터 준비 */
		Long fakeMemberId = 1L;
		Long fakeProductId = 1L;
		OrderReq orderReq = new OrderReq(5);

		/* stub - 가짜 객체 행동 정의 */
		// 고정 값
		String productName = "맥북 프로";
		Long price = 8720L;
		String img = "https://맥북프로.jpg";
		// 변동 값
		int stock = 3;
		int sales = 97;
		Boolean onSale = true;

		Product product = new Product(productName, price, img, stock, sales, onSale);
		ReflectionTestUtils.setField(product, "productId", fakeProductId);

		Optional<Product> fakeProductOP = Optional.of(product);
		when(productRepository.findById(fakeProductId)).thenReturn(fakeProductOP);

		/* when & then - 테스트 실행 및 검증 */
		assertThat(product.getStock() - orderReq.getQuantity() < 0).isTrue();
		assertThatThrownBy(() -> orderService.insertOrder(fakeMemberId, fakeProductId, orderReq))
			.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> orderService.insertOrder(fakeMemberId, fakeProductId, orderReq))
			.hasMessageContaining("현재 남은 재고는 %d개 입니다.", product.getStock());
	}



}
