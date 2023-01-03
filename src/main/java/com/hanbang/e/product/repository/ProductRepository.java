package com.hanbang.e.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbang.e.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
