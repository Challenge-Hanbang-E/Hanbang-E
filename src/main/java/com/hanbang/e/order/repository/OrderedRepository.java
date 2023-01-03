package com.hanbang.e.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbang.e.order.entity.OrderedProduct;

public interface OrderedRepository extends JpaRepository<OrderedProduct, Long> {
}
