package com.hanbang.e.product.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class ProductList {
	List<ProductSimple> items;

	public ProductList(List<ProductSimple> items) {
		this.items = items;
	}
}
