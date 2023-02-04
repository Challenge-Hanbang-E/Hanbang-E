package com.hanbang.e.product.dto;

import com.hanbang.e.product.entity.Product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductDetailResp {
	private Long productId;
	private String name;
	private Long price;
	private String productimg;

	@Builder
	public ProductDetailResp(Long productId, String name, Long price, String productimg, int stock,
		int sales,
		Boolean onSale) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.productimg = productimg;
	}

	public static ProductDetailResp from(Product product) {
		return ProductDetailResp.builder()
			.productId(product.getProductId())
			.name(product.getProductName())
			.price(product.getPrice())
			.productimg(product.getImg())
			.build();
	}
}
