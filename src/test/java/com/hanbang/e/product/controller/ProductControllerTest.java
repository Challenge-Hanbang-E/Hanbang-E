package com.hanbang.e.product.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

	@Autowired
	TestRestTemplate rt;

	@Autowired
	private ProductRepository productRepository;

	private static ObjectMapper om;
	private static HttpHeaders headers;

	@BeforeAll
	public static void init() {
		om = new ObjectMapper();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@BeforeEach
	public void dataSet() {
		Product product1 = Product.builder()
			.productName("아이폰11")
			.price(500000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.build();
		Product product2 = Product.builder()
			.productName("아이폰12")
			.price(1000000L)
			.img("http....")
			.stock(15)
			.sales(100)
			.onSale(true)
			.build();
		Product product3 = Product.builder()
			.productName("아이폰13")
			.price(1200000L)
			.img("http....")
			.stock(10)
			.sales(75)
			.onSale(true)
			.build();
		productRepository.save(product1);
		productRepository.save(product2);
		productRepository.save(product3);
	}

	@AfterEach
	public void dataClear() {
		productRepository.deleteAll();
	}

	@DisplayName("상품 상세조회")
	@Test
	public void getProductDetailsTest() {
		/* given - 데이터 준비 */

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/product/details/1", HttpMethod.GET, request, String.class);

		System.out.println(response);
		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");
		String name = dc.read("$.data.name");

		assertThat(result).isEqualTo("success");
		assertThat(name).isEqualTo("아이폰11");
	}

	@DisplayName("상품 검색하기, 낮은 가격순 조회")
	@Test
	public void searchProductOrderByLowToHighTest() {
		/* given - 데이터 준비 */

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/product/list?search=아이&orderby=priceasc&page=0&size=5",
			HttpMethod.GET, request, String.class);

		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");

		String data1 = dc.read("$.data.items[0].name");
		String data2 = dc.read("$.data.items[1].name");
		String data3 = dc.read("$.data.items[2].name");

		assertThat(result).isEqualTo("success");
		assertThat(data1).isEqualTo("아이폰11");
		assertThat(data2).isEqualTo("아이폰12");
		assertThat(data3).isEqualTo("아이폰13");
	}

	@DisplayName("상품 검색하기, 높은 가격순 조회")
	@Test
	public void searchProductOrderByHighToLowTest() {
		/* given - 데이터 준비 */

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/product/list?search=아이&orderby=pricedesc&page=0&size=5",
			HttpMethod.GET, request, String.class);

		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");

		String data1 = dc.read("$.data.items[0].name");
		String data2 = dc.read("$.data.items[1].name");
		String data3 = dc.read("$.data.items[2].name");

		assertThat(result).isEqualTo("success");
		assertThat(data1).isEqualTo("아이폰13");
		assertThat(data2).isEqualTo("아이폰12");
		assertThat(data3).isEqualTo("아이폰11");
	}

	@DisplayName("상품 검색하기, 판매량순 조회")
	@Test
	public void searchProductOrderByBestSellingTest() {
		/* given - 데이터 준비 */

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/product/list?search=아이&orderby=popular&page=0&size=5",
			HttpMethod.GET, request, String.class);

		System.out.println(response);
		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");

		String data1 = dc.read("$.data.items[0].name");
		String data2 = dc.read("$.data.items[1].name");
		String data3 = dc.read("$.data.items[2].name");

		assertThat(result).isEqualTo("success");
		assertThat(data1).isEqualTo("아이폰12");
		assertThat(data2).isEqualTo("아이폰13");
		assertThat(data3).isEqualTo("아이폰11");
	}
}