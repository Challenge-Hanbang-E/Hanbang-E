package com.hanbang.e.product.service;

import static com.hanbang.e.common.exception.ExceptionMessage.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbang.e.product.dto.ProductSimpleResp;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional(readOnly = true)
	public Product getProductDetails(Long productId) {
		Product selectedProduct = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_PRODUCT_MSG.getMsg()));

		return selectedProduct;
	}

	@Transactional(readOnly = true)
	public List<ProductSimpleResp> searchProduct(String keyword, Pageable pageable){
		return productRepository.searchPageFilter(keyword, pageable);
	}

	@Transactional(readOnly = true)
	public Slice<ProductSimpleResp> searchProductCursor(String search, Long cursorId, Pageable pageable) {
		return productRepository.searchPageCursorFilter(search, cursorId, pageable);
	}

}
