package com.hanbang.e.product.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanbang.e.product.dto.ProductSimpleResp;
import com.hanbang.e.product.entity.Product;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.productId=:id")
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="5000")}) // TimeOut 5초로 설정
    Product findByIdWithPessimisticLock(@Param("id") Long id);
    
    @Query(value =
		    "SELECT new com.hanbang.e.product.dto.ProductSimpleResp(pi.productId, pi.productName, pi.price, pi.img) "
			    + "FROM Product pi "
			    + "WHERE pi.productName LIKE %:keyword% ")
	  Slice<ProductSimpleResp> findPagesWithNoIndex(@Param(value = "keyword") String keyword, Pageable pageable);

}
