package com.hanbang.e.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbang.e.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
