package com.hanbang.e.product.dto;

import com.hanbang.e.product.entity.Product;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductSimple {

	private Long productId;

	private String brand;

	private String name;

	private Long price;

	private String productimg;

	@Builder
	public ProductSimple(Long productId, String brand, String name, Long price, String productimg) {
		this.productId = productId;
		this.brand = brand;
		this.name = name;
		this.price = price;
		this.productimg = productimg;
	}

	public static ProductSimple from(Product product) {
		return ProductSimple.builder()
			.productId(product.getProductId())
			.brand(product.getBrand().getBrandName())
			.name(product.getProductName())
			.price(product.getPrice())
			.productimg(product.getImg())
			.build();
	}
}
