package com.hanbang.e.order.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;
import com.hanbang.e.order.dto.OrderReq;
import com.hanbang.e.order.entity.Orders;
import com.hanbang.e.order.repository.OrderRepository;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

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

	private static ObjectMapper om;
	private static HttpHeaders headers;


	@BeforeAll
	public static void init() {
		om = new ObjectMapper();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@DisplayName("상품 주문")
	@Test
	void doOrderTest() throws JsonProcessingException {
		/* given - 데이터 준비 */
		Member dummyMember = new Member("tj087@naver.com", "1234", "제주시");
		Product product1 = Product.builder()
			.productName("아이폰11")
			.price(500000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.build();
		memberRepository.save(dummyMember);
		productRepository.save(product1);

		OrderReq order = new OrderReq(1);
		String body = om.writeValueAsString(order);

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(body, headers);
		ResponseEntity<String> response = rt.exchange("/api/order?productId=1", HttpMethod.POST, request, String.class);

		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");
		String msg = dc.read("$.msg");

		assertThat(result).isEqualTo("success");
		assertThat(msg).isEqualTo("주문 성공");
	}

	@DisplayName("주문 삭제")
	@Test
	public void deleteOrderTest() throws JsonProcessingException {
		/* given - 데이터 준비 */
		Member dummyMember = new Member("tj087@naver.com", "1234", "제주시");
		Product product1 = Product.builder()
			.productName("아이폰11")
			.price(500000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.build();
		memberRepository.save(dummyMember);
		productRepository.save(product1);
		Orders dummyOrder = Orders.builder()
			.destination(dummyMember.getAddress())
			.quantity(1)
			.productPrice(product1.getPrice())
			.member(dummyMember)
			.build();
		orderRepository.save(dummyOrder);

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/order?orderId=1", HttpMethod.DELETE, request, String.class);

		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");
		String msg = dc.read("$.msg");

		assertThat(result).isEqualTo("success");
		assertThat(msg).isEqualTo("주문 삭제 성공");
	}
}