package com.hanbang.e.product.controller;

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
import com.hanbang.e.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

	private final ProductService productService;

	@GetMapping("/list")
	public ResponseEntity searchProduct(@RequestParam("search") String search, @RequestParam("orderby") String orderby,
		Pageable pageable) {

		ProductListResp response = productService.searchProduct(search, orderby, pageable);
		return new ResponseEntity(new ResponseDto("success", "상품 검색 성공", response), HttpStatus.OK);
	}

	@GetMapping("/details/{productId}")
	public ResponseEntity getProductDetails(@PathVariable("productId") Long productId) {

		ProductDetailResp response = productService.getProductDetails(productId);
		return new ResponseEntity(new ResponseDto("success", "상세 조회 성공", response), HttpStatus.OK);
	}

}
