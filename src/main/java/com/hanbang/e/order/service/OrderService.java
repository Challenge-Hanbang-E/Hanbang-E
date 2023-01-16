package com.hanbang.e.order.service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;
import com.hanbang.e.order.dto.OrderReq;
import com.hanbang.e.order.dto.OrderResp;
import com.hanbang.e.order.entity.Orders;
import com.hanbang.e.order.repository.OrderRepository;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;

import static com.hanbang.e.common.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;

	@Transactional
	public void insertOrder(Long memberId, Long productId, OrderReq orderReq) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_MEMBER_MSG.getMsg()));

		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_PRODUCT_MSG.getMsg()));

		if (!product.getOnSale()) {
			throw new IllegalArgumentException(NOT_SELL_PRODUCT_MSG.getMsg());
		}

		if (product.getStock() == 0) {
			throw new IllegalArgumentException(PRODUCT_OUTOF_STOCK_MSG.getMsg());
		}

		int orderQuantity = orderReq.getQuantity();

		if (product.getStock() - orderQuantity < 0) {
			throw new IllegalArgumentException(String.format(BE_IN_STOCK_CHECK_MSG.getMsg(), product.getStock()));
		}

		product.sell(orderQuantity);

		Orders order = orderReq.toEntity(member, product, orderQuantity);
		orderRepository.save(order);
	}

	@Transactional(readOnly = true)
	public List<OrderResp> findMyOrderList(Long memberId) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_MEMBER_MSG.getMsg()));

		List<Orders> orderList = orderRepository.findOrdersByMemberOrderByCreatedAtDesc(member);

		List<OrderResp> orderRespList = new ArrayList<OrderResp>();

		if (orderList.size() != 0) {
			for (Orders order : orderList) {
				OrderResp orderResponse = OrderResp.from(order);
				orderRespList.add(orderResponse);
			}
		}

		return orderRespList;
	}
  
	@Transactional
	public void deleteOrder(Long memberId, Long orderId) {

		Orders orders = orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_ORDER_MSG.getMsg()));

		if (!orders.getMember().getMemberId().equals(memberId)) {
			throw new IllegalArgumentException(NOT_HAVE_PERMISSION_DELETE_MSG.getMsg());
		}

		Product cancledProduct = orders.getProduct();
		cancledProduct.orderCancel(orders.getQuantity());
		
		orderRepository.deleteById(orderId);
	}

}
