package com.hanbang.e.order.controller;

import com.hanbang.e.common.dto.ResponseDto;

import com.hanbang.e.order.dto.OrderReq;
import com.hanbang.e.order.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping("")
	public ResponseEntity<ResponseDto> doOrder(@RequestParam Long productId, @RequestBody OrderReq orderReq) {
		Long memberId = 1L; // 임시용
		ResponseDto response = orderService.insertOrder(memberId, productId, orderReq);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}
