package com.hanbang.e.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.hanbang.e.common.dto.ResponseDto;
import com.hanbang.e.common.jwt.JwtUtil;
import com.hanbang.e.order.dto.OrderReq;
import com.hanbang.e.order.dto.OrderResp;
import com.hanbang.e.order.facade.OrderRedissonLockFacade;
import com.hanbang.e.order.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
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

	private final JwtUtil jwtUtil;

	@PostMapping("")
	public ResponseEntity<ResponseDto<?>> doOrder(@RequestParam Long productId, @Valid @RequestBody OrderReq orderReq, HttpServletRequest request) {
		Long memberId = jwtUtil.getMemberIdFromToken(request);
		orderService.insertOrder(memberId, productId, orderReq);
		ResponseDto<?> response = new ResponseDto<>("success", "주문 성공", null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/list")
	public ResponseEntity<ResponseDto<List<OrderResp>>> getMyOrderList(HttpServletRequest request) {
		Long memberId = jwtUtil.getMemberIdFromToken(request);
		List<OrderResp> orderRespList = orderService.findMyOrderList(memberId);
		ResponseDto response = new ResponseDto<>("success", "주문 조회 성공", orderRespList);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("")
	public ResponseEntity<?> deleteOrder(@RequestParam Long orderId, HttpServletRequest request) {
		Long memberId = jwtUtil.getMemberIdFromToken(request);
		orderService.deleteOrder(memberId, orderId);
		return new ResponseEntity<>(new ResponseDto("success", "주문 삭제 성공", null), HttpStatus.OK);
	}

}
