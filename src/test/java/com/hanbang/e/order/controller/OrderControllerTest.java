package com.hanbang.e.order.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbang.e.common.jwt.JwtUtil;
import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;
import com.hanbang.e.order.entity.Orders;
import com.hanbang.e.order.repository.OrderRepository;
import com.hanbang.e.order.dto.OrderReq;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import javax.servlet.http.HttpServletRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

	@Autowired
	TestRestTemplate rt;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private JwtUtil jwtUtil;

	private static ObjectMapper om;
	private static HttpHeaders headers;

	@BeforeEach
	public void init() {
		om = new ObjectMapper();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@AfterEach
	void delete() {
		orderRepository.deleteAll();
		productRepository.deleteAll();
		memberRepository.deleteAll();
	}

	@DisplayName("상품 주문")
	@Test
	void doOrderTest() throws JsonProcessingException {
		/* given - 데이터 준비 */
		Member member = new Member("tj087@naver.com", "1234", "제주시");
		Product product = Product.builder()
				.productName("아이폰11")
				.price(500000L)
				.img("http....")
				.stock(10)
				.sales(50)
				.onSale(true)
				.build();
		memberRepository.save(member);
		productRepository.save(product);

		String token = jwtUtil.createToken(member.getMemberId());

		headers.add("Authorization", token);
		OrderReq order = new OrderReq(1);
		String body = om.writeValueAsString(order);

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(body, headers);

		ResponseEntity<String> response = rt.exchange("/api/order?productId="+product.getProductId(), HttpMethod.POST, request, String.class);

		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");
		String msg = dc.read("$.msg");

		assertThat(result).isEqualTo("success");
		assertThat(msg).isEqualTo("주문 성공");

	}

	@DisplayName("주문 조회 API")
	@Test
	void getMyOrderListTest() throws JsonProcessingException {
		/* given - 데이터 준비 */
		Product product1 = new Product("화장품", 23000L, "http://화장품.jpg", 50, 50, true);
		productRepository.save(product1);

		Product product2 = new Product("노트북 파우치", 36000L, "http://노트북_파우치.jpg", 50, 50, true);
		productRepository.save(product2);

		Product product3 = new Product("제로콜라세트", 12000L, "http://제로콜라세트.jpg", 50, 50, true);
		productRepository.save(product3);

		Member member = new Member("tj087@naver.com", "1234", "제주시");
		memberRepository.save(member);

		headers.add("Authorization", jwtUtil.createToken(member.getMemberId()));

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
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/order/list", HttpMethod.GET, request, String.class);

		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");
		String msg = dc.read("$.msg");
		Integer orderId = dc.read("$.data[1].orderId");
		String orderDate = dc.read("$.data[1].orderDate");
		Integer quantity = dc.read("$.data[1].quantity");
		Integer productPrice = dc.read("$.data[1].productPrice");
		Integer totalPrice = dc.read("$.data[1].totalPrice");
		Long productId = dc.read("$.data[1].productId", Long.class);
		String productName = dc.read("$.data[1].productName");
		String img = dc.read("$.data[1].img");

		assertThat(result).isEqualTo("success");
		assertThat(msg).isEqualTo("주문 조회 성공");
		assertThat(orderId).isEqualTo(2);
		assertThat(orderDate.split("\\.")[0]).isEqualTo(order2.getCreatedAt().toString().split("\\.")[0]);
		assertThat(quantity).isEqualTo(order2.getQuantity());
		assertThat(productPrice).isEqualTo(36000);
		assertThat(totalPrice).isEqualTo(72000);
		assertThat(productId).isEqualTo(order2.getProduct().getProductId());
		assertThat(productName).isEqualTo(order2.getProduct().getProductName());
		assertThat(img).isEqualTo(order2.getProduct().getImg());

	}

	@DisplayName("주문 삭제")
	@Test
	public void deleteOrderTest() throws JsonProcessingException {
		/* given - 데이터 준비 */
		Member member = new Member("tj087@naver.com", "1234", "제주시");
		Product product = Product.builder()
				.productName("아이폰11")
				.price(500000L)
				.img("http....")
				.stock(10)
				.sales(50)
				.onSale(true)
				.build();
		memberRepository.save(member);
		productRepository.save(product);

		headers.add("Authorization", jwtUtil.createToken(member.getMemberId()));
		Orders newOrder = Orders.builder()
			.destination(member.getAddress())
			.quantity(1)
			.productPrice(product.getPrice())
			.member(member)
			.product(product)
			.build();
		orderRepository.save(newOrder);

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/order?orderId=" + newOrder.getOrderId(), HttpMethod.DELETE, request, String.class);

		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");
		String msg = dc.read("$.msg");

		assertThat(result).isEqualTo("success");
		assertThat(msg).isEqualTo("주문 삭제 성공");
	}
}
