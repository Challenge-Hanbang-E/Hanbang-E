package com.hanbang.e.order.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.hanbang.e.order.dto.OrderResp;
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


	@DisplayName("상품 주문 API - 주문 성공 경우")
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

		Member member = new Member("mina@naver.com", "12345", "제주도");
		ReflectionTestUtils.setField(member, "memberId", fakeMemberId);
		when(memberRepository.findById(fakeMemberId)).thenReturn(Optional.of(member));

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

	@DisplayName("상품 주문 API - 현재 판매하는 상품이 아닌 경우, onSale=false")
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

		Member member = new Member("mina@naver.com", "12345", "제주도");
		ReflectionTestUtils.setField(member, "memberId", fakeMemberId);
		when(memberRepository.findById(fakeMemberId)).thenReturn(Optional.of(member));

		/* when & then - 테스트 실행 및 검증 */
		assertThatThrownBy(() -> orderService.insertOrder(fakeMemberId, fakeProductId, orderReq))
			.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> orderService.insertOrder(fakeMemberId, fakeProductId, orderReq))
			.hasMessageContaining("현재 판매하는 상품이 아닙니다.");
	}

	@DisplayName("상품 주문 API - 현재 재고가 없는 상품인 경우, stock=0")
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

		Member member = new Member("mina@naver.com", "12345", "제주도");
		ReflectionTestUtils.setField(member, "memberId", fakeMemberId);
		when(memberRepository.findById(fakeMemberId)).thenReturn(Optional.of(member));

		/* when & then - 테스트 실행 및 검증 */
		assertThatThrownBy(() -> orderService.insertOrder(fakeMemberId, fakeProductId, orderReq))
			.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> orderService.insertOrder(fakeMemberId, fakeProductId, orderReq))
			.hasMessageContaining("현재 재고가 없는 상품입니다.");

	}

	@DisplayName("상품 주문 API - 현재 재고보다 주문수량이 많이 들어 온 경우, stock - quantity < 0")
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
		
    	Member member = new Member("mina@naver.com", "12345", "제주도");
		ReflectionTestUtils.setField(member, "memberId", fakeMemberId);
    	when(memberRepository.findById(fakeMemberId)).thenReturn(Optional.of(member));

		/* when & then - 테스트 실행 및 검증 */
		assertThat(product.getStock() - orderReq.getQuantity() < 0).isTrue();
		assertThatThrownBy(() -> orderService.insertOrder(fakeMemberId, fakeProductId, orderReq))
			.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> orderService.insertOrder(fakeMemberId, fakeProductId, orderReq))
			.hasMessageContaining("현재 남은 재고는 %d개 입니다.", product.getStock());
	}

	@DisplayName("주문 조회 API")
	@Test
	public void findMyOrderList() {

		/* given - 데이터 준비 */
		Long fakeMemberId = 1L;

		/* stub - 가짜 객체 행동 정의 */
		Member member = new Member("mina@naver.com", "12345", "제주도");
		ReflectionTestUtils.setField(member, "memberId", fakeMemberId);
		when(memberRepository.findById(fakeMemberId)).thenReturn(Optional.of(member));

		Product product1 = new Product("화장품", 23000L, "http://화장품.jpg", 0, 100, false);
		ReflectionTestUtils.setField(product1, "productId", 1L);

		Product product2 = new Product("노트북 파우치", 34000L, "http://노트북_파우치.jpg", 50, 50, true);
		ReflectionTestUtils.setField(product2, "productId", 2L);

		Product product3 = new Product("제로콜라세트", 12000L, "http://제로콜라세트.jpg", 20, 80, true);
		ReflectionTestUtils.setField(product2, "productId", 3L);

		Orders order1 = Orders.builder()
			.destination(member.getAddress())
			.quantity(3)
			.productPrice(23000L)
			.member(member)
			.product(product1)
			.build();
		ReflectionTestUtils.setField(order1, "orderId", 3L);
		ReflectionTestUtils.setField(order1, "createdAt", LocalDateTime.of(2023, 01, 9, 02, 54, 15, 126515));

		Orders order2 = Orders.builder()
			.destination(member.getAddress())
			.quantity(2)
			.productPrice(36000L)
			.member(member)
			.product(product2)
			.build();
		ReflectionTestUtils.setField(order2, "orderId", 2L);
		ReflectionTestUtils.setField(order2, "createdAt", LocalDateTime.of(2023, 01, 9, 02, 54, 06, 516230));


		Orders order3 = Orders.builder()
			.destination(member.getAddress())
			.quantity(5)
			.productPrice(12000L)
			.member(member)
			.product(product3)
			.build();
		ReflectionTestUtils.setField(order3, "orderId", 1L);
		ReflectionTestUtils.setField(order3, "createdAt",  LocalDateTime.of(2023, 01, 9, 02, 53, 19, 539770));

		List<Orders> orderList = new ArrayList<>();
		orderList.add(order1);
		orderList.add(order2);
		orderList.add(order3);

		when(orderRepository.findOrdersByMemberOrderByCreatedAtDesc(member)).thenReturn(orderList);

		/* when - 테스트 실행 */
		ResponseDto<List<OrderResp>> response = orderService.findMyOrderList(fakeMemberId);

		/* then - 검증 */
		assertThat(response.getData().get(0).getOrderId()).isEqualTo(order1.getOrderId());
		assertThat(response.getData().get(1).getOrderId()).isEqualTo(order2.getOrderId());
		assertThat(response.getData().get(2).getOrderId()).isEqualTo(order3.getOrderId());

		assertThat(response.getData().get(0).getOrderDate()).isEqualTo(order1.getCreatedAt());
		assertThat(response.getData().get(0).getTotalPrice()).isEqualTo(order1.getTotalPrice());
		assertThat(response.getData().get(1).getQuantity()).isEqualTo(order2.getQuantity());
		assertThat(response.getData().get(2).getProductPrice()).isEqualTo(order3.getProductPrice());
		assertThat(response.getData().get(0).getProductId()).isEqualTo(order1.getProduct().getProductId());
		assertThat(response.getData().get(1).getProductName()).isEqualTo(order2.getProduct().getProductName());
		assertThat(response.getData().get(2).getImg()).isEqualTo(order3.getProduct().getImg());

	}

	@DisplayName("주문 삭제 확인, 작성자와 요청자가 다른 경우")
	@Test
	public void deleteOrderTest() {
		/* given - 데이터 준비 */
		Long requestMemberId = 2L;

		/* stub - 가짜 객체 행동 정의 */
		Member member = new Member("mina@naver.com", "12345", "제주도");
		ReflectionTestUtils.setField(member, "memberId", 1L);
		Product productPS = Product.builder()
			.productName("맥북 프로")
			.price(1000000L)
			.img("https://맥북프로.jpg")
			.stock(10)
			.sales(3)
			.onSale(true)
			.build();
		Orders orders = Orders.builder()
			.destination("제주도")
			.quantity(1)
			.productPrice(1000000L)
			.member(member)
			.product(productPS)
			.build();
		Optional<Orders> ordersPS = Optional.of(orders);
		when(orderRepository.findById(any())).thenReturn(ordersPS);

		/* when & then - 테스트 실행 및 검증 */
		assertThatThrownBy(
			() -> {
				orderService.deleteOrder(requestMemberId, 1L);
			}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 삭제 권한이 없습니다.");
	}
}
