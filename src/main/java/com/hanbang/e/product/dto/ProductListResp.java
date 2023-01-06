package com.hanbang.e.product.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class ProductListResp {
	List<ProductSimpleResp> items;

	public ProductListResp(List<ProductSimpleResp> items) {
		this.items = items;
	}
}
