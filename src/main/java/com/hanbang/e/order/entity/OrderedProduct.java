package com.hanbang.e.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.hanbang.e.product.entity.Product;

import lombok.Getter;

@Entity
@Getter
public class OrderedProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderedProductId;

	@Column(nullable = false)
	private Long amount;

	@Column(nullable = false)
	private Long price;

	@ManyToOne
	@JoinColumn(name = "ORDER_ID")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
}
