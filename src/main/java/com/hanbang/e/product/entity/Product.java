package com.hanbang.e.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;

@Entity
@Getter
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
	private int amount;

	@Column(nullable = false)
	private int sales;

	@Column(nullable = false)
	private Boolean onSale;

	@ManyToOne
	@JoinColumn(name = "BRAND_ID")
	private Brand brand;
}
