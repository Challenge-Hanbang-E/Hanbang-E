package com.hanbang.e.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbang.e.member.entity.Member;
import com.hanbang.e.order.entity.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {

	List<Orders> findOrdersByMemberOrderByCreatedAtDesc(Member member);

}
