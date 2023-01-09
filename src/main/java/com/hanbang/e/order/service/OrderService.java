package com.hanbang.e.order.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbang.e.common.dto.ResponseDto;
import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;
import com.hanbang.e.order.dto.OrderReq;
import com.hanbang.e.order.entity.Orders;
import com.hanbang.e.order.repository.OrderRepository;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;

	@Transactional
	public ResponseDto<?> insertOrder(Long memberId, Long productId, OrderReq orderReq) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

		if (!product.getOnSale()) {
			throw new IllegalArgumentException("현재 판매하는 상품이 아닙니다.");
		}

		if (product.getStock() == 0) {
			throw new IllegalArgumentException("현재 재고가 없는 상품입니다.");
		}

		int orderQuantity = orderReq.getQuantity();

		if (product.getStock() - orderQuantity < 0) {
			throw new IllegalArgumentException(String.format("현재 남은 재고는 %d개 입니다.", product.getStock()));
		}

		product.sell(orderQuantity);

		Orders order = orderReq.toEntity(member, product, orderQuantity);
		orderRepository.save(order);

		return new ResponseDto<>("success", "주문 성공", null);
	}

}
