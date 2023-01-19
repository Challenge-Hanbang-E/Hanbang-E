package com.hanbang.e.product.service;

import static com.hanbang.e.common.exception.ExceptionMessage.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
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
	public List<Product> searchProduct(String search, String orderby, Pageable pageable) {
		List<Product> searchedProductList;
		if (orderby.equals("pricedesc")) { // 높은 가격순 조회
			searchedProductList = productRepository.findByProductNameContainingOrderByPriceDesc(search, pageable);
		} else if (orderby.equals("priceasc")) { // 낮은 가격순 조회
			searchedProductList = productRepository.findByProductNameContainingOrderByPriceAsc(search, pageable);
		} else { // 판매량 순 조회
			searchedProductList = productRepository.findByProductNameContainingOrderBySalesDesc(search, pageable);
		}

		return searchedProductList;
	}

	@Transactional(readOnly = true)
	public Product getProductDetails(Long productId) {
		Product selectedProduct = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_PRODUCT_MSG.getMsg()));

		return selectedProduct;
	}

	@Transactional
	public List<ProductSimpleResp> searchProductQuerydsl(String keyword, Pageable pageable){
		List<ProductSimpleResp> result = productRepository.searchPageFilter(keyword, pageable);
		return result;
	}

	@Transactional
	public List<Product> searchProductEntityQuerydsl(String keyword, Pageable pageable){
		List<Product> result = productRepository.searchProductPageFilter(keyword, pageable);
		return result;
	}
}
