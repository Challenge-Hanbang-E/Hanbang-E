package com.hanbang.e.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "product_covering_index", indexes = {
	@Index(name = "price_covering_index", columnList = "price, name, img"),
	@Index(name = "sales_covering_index", columnList = "sales, price, name, img")
})
@Entity
@Getter
@NoArgsConstructor
public class ProductCoveringIndex {

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

	@Builder
	public ProductCoveringIndex(String productName, Long price, String img, int stock, int sales, Boolean onSale) {
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
