package com.hanbang.e.product.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import com.hanbang.e.product.dto.ProductSimpleResp;
import com.hanbang.e.product.entity.ProductIndex;

@DataJpaTest
public class ProductIndexRepositoryTest {

	@Autowired
	private ProductIndexRepository productIndexRepository;

	@BeforeEach
	public void setup() {
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
		ProductIndex product4 = ProductIndex.builder()
			.productName("아이폰14")
			.price(1500000L)
			.img("http....")
			.stock(5)
			.sales(80)
			.onSale(true)
			.build();

		productIndexRepository.save(product1);
		productIndexRepository.save(product2);
		productIndexRepository.save(product3);
		productIndexRepository.save(product4);
	}

	@DisplayName("검색결과 높은 가격순 조회")
	@Test
	public void OrderByHighToLowPriceTest() {

		/* given - 데이터 준비 */
		String search = "아이";
		Sort property = Sort.by(Sort.Direction.DESC, "price");
		Pageable pageable = PageRequest.of(0, 5, property);

		/* when - 테스트 실행 */
		Slice<ProductSimpleResp> result = productIndexRepository.findPagesWithIndex(search, pageable);

		/* then - 검증 */
		assertThat(result.getContent().get(0).getPrice()).isEqualTo(1500000L);
		assertThat(result.getContent().get(1).getPrice()).isEqualTo(1200000L);
		assertThat(result.getContent().get(2).getPrice()).isEqualTo(1000000L);

	}

	@DisplayName("검색결과 낮은 가격순 조회")
	@Test
	public void OrderByLowToHighPriceTest() {

		/* given - 데이터 준비 */
		String search = "아이";
		Sort property = Sort.by(Sort.Direction.ASC, "price");
		Pageable pageable = PageRequest.of(0, 5, property);

		/* when - 테스트 실행 */
		Slice<ProductSimpleResp> result = productIndexRepository.findPagesWithIndex(search, pageable);

		/* then - 검증 */
		assertThat(result.getContent().get(0).getPrice()).isEqualTo(500000L);
		assertThat(result.getContent().get(1).getPrice()).isEqualTo(1000000L);
		assertThat(result.getContent().get(2).getPrice()).isEqualTo(1200000L);

	}

	@DisplayName("검색결과 판매량순 조회")
	@Test
	public void OrderByBestSellingTest() {

		/* given - 데이터 준비 */
		String search = "아이";
		Sort property = Sort.by(Sort.Direction.DESC, "sales");
		Pageable pageable = PageRequest.of(0, 5, property);

		/* when - 테스트 실행 */
		Slice<ProductSimpleResp> result = productIndexRepository.findPagesWithIndex(search, pageable);

		/* then - 검증 */
		assertThat(result.getContent().get(0).getName()).isEqualTo("아이폰12");
		assertThat(result.getContent().get(1).getName()).isEqualTo("아이폰14");
		assertThat(result.getContent().get(2).getName()).isEqualTo("아이폰13");

	}

	@DisplayName("검색결과 슬라이스 다음페이지 여부 확인")
	@Test
	public void SlicingHasNextTest() {

		/* 다음 페이지가 있는 경우 */
		/* given - 데이터 준비 */
		String search = "아이";
		Sort property = Sort.by(Sort.Direction.DESC, "sales");
		Pageable pageable1 = PageRequest.of(0, 2, property);

		/* when - 테스트 실행 */
		Slice<ProductSimpleResp> result1 = productIndexRepository.findPagesWithIndex(search, pageable1);

		/* then - 검증 */
		assertThat(result1.hasNext()).isEqualTo(true);
		assertThat(result1.getContent().get(0).getName()).isEqualTo("아이폰12");
		assertThat(result1.getContent().get(1).getName()).isEqualTo("아이폰14");

		/* 다음 페이지가 없는 경우 */
		/* given - 데이터 준비 */
		Pageable pageable2 = PageRequest.of(0, 5, property);

		/* when - 테스트 실행 */
		Slice<ProductSimpleResp> result2 = productIndexRepository.findPagesWithIndex(search, pageable2);

		/* then - 검증 */
		assertThat(result2.hasNext()).isEqualTo(false);
		assertThat(result2.getContent().get(0).getName()).isEqualTo("아이폰12");
		assertThat(result2.getContent().get(1).getName()).isEqualTo("아이폰14");
	}

}
