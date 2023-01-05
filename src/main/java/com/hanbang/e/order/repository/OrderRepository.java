package com.hanbang.e.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbang.e.order.entity.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
