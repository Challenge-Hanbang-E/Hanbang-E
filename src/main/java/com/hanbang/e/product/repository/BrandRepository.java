package com.hanbang.e.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanbang.e.product.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
