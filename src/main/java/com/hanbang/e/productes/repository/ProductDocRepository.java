package com.hanbang.e.productes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.hanbang.e.productes.entity.ProductDoc;

public interface ProductDocRepository extends ElasticsearchRepository<ProductDoc, String> {

	Page<ProductDoc> findAllByProductName(String name,  Pageable pageable);
}
