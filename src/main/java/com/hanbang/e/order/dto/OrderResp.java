package com.hanbang.e.order.dto;

import com.hanbang.e.order.entity.Orders;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResp {

	private Long orderId;
	private int quantity;
	private Long productPrice;
	private Long totalPrice;
	private Long productId;
	private String productName;
	private String img;

	@Builder
	public OrderResp(Long orderId, int quantity, Long productPrice, Long totalPrice, Long productId, String productName,
		String img) {
		this.orderId = orderId;
		this.quantity = quantity;
		this.productPrice = productPrice;
		this.totalPrice = totalPrice;
		this.productId = productId;
		this.productName = productName;
		this.img = img;
	}

	public static OrderResp from(Orders order) {
		return OrderResp.builder()
			.orderId(order.getOrderId())
			.quantity(order.getQuantity())
			.productPrice(order.getProductPrice())
			.totalPrice(order.getTotalPrice())
			.productId(order.getProduct().getProductId())
			.productName(order.getProduct().getProductName())
			.img(order.getProduct().getImg())
			.build();
	}

}
