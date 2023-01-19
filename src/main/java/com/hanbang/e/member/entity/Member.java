package com.hanbang.e.member.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@Column(nullable = false, length = 40)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, length = 60)
	private String address;

	@Builder
	public Member(String email, String password, String address) {
		this.email = email;
		this.password = password;
		this.address = address;
	}
}
