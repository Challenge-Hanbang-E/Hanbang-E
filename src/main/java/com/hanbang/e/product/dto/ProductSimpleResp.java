package com.hanbang.e.product.dto;

import com.hanbang.e.product.entity.Product;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductSimpleResp {

	private Long productId;
	private String name;
	private Long price;
	private String img;

	@Builder
	public ProductSimpleResp(Long productId, String brand, String name, Long price, String img) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.img = img;
	}

	public static ProductSimpleResp from(Product product) {
		return ProductSimpleResp.builder()
			.productId(product.getProductId())
			.name(product.getProductName())
			.price(product.getPrice())
			.img(product.getImg())
			.build();
	}
}
