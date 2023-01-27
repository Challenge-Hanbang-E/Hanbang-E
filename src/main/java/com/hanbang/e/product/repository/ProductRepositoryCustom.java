package com.hanbang.e.product.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.hanbang.e.product.dto.ProductSimpleResp;

public interface ProductRepositoryCustom {

	Slice<ProductSimpleResp> searchPageFilter (String keyword, Pageable pageable);

}
