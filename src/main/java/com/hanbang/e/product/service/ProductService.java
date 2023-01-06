package com.hanbang.e.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbang.e.common.dto.ResponseDto;
import com.hanbang.e.product.dto.ProductDetails;
import com.hanbang.e.product.dto.ProductList;
import com.hanbang.e.product.dto.ProductSimple;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional(readOnly = true)
	public ProductList searchProduct(String search, String orderby, Pageable pageable) {

		List<Product> searchedProductList;
		if (orderby.equals("pricedesc")) { // 높은 가격순 조회
			searchedProductList = productRepository.findByProductNameContainingOrderByPriceDesc(search, pageable);
		} else if (orderby.equals("priceasc")) { // 낮은 가격순 조회
			searchedProductList = productRepository.findByProductNameContainingOrderByPriceAsc(search, pageable);
		} else { // 판매량 순 조회
			searchedProductList = productRepository.findByProductNameContainingOrderBySalesDesc(search, pageable);
		}

		List<ProductSimple> result = new ArrayList<>();
		for (Product product : searchedProductList) {
			result.add(ProductSimple.from(product));
		}

		return new ProductList(result);
	}

	@Transactional(readOnly = true)
	public ProductDetails getProductDetails(Long productId) {

		Product selectedProduct = productRepository.findById(productId).orElseThrow(
			() -> new IllegalArgumentException("해당 상품은 존재하지 않습니다.")
		);

		return ProductDetails.from(selectedProduct);
	}
}
