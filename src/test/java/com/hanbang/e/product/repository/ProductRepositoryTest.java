package com.hanbang.e.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import com.hanbang.e.product.entity.Product;

@Sql("classpath:db/productDomainTableInit.sql")
@DataJpaTest
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	public void setup() {
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
	public void clear() {
		productRepository.deleteAll();
	}

	@DisplayName("검색결과 높은 가격순 조회")
	@Test
	public void OrderByHighToLowPriceTest() {

		/* given - 데이터 준비 */
		String search = "아이";
		Pageable pageable = PageRequest.of(0, 5);

		/* when - 테스트 실행 */
		List<Product> productList = productRepository.findByProductNameContainingOrderByPriceDesc(search, pageable);

		/* then - 검증 */
		assertThat(productList.get(0).getProductName()).isEqualTo("아이폰13");
	}

	@DisplayName("검색결과 낮은 가격순 조회")
	@Test
	public void OrderByLowToHighPriceTest() {

		/* given - 데이터 준비 */
		String search = "아이";
		Pageable pageable = PageRequest.of(0, 5);

		/* when - 테스트 실행 */
		List<Product> productList = productRepository.findByProductNameContainingOrderByPriceAsc(search, pageable);

		/* then - 검증 */
		assertThat(productList.get(0).getProductName()).isEqualTo("아이폰11");
	}

	@DisplayName("검색결과 판매량순 조회")
	@Test
	public void OrderByBestSellingTest() {

		/* given - 데이터 준비 */
		String search = "아이";
		Pageable pageable = PageRequest.of(0, 5);

		/* when - 테스트 실행 */
		List<Product> productList = productRepository.findByProductNameContainingOrderBySalesDesc(search, pageable);

		/* then - 검증 */
		assertThat(productList.get(0).getProductName()).isEqualTo("아이폰12");
	}

	@DisplayName("검색결과 판매량순 조회 페이징 작동 확인")
	@Test
	public void OrderByBestSellingPagingTest() {

		/* given - 데이터 준비 */
		String search = "아이";
		Pageable pageable = PageRequest.of(1, 2);

		/* when - 테스트 실행 */
		List<Product> productList = productRepository.findByProductNameContainingOrderBySalesDesc(search, pageable);

		/* then - 검증 */
		assertThat(productList.get(0).getProductName()).isEqualTo("아이폰11");
	}
}