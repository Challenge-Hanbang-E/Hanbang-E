package com.hanbang.e.productes.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hanbang.e.productes.entity.ProductDoc;
import com.hanbang.e.productes.repository.ProductDocRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductDocService {

	private final ProductDocRepository productDocRepository;

	public Page<ProductDoc> searchProductDoc(String keyword, Pageable pageable) {
		return productDocRepository.findAllByProductName(keyword, pageable);
	}

}
