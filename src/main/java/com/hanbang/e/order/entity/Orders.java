package com.hanbang.e.order.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.hanbang.e.member.entity.Member;
import com.hanbang.e.product.entity.Product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false, length = 50)
	private String destination;

	@Column(nullable = false)
	private int quantity;

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

	@Builder
	public Orders(String destination, int quantity, Long productPrice, Member member,
		Product product) {
		this.destination = destination;
		this.quantity = quantity;
		this.productPrice = productPrice;
		this.totalPrice = productPrice * quantity;
		this.member = member;
		this.product = product;
	}

}
