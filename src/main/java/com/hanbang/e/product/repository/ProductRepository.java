package com.hanbang.e.product.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbang.e.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByProductNameContainingOrderByPriceDesc(String search, Pageable pageable);

	List<Product> findByProductNameContainingOrderByPriceAsc(String search, Pageable pageable);

	List<Product> findByProductNameContainingOrderBySalesDesc(String search, Pageable pageable);
}
