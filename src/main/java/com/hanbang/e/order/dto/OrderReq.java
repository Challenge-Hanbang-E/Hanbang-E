package com.hanbang.e.order.dto;

import javax.validation.constraints.Min;

import com.hanbang.e.member.entity.Member;
import com.hanbang.e.order.entity.Orders;
import com.hanbang.e.product.entity.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderReq {

	@Min(value = 1, message = "주문 수량을 선택해주세요.")
	private int quantity;

	public OrderReq(int quantity) {
		this.quantity = quantity;
	}

	public Orders toEntity(Member member, Product product, int quantity) {
		return Orders.builder()
			.destination(member.getAddress())
			.quantity(quantity)
			.productPrice(product.getPrice())
			.member(member)
			.product(product)
			.build();
	}

}
