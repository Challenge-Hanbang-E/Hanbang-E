package com.hanbang.e.order.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.hanbang.e.member.entity.Member;

import lombok.Getter;

@Entity
@Getter
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private Long totalPrice;

	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	private Member member;
}
