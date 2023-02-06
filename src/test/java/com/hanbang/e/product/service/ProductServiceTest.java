package com.hanbang.e.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hanbang.e.product.dto.ProductDetailResp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import com.hanbang.e.product.dto.ProductSimpleResp;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.entity.ProductFullTextIndex;
import com.hanbang.e.product.repository.ProductFullTextIndexRepository;
import com.hanbang.e.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ProductFullTextIndexRepository productFullTextRepository;

	@DisplayName("상품 상세 조회")
	@Test
	void getProductDetails() {
		/* given - 데이터 준비 */
		Long id = 1L;

		Product product = Product.builder()
			.productName("아이폰11")
			.price(500000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.build();
		Optional<Product> productOP = Optional.of(product);
		when(productRepository.findById(id)).thenReturn(productOP);

		/* when - 테스트 실행 */
		ProductDetailResp result = productService.getProductDetails(id);

		/* then - 검증 */
		assertThat(result.getName()).isEqualTo("아이폰11");
	}

	@DisplayName("상품 검색 - full-text index")
	@Test
	void searchProductWithFullText() {
		/* given - 데이터 준비 */
		String search = "잔스포츠 백팩";
		Sort property = Sort.by(Sort.Direction.ASC, "price");
		Pageable pageable = PageRequest.of(0, 3, property);

		/* stub - 가짜 객체 행동 정의 */
		List<ProductFullTextIndex> productList = new ArrayList<>();

		ProductFullTextIndex product1 = ProductFullTextIndex.builder()
			.productName("잔스포츠 백팩 1")
			.price(50000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.build();
		ProductFullTextIndex product2 = ProductFullTextIndex.builder()
			.productName("잔스포츠 백팩 2")
			.price(70000L)
			.img("http....")
			.stock(15)
			.sales(100)
			.onSale(true)
			.build();
		ProductFullTextIndex product3 = ProductFullTextIndex.builder()
			.productName("잔스포츠 백팩 3")
			.price(100000L)
			.img("http....")
			.stock(10)
			.sales(75)
			.onSale(true)
			.build();

		productList.add(product1);
		productList.add(product2);
		productList.add(product3);

		when(productFullTextRepository.findPagesWithFullTextIndex(search, pageable))
			.thenReturn(new SliceImpl<>(productList, pageable, false));

		/* when - 테스트 실행 */
		String queryKeyword = "+" + search.replace(" ", " +");
		Slice<ProductFullTextIndex> result = productFullTextRepository.findPagesWithFullTextIndex(search, pageable);

		List<ProductSimpleResp> data = result.getContent()
			.stream()
			.map(ProductSimpleResp::from)
			.toList();

		/* then - 검증 */
		assertThat(queryKeyword).isEqualTo("+잔스포츠 +백팩");
		assertThat(data.get(0).getName()).isEqualTo("잔스포츠 백팩 1");
		assertThat(data.get(1).getName()).isEqualTo("잔스포츠 백팩 2");
		assertThat(data.get(2).getName()).isEqualTo("잔스포츠 백팩 3");
		assertThat(result.getContent().get(0).getPrice()).isEqualTo(50000L);
		assertThat(result.getContent().get(1).getPrice()).isEqualTo(70000L);
		assertThat(result.getContent().get(2).getPrice()).isEqualTo(100000L);
		assertThat(result.hasNext()).isFalse();

	}
}
