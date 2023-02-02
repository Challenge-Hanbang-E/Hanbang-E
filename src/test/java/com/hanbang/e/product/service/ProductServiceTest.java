package com.hanbang.e.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.hanbang.e.product.dto.ProductDetailResp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

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
}
