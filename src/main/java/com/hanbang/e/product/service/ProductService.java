package com.hanbang.e.product.service;

import static com.hanbang.e.common.exception.ExceptionMessage.*;

import java.util.List;

import com.hanbang.e.product.dto.ProductDetailResp;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbang.e.product.dto.ProductSimpleResp;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.entity.ProductFullTextIndex;
import com.hanbang.e.product.repository.ProductCoveringIndexRepository;
import com.hanbang.e.product.repository.ProductFullTextIndexRepository;
import com.hanbang.e.product.repository.ProductIndexRepository;
import com.hanbang.e.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductCoveringIndexRepository productCoveringIndexRepository;
	private final ProductIndexRepository productIndexRepository;
	private final ProductFullTextIndexRepository productFullTextIndexRepository;

	@Transactional(readOnly = true)
	@Cacheable(value = "ProductDetailResp", key = "#productId", cacheManager = "redisCacheManager")
	public ProductDetailResp getProductDetails(Long productId) {
		Product selectedProduct = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_PRODUCT_MSG.getMsg()));

		ProductDetailResp details = ProductDetailResp.from(selectedProduct);
		return details;
	}

	@Transactional(readOnly = true)
	public Slice<ProductSimpleResp> searchProductWithNoIndex(String keyword, Pageable pageable) {
		return productRepository.findPagesWithNoIndex(keyword, pageable);
	}

	@Transactional(readOnly = true)
	public Slice<ProductSimpleResp> searchProductWithIndex(String keyword, Pageable pageable) {
		return productIndexRepository.findPagesWithIndex(keyword, pageable);
	}

	@Transactional(readOnly = true)
	public Slice<ProductSimpleResp> searchProductWithCoveringIndex(String keyword, Pageable pageable) {
		return productCoveringIndexRepository.findPagesWithCoveringIndex(keyword, pageable);
	}

	@Transactional(readOnly = true)
	public Slice<ProductSimpleResp> searchProductWithFullText(String keyword, Pageable pageable) {
		String queryKeyword = "+" + keyword.replace(" ", " +");
		Slice<ProductFullTextIndex> result = productFullTextIndexRepository.findPagesWithFullTextIndex(queryKeyword, pageable);

		List<ProductSimpleResp> data = result.getContent()
			.stream()
			.map(ProductSimpleResp::from)
			.toList();

		return new SliceImpl<>(data, pageable, result.hasNext());
	}

}
