package com.hanbang.e.product.dto;

import com.hanbang.e.product.entity.Product;

import lombok.Builder;

public class ProductDetails {
	private Long productId;

	private String brand;

	private String name;

	private Long price;

	private String productimg;

	private int stock;

	private int sales;

	private Boolean onSale;

	@Builder
	public ProductDetails(Long productId, String brand, String name, Long price, String productimg, int stock,
		int sales,
		Boolean onSale) {
		this.productId = productId;
		this.brand = brand;
		this.name = name;
		this.price = price;
		this.productimg = productimg;
		this.stock = stock;
		this.sales = sales;
		this.onSale = onSale;
	}

	public static ProductDetails from(Product product) {
		return ProductDetails.builder()
			.productId(product.getProductId())
			.brand(product.getBrand().getBrandName())
			.name(product.getProductName())
			.price(product.getPrice())
			.productimg(product.getImg())
			.stock(product.getStock())
			.onSale(product.getOnSale())
			.build();
	}
}
