package com.hanbang.e.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

	@Column(nullable = false, name = "NAME")
	private String productName;

	@Column(nullable = false)
	private Long price;

	@Column(nullable = false)
	private String img;

	@Column(nullable = false)
	private int stock;

	@Column(nullable = false)
	private int sales;

	@Column(nullable = false)
	private Boolean onSale;

	@ManyToOne
	@JoinColumn(name = "BRAND_ID")
	private Brand brand;

	@Builder
	public Product(String productName, Long price, String img, int stock, int sales, Boolean onSale, Brand brand) {
		this.productName = productName;
		this.price = price;
		this.img = img;
		this.stock = stock;
		this.sales = sales;
		this.onSale = onSale;
		this.brand = brand;
	}

	public void sell(int quantity) {
		this.stock -= quantity;
		this.sales += quantity;
	}
}
