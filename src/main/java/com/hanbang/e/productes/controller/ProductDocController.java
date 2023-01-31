package com.hanbang.e.productes.controller;

import java.util.Iterator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanbang.e.common.dto.PageResponseDto;
import com.hanbang.e.productes.entity.ProductDoc;
import com.hanbang.e.productes.service.ProductDocService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/es/product")
public class ProductDocController {

	private final ProductDocService productDocService;

	@GetMapping("/list")
	public ResponseEntity<?> productList(@RequestParam("search") String keyword, Pageable pageable) {
		checkPageableParam(pageable);
		Page<ProductDoc> response = productDocService.searchProductDoc(keyword, pageable);
		return new ResponseEntity<>(
			new PageResponseDto<>("success", "검색 성공", response.getContent(), response.hasNext()), HttpStatus.OK);
	}

	private void checkPageableParam(Pageable pageable) {
		Iterator<Sort.Order> orderParams = pageable.getSort().stream().iterator();

		while (orderParams.hasNext()) {
			Sort.Order orderParam = orderParams.next();
			String orderRequest = orderParam.getProperty();
			if (!(orderRequest.equalsIgnoreCase("price") || orderRequest.equalsIgnoreCase("sales"))) {
				throw new IllegalArgumentException("정렬 기준을 정확히 입력해주세요!");
			}
		}
	}
}
