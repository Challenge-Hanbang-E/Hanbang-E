package com.hanbang.e.product.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanbang.e.common.dto.ResponseDto;
import com.hanbang.e.product.dto.ProductDetailResp;
import com.hanbang.e.product.dto.ProductListResp;
import com.hanbang.e.product.dto.ProductSimpleResp;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

	private final ProductService productService;

	@GetMapping("/list")
	public ResponseEntity<?> searchProduct(@RequestParam("search") String search,
		@RequestParam("orderby") String orderby,
		Pageable pageable) {

		var data = productService.searchProduct(search, orderby, pageable).stream()
			.map(ProductSimpleResp::from)
			.toList();
		ProductListResp response = new ProductListResp(data);

		return new ResponseEntity<>(new ResponseDto<>("success", " 성공", response), HttpStatus.OK);
	}

	@GetMapping("/details/{productId}")
	public ResponseEntity<?> getProductDetails(@PathVariable("productId") Long productId) {
		Product data = productService.getProductDetails(productId);
		ProductDetailResp response = ProductDetailResp.from(data);

		return new ResponseEntity<>(new ResponseDto<>("success", "상세 조회 성공", response), HttpStatus.OK);
	}

	@GetMapping("/list/test")
	public ResponseEntity<?> searchProductQuerydsl(@RequestParam("search") String search, Pageable pageable) {

		List<ProductSimpleResp> response = productService.searchProductQuerydsl(search, pageable);

		return new ResponseEntity<>(new ResponseDto<>("success", "검색 성공", response), HttpStatus.OK);
	}

	@GetMapping("/list/querydsl/entity")
	public ResponseEntity<?> searchProductEntityQuerydsl(@RequestParam("search") String search, Pageable pageable) {

		List<Product> response = productService.searchProductEntityQuerydsl(search, pageable);
		var data = response.stream()
			.map(ProductSimpleResp::from)
			.toList();

		return new ResponseEntity<>(new ResponseDto<>("success", "검색 성공", data), HttpStatus.OK);
	}
}
