package com.hanbang.e.order.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.hanbang.e.member.entity.Member;
import com.hanbang.e.product.entity.Product;

import lombok.Getter;

@Entity
@Getter
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false, length = 50)
	private String destination;

	@Column(nullable = false)
	private int amount;

	@Column(nullable = false)
	private Long productPrice;

	@Column(nullable = false)
	private Long totalPrice;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;

}
