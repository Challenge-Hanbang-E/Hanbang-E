package com.hanbang.e.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderReq {

	private int quantity;

	public OrderReq(int quantity) {
		this.quantity = quantity;
	}

}
