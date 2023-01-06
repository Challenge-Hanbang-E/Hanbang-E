package com.hanbang.e.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.hanbang.e.product.dto.ProductDetailResp;
import com.hanbang.e.product.dto.ProductListResp;
import com.hanbang.e.product.entity.Brand;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@DisplayName("상품 검색하기, 높은 가격순 조회")
	@Test
	void searchProductOrderByHighToLow() {
		/* given - 데이터 준비 */
		String search = "아이";
		String orderby = "pricedesc";
		Pageable pageable = PageRequest.of(0, 5);

		// 가짜 객체의 행동 정의
		List<Product> productList = new ArrayList<>();
		Brand productBrand2 = new Brand("apple");
		productList.add(Product.builder()
			.productName("아이폰13")
			.price(1200000L)
			.img("http....")
			.stock(10)
			.sales(75)
			.onSale(true)
			.brand(productBrand2)
			.build());
		productList.add(Product.builder()
			.productName("아이폰12")
			.price(1000000L)
			.img("http....")
			.stock(15)
			.sales(100)
			.onSale(true)
			.brand(productBrand2)
			.build());
		productList.add(Product.builder()
			.productName("아이폰11")
			.price(500000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.brand(productBrand2)
			.build());
		when(productRepository.findByProductNameContainingOrderByPriceDesc(search, pageable)).thenReturn(productList);

		/* when - 테스트 실행 */
		ProductListResp response = productService.searchProduct(search, orderby, pageable);

		/* then - 검증 */
		assertThat(response.getItems().get(0).getName()).isEqualTo("아이폰13");
		assertThat(response.getItems().get(1).getName()).isEqualTo("아이폰12");
		assertThat(response.getItems().get(2).getName()).isEqualTo("아이폰11");

	}

	@DisplayName("상품 검색하기, 낮은 가격순 조회")
	@Test
	void searchProductOrderByLowToHigh() {
		/* given - 데이터 준비 */
		String search = "아이";
		String orderby = "priceasc";
		Pageable pageable = PageRequest.of(0, 5);

		// 가짜 객체의 행동 정의
		List<Product> productList = new ArrayList<>();
		Brand productBrand2 = new Brand("apple");
		productList.add(Product.builder()
			.productName("아이폰11")
			.price(500000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.brand(productBrand2)
			.build());
		productList.add(Product.builder()
			.productName("아이폰12")
			.price(1000000L)
			.img("http....")
			.stock(15)
			.sales(100)
			.onSale(true)
			.brand(productBrand2)
			.build());
		productList.add(Product.builder()
			.productName("아이폰13")
			.price(1200000L)
			.img("http....")
			.stock(10)
			.sales(75)
			.onSale(true)
			.brand(productBrand2)
			.build());
		when(productRepository.findByProductNameContainingOrderByPriceAsc(search, pageable)).thenReturn(productList);

		/* when - 테스트 실행 */
		ProductListResp result = productService.searchProduct(search, orderby, pageable);

		/* then - 검증 */
		assertThat(result.getItems().get(0).getName()).isEqualTo("아이폰11");
		assertThat(result.getItems().get(1).getName()).isEqualTo("아이폰12");
		assertThat(result.getItems().get(2).getName()).isEqualTo("아이폰13");
	}

	@DisplayName("상품 검색하기, 높은 가격순 조회")
	@Test
	void searchProductOrderByBestSelling() {
		/* given - 데이터 준비 */
		String search = "아이";
		String orderby = "popular";
		Pageable pageable = PageRequest.of(0, 5);

		// 가짜 객체의 행동 정의
		List<Product> productList = new ArrayList<>();
		Brand productBrand2 = new Brand("apple");
		productList.add(Product.builder()
			.productName("아이폰12")
			.price(1000000L)
			.img("http....")
			.stock(15)
			.sales(100)
			.onSale(true)
			.brand(productBrand2)
			.build());
		productList.add(Product.builder()
			.productName("아이폰13")
			.price(1200000L)
			.img("http....")
			.stock(10)
			.sales(75)
			.onSale(true)
			.brand(productBrand2)
			.build());
		productList.add(Product.builder()
			.productName("아이폰11")
			.price(500000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.brand(productBrand2)
			.build());
		when(productRepository.findByProductNameContainingOrderBySalesDesc(search, pageable)).thenReturn(productList);

		/* when - 테스트 실행 */
		ProductListResp result = productService.searchProduct(search, orderby, pageable);

		/* then - 검증 */
		assertThat(result.getItems().get(0).getName()).isEqualTo("아이폰12");
		assertThat(result.getItems().get(1).getName()).isEqualTo("아이폰13");
		assertThat(result.getItems().get(2).getName()).isEqualTo("아이폰11");
	}

	@DisplayName("상품 상세 조회")
	@Test
	void getProductDetails() {
		/* given - 데이터 준비 */
		Long id = 1L;

		Brand productBrand2 = new Brand("apple");
		Product product = Product.builder()
			.productName("아이폰11")
			.price(500000L)
			.img("http....")
			.stock(10)
			.sales(50)
			.onSale(true)
			.brand(productBrand2)
			.build();
		Optional<Product> productOP = Optional.of(product);
		when(productRepository.findById(id)).thenReturn(productOP);

		/* when - 테스트 실행 */
		ProductDetailResp result = productService.getProductDetails(id);

		/* then - 검증 */
		assertThat(result.getName()).isEqualTo("아이폰11");
	}
}