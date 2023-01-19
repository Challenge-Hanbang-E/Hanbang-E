package com.hanbang.e.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hanbang.e.product.dto.ProductSimpleResp;

public interface ProductRepositoryCustom {

	Page<ProductSimpleResp> searchPageFilter (String keyword, Pageable pageable);
}
