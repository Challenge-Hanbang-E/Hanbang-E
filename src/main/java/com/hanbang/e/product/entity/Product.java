package com.hanbang.e.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@Column(nullable = false, name = "NAME", length = 150)
	private String productName;

	@Column(nullable = false)
	private Long price;

	@Column(nullable = false, length = 200)
	private String img;

	@Column(nullable = false)
	private Integer stock;

	@Column(nullable = false)
	private Integer sales;

	@Column(nullable = false)
	private Boolean onSale;

	@Builder
	public Product(String productName, Long price, String img, int stock, int sales, Boolean onSale) {
		this.productName = productName;
		this.price = price;
		this.img = img;
		this.stock = stock;
		this.sales = sales;
		this.onSale = onSale;
	}

	public void sell(int quantity) {
		this.stock -= quantity;
		this.sales += quantity;
	}

	public void orderCancel(int quantity) {
		this.stock += quantity;
		this.sales -= quantity;
	}
}
