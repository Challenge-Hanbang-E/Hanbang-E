package com.hanbang.e.product.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanbang.e.common.dto.PageResponseDto;
import com.hanbang.e.common.dto.ResponseDto;
import com.hanbang.e.product.dto.ProductDetailResp;
import com.hanbang.e.product.dto.ProductSimpleResp;
import com.hanbang.e.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

	private final ProductService productService;

	@GetMapping("/details/{productId}")
	public ResponseEntity<?> getProductDetails(@PathVariable("productId") Long productId) {
		ProductDetailResp details = productService.getProductDetails(productId);

		return new ResponseEntity<>(new ResponseDto<>("success", "상세 조회 성공", details), HttpStatus.OK);
	}

	@GetMapping("/list")
	public ResponseEntity<?> searchProduct(@RequestParam("search") String search, Pageable pageable) {
		Slice<ProductSimpleResp> response = productService.searchProductWithIndex(search, pageable);

		return new ResponseEntity<>(new PageResponseDto<>("success", "검색 성공", response.getContent(), response.hasNext()), HttpStatus.OK);
	}

	@GetMapping("/list/ci")
	public ResponseEntity<?> productCoveringIndexList(@RequestParam("search") String search, Pageable pageable) {
		Slice<ProductSimpleResp> response = productService.searchProductWithCoveringIndex(search, pageable);
		return new ResponseEntity<>(new PageResponseDto<>("success", "검색 성공", response.getContent(), response.hasNext()), HttpStatus.OK);
	}

	@GetMapping("/list/ft")
	public ResponseEntity<?> searchProductWithFullText(@RequestParam("search") String search, Pageable pageable) {
		Slice<ProductSimpleResp> response = productService.searchProductWithFullText(search, pageable);

		return new ResponseEntity<>(new PageResponseDto<>("success", "검색 성공", response.getContent(), response.hasNext()), HttpStatus.OK);
	}
}
