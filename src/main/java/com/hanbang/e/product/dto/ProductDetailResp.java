package com.hanbang.e.product.dto;

import com.hanbang.e.product.entity.Product;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductDetailResp {
	private Long productId;
	private String brand;
	private String name;
	private Long price;
	private String productimg;
	private int stock;
	private int sales;
	private Boolean onSale;

	@Builder
	public ProductDetailResp(Long productId, String name, Long price, String productimg, int stock,
		int sales,
		Boolean onSale) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.productimg = productimg;
		this.stock = stock;
		this.sales = sales;
		this.onSale = onSale;
	}

	public static ProductDetailResp from(Product product) {
		return ProductDetailResp.builder()
			.productId(product.getProductId())
			.name(product.getProductName())
			.price(product.getPrice())
			.productimg(product.getImg())
			.stock(product.getStock())
			.onSale(product.getOnSale())
			.build();
	}
}
