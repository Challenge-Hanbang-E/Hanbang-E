package com.hanbang.e.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
