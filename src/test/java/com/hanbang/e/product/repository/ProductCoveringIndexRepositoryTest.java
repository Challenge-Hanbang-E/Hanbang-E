package com.hanbang.e.product.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.hanbang.e.product.dto.ProductSimpleResp;
import com.hanbang.e.product.entity.ProductCoveringIndex;

@DataJpaTest
class ProductCoveringIndexRepositoryTest {

	@Autowired
	private ProductCoveringIndexRepository productRepository;

	@BeforeEach
	public void setup() {
		ProductCoveringIndex product1 = ProductCoveringIndex.builder()
			.productName("아이폰11")
			.price(500000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.build();
		ProductCoveringIndex product2 = ProductCoveringIndex.builder()
			.productName("아이폰12")
			.price(1000000L)
			.img("http....")
			.stock(15)
			.sales(100)
			.onSale(true)
			.build();
		ProductCoveringIndex product3 = ProductCoveringIndex.builder()
			.productName("아이폰13")
			.price(1200000L)
			.img("http....")
			.stock(10)
			.sales(75)
			.onSale(true)
			.build();
		ProductCoveringIndex product4 = ProductCoveringIndex.builder()
			.productName("아이폰14")
			.price(1500000L)
			.img("http....")
			.stock(5)
			.sales(80)
			.onSale(true)
			.build();

		productRepository.save(product1);
		productRepository.save(product2);
		productRepository.save(product3);
		productRepository.save(product4);
	}

	@AfterEach
	public void clear() {
		productRepository.deleteAll();
	}

	@DisplayName("검색어에 해당하는 상품이 없는 검색")
	@Test
	void noMatchingKeywordTest() {
		String search = "존재하지 않는 상품";
		Sort property = Sort.by(Sort.Direction.DESC, "price");
		Pageable pageable = PageRequest.of(0, 5, property);

		List<ProductSimpleResp> productList = productRepository.findPagesWithCoveringIndex(search, pageable)
			.getContent();

		assertThat(productList.size()).isEqualTo(0);
	}

	@DisplayName("높은 가격순 검색")
	@Test
	void orderByPriceDescTest() {

		/* given - 데이터 준비 */
		String search = "아이";
		Sort property = Sort.by(Sort.Direction.DESC, "price");
		Pageable pageable = PageRequest.of(0, 5, property);

		/* when - 테스트 실행 */
		List<ProductSimpleResp> productList = productRepository.findPagesWithCoveringIndex(search, pageable)
			.getContent();

		/* then - 검증 */
		assertThat(productList.get(0).getPrice()).isEqualTo(1500000L);
		assertThat(productList.get(1).getPrice()).isEqualTo(1200000L);
		assertThat(productList.get(2).getPrice()).isEqualTo(1000000L);

	}

	@DisplayName("낮은 가격순 검색")
	@Test
	void orderByPriceAscTest() {

		/* given - 데이터 준비 */
		String search = "아이";
		Sort property = Sort.by(Sort.Direction.ASC, "price");
		Pageable pageable = PageRequest.of(0, 5, property);

		/* when - 테스트 실행 */
		List<ProductSimpleResp> productList = productRepository.findPagesWithCoveringIndex(search, pageable)
			.getContent();

		/* then - 검증 */
		assertThat(productList.get(0).getPrice()).isEqualTo(500000L);
		assertThat(productList.get(1).getPrice()).isEqualTo(1000000L);
		assertThat(productList.get(2).getPrice()).isEqualTo(1200000L);
	}

	@DisplayName("높은 판매량순 검색")
	@Test
	void orderBySalesDescTest() {

		/* given - 데이터 준비 */
		String search = "아이";
		Sort property = Sort.by(Sort.Direction.DESC, "sales");
		Pageable pageable = PageRequest.of(0, 5, property);

		/* when - 테스트 실행 */
		List<ProductSimpleResp> productList = productRepository.findPagesWithCoveringIndex(search, pageable)
			.getContent();

		/* then - 검증 */
		assertThat(productList.get(0).getName()).isEqualTo("아이폰12");
		assertThat(productList.get(1).getName()).isEqualTo("아이폰14");
		assertThat(productList.get(2).getName()).isEqualTo("아이폰13");
	}

	@DisplayName("판매량순 페이징 조회 - 1페이지 2개")
	@Test
	void orderBySalesDescPagingTest() {

		/* given - 데이터 준비 */
		String search = "아이";
		Sort property = Sort.by(Sort.Direction.DESC, "sales");
		Pageable pageable = PageRequest.of(0, 2, property);

		/* when - 테스트 실행 */
		List<ProductSimpleResp> productList = productRepository.findPagesWithCoveringIndex(search, pageable)
			.getContent();

		/* then - 검증 */
		assertThat(productList.size()).isEqualTo(2);
		assertThat(productList.get(0).getName()).isEqualTo("아이폰12");
		assertThat(productList.get(1).getName()).isEqualTo("아이폰14");
	}

}