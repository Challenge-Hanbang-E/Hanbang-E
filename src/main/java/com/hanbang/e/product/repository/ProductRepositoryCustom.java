package com.hanbang.e.product.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.hanbang.e.product.dto.ProductSimpleResp;
import com.hanbang.e.product.entity.Product;

public interface ProductRepositoryCustom {

	List<ProductSimpleResp> searchPageFilter (String keyword, Pageable pageable);

	List<Product> searchProductPageFilter (String keyword, Pageable pageable);
}
