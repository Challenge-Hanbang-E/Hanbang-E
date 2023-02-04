package com.hanbang.e.product.controller;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.test.context.TestExecutionListeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbang.e.common.env.AcceptanceTestExecutionListener;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.entity.ProductIndex;
import com.hanbang.e.product.repository.ProductIndexRepository;
import com.hanbang.e.product.repository.ProductRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class,}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

	@Autowired
	TestRestTemplate rt;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductIndexRepository productIndexRepository;

	private static ObjectMapper om;
	private static HttpHeaders headers;

	@BeforeAll
	public static void init() {
		om = new ObjectMapper();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@DisplayName("상품 상세조회")
	@Test
	public void getProductDetailsTest() {
		/* given - 데이터 준비 */
		Product product1 = Product.builder()
			.productName("아이폰11")
			.price(500000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.build();
		productRepository.save(product1);

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/product/details/"+ product1.getProductId(), HttpMethod.GET, request, String.class);

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
		ProductIndex product1 = ProductIndex.builder()
			.productName("아이폰11")
			.price(500000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.build();
		ProductIndex product2 = ProductIndex.builder()
			.productName("아이폰12")
			.price(1000000L)
			.img("http....")
			.stock(15)
			.sales(100)
			.onSale(true)
			.build();
		ProductIndex product3 = ProductIndex.builder()
			.productName("아이폰13")
			.price(1200000L)
			.img("http....")
			.stock(10)
			.sales(75)
			.onSale(true)
			.build();

		productIndexRepository.save(product1);
		productIndexRepository.save(product2);
		productIndexRepository.save(product3);

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/product/list?search=아이&page=0&size=3&sort=price,asc",
			HttpMethod.GET, request, String.class);

		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");

		String data1 = dc.read("$.data[0].name");
		String data2 = dc.read("$.data[1].name");
		String data3 = dc.read("$.data[2].name");

		Boolean nextPage = dc.read("$.hasNextPage");

		assertThat(result).isEqualTo("success");
		assertThat(data1).isEqualTo("아이폰11");
		assertThat(data2).isEqualTo("아이폰12");
		assertThat(data3).isEqualTo("아이폰13");
		assertThat(nextPage).isEqualTo(false);
	}

	@DisplayName("상품 검색하기, 높은 가격순 조회")
	@Test
	public void searchProductOrderByHighToLowTest() {
		/* given - 데이터 준비 */
		ProductIndex product1 = ProductIndex.builder()
			.productName("아이폰11")
			.price(500000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.build();
		ProductIndex product2 = ProductIndex.builder()
			.productName("아이폰12")
			.price(1000000L)
			.img("http....")
			.stock(15)
			.sales(100)
			.onSale(true)
			.build();
		ProductIndex product3 = ProductIndex.builder()
			.productName("아이폰13")
			.price(1200000L)
			.img("http....")
			.stock(10)
			.sales(75)
			.onSale(true)
			.build();

		productIndexRepository.save(product1);
		productIndexRepository.save(product2);
		productIndexRepository.save(product3);

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/product/list?search=아이&page=0&size=3&sort=price,desc",
			HttpMethod.GET, request, String.class);

		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");

		String data1 = dc.read("$.data[0].name");
		String data2 = dc.read("$.data[1].name");
		String data3 = dc.read("$.data[2].name");

		Boolean nextPage = dc.read("$.hasNextPage");

		assertThat(result).isEqualTo("success");
		assertThat(data1).isEqualTo("아이폰13");
		assertThat(data2).isEqualTo("아이폰12");
		assertThat(data3).isEqualTo("아이폰11");
		assertThat(nextPage).isEqualTo(false);
	}

	@DisplayName("상품 검색하기, 판매량순 조회")
	@Test
	public void searchProductOrderByBestSellingTest() {
		/* given - 데이터 준비 */
		ProductIndex product1 = ProductIndex.builder()
			.productName("아이폰11")
			.price(500000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.build();
		ProductIndex product2 = ProductIndex.builder()
			.productName("아이폰12")
			.price(1000000L)
			.img("http....")
			.stock(15)
			.sales(100)
			.onSale(true)
			.build();
		ProductIndex product3 = ProductIndex.builder()
			.productName("아이폰13")
			.price(1200000L)
			.img("http....")
			.stock(10)
			.sales(75)
			.onSale(true)
			.build();

		productIndexRepository.save(product1);
		productIndexRepository.save(product2);
		productIndexRepository.save(product3);

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/product/list?search=아이&page=0&size=3&sort=sales,desc",
			HttpMethod.GET, request, String.class);

		System.out.println(response);
		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");

		String data1 = dc.read("$.data[0].name");
		String data2 = dc.read("$.data[1].name");
		String data3 = dc.read("$.data[2].name");

		Boolean nextPage = dc.read("$.hasNextPage");

		assertThat(result).isEqualTo("success");
		assertThat(data1).isEqualTo("아이폰12");
		assertThat(data2).isEqualTo("아이폰13");
		assertThat(data3).isEqualTo("아이폰11");
		assertThat(nextPage).isEqualTo(false);
	}
}
