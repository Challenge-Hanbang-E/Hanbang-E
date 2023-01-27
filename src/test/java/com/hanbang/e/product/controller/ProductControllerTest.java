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

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/product/list?search=아이&page=0&size=3&sort=price,asc",
			HttpMethod.GET, request, String.class);

		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");

		String data1 = dc.read("$.data.content[0].name");
		String data2 = dc.read("$.data.content[1].name");
		String data3 = dc.read("$.data.content[2].name");
		boolean lastPage = dc.read("$.data.last");

		assertThat(result).isEqualTo("success");
		assertThat(data1).isEqualTo("아이폰11");
		assertThat(data2).isEqualTo("아이폰12");
		assertThat(data3).isEqualTo("아이폰13");
		assertThat(true).isEqualTo(lastPage);

	}

	@DisplayName("상품 검색하기, 높은 가격순 조회")
	@Test
	public void searchProductOrderByHighToLowTest() {
		/* given - 데이터 준비 */
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

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/product/list?search=아이&page=0&size=3&sort=price,desc",
			HttpMethod.GET, request, String.class);

		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");

		String data1 = dc.read("$.data.content[0].name");
		String data2 = dc.read("$.data.content[1].name");
		String data3 = dc.read("$.data.content[2].name");
		boolean lastPage = dc.read("$.data.last");

		assertThat(result).isEqualTo("success");
		assertThat(data1).isEqualTo("아이폰13");
		assertThat(data2).isEqualTo("아이폰12");
		assertThat(data3).isEqualTo("아이폰11");
		assertThat(true).isEqualTo(lastPage);

	}

	@DisplayName("상품 검색하기, 판매량순 조회")
	@Test
	public void searchProductOrderByBestSellingTest() {
		/* given - 데이터 준비 */
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

		/* when - 테스트 실행 */
		HttpEntity<String> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = rt.exchange("/api/product/list?search=아이&page=0&size=3&sort=sales,desc",
			HttpMethod.GET, request, String.class);

		/* then - 검증 */
		DocumentContext dc = JsonPath.parse(response.getBody());
		String result = dc.read("$.result");

		String data1 = dc.read("$.data.content[0].name");
		String data2 = dc.read("$.data.content[1].name");
		String data3 = dc.read("$.data.content[2].name");
		boolean lastPage = dc.read("$.data.last");

		assertThat(result).isEqualTo("success");
		assertThat(data1).isEqualTo("아이폰12");
		assertThat(data2).isEqualTo("아이폰13");
		assertThat(data3).isEqualTo("아이폰11");
		assertThat(true).isEqualTo(lastPage);

	}

	@DisplayName("다음 페이지 데이터 있는지, 없는지 확인")
	@Test
	public void lastPageCheckTest() {
		/* given - 데이터 준비 */
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

		/* when - 다음 데이터가 존재하는 경우 테스트 실행 */
		HttpEntity<String> request1 = new HttpEntity<>(null, headers);
		ResponseEntity<String> response1 = rt.exchange("/api/product/list?search=아이&page=0&size=1&sort=sales,desc",
			HttpMethod.GET, request1, String.class);

		/* then - 검증 */
		DocumentContext dc1 = JsonPath.parse(response1.getBody());
		String result1 = dc1.read("$.result");

		String data1 = dc1.read("$.data.content[0].name");
		boolean lastPage1 = dc1.read("$.data.last");

		assertThat(result1).isEqualTo("success");
		assertThat(data1).isEqualTo("아이폰12");
		assertThat(false).isEqualTo(lastPage1);


		/* when - 다음 데이터가 존재하지 않는 경우 테스트 실행 */
		HttpEntity<String> request2 = new HttpEntity<>(null, headers);
		ResponseEntity<String> response2 = rt.exchange("/api/product/list?search=아이&page=0&size=3&sort=sales,desc",
			HttpMethod.GET, request2, String.class);

		/* then - 검증 */
		DocumentContext dc2 = JsonPath.parse(response2.getBody());
		String result2 = dc2.read("$.result");

		String data2 = dc2.read("$.data.content[0].name");
		boolean lastPage2 = dc2.read("$.data.last");

		assertThat(result2).isEqualTo("success");
		assertThat(data2).isEqualTo("아이폰12");
		assertThat(true).isEqualTo(lastPage2);

	}
}
