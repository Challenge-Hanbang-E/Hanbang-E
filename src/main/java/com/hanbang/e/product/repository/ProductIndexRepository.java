package com.hanbang.e.product.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanbang.e.product.dto.ProductSimpleResp;
import com.hanbang.e.product.entity.ProductIndex;

public interface ProductIndexRepository extends JpaRepository<ProductIndex, Long> {

	@Query(value =
		"SELECT new com.hanbang.e.product.dto.ProductSimpleResp(pi.productId, pi.productName, pi.price, pi.img) "
			+ "FROM ProductIndex pi "
			+ "WHERE pi.productName LIKE %:keyword% ")
	Slice<ProductSimpleResp> findPagesWithIndex(@Param(value = "keyword") String keyword, Pageable pageable);

}
