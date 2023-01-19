package com.hanbang.e.product.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.hanbang.e.product.dto.ProductSimpleResp;

public interface ProductRepositoryCustom {

	List<ProductSimpleResp> searchPageFilter (String keyword, Pageable pageable);

}
