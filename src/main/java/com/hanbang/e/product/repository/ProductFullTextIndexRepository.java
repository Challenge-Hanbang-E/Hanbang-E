package com.hanbang.e.product.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanbang.e.product.entity.ProductFullTextIndex;

public interface ProductFullTextIndexRepository extends JpaRepository<ProductFullTextIndex, Long> {

	// full-text index 적용 쿼리
	@Query(value =
		"SELECT * FROM product_ngram WHERE MATCH(name) AGAINST(:keyword IN BOOLEAN MODE)", nativeQuery = true)
	Slice<ProductFullTextIndex> findPagesWithFullTextIndex(@Param(value = "keyword") String keyword, Pageable pageable);
}
