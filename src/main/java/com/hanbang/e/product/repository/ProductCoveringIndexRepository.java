package com.hanbang.e.product.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanbang.e.product.dto.ProductSimpleResp;
import com.hanbang.e.product.entity.ProductCoveringIndex;

public interface ProductCoveringIndexRepository extends JpaRepository<ProductCoveringIndex, Long> {

	@Query(value =
		"SELECT new com.hanbang.e.product.dto.ProductSimpleResp(pci.productId, pci.productName, pci.price, pci.img) "
			+ "FROM ProductCoveringIndex pci "
			+ "WHERE pci.productName LIKE %:keyword% ")
	List<ProductSimpleResp> findPagesWithCoveringIndex(@Param(value = "keyword") String keyword, Pageable pageable);

}
