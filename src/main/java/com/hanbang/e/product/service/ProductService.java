package com.hanbang.e.product.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbang.e.common.dto.ResponseDto;
import com.hanbang.e.product.dto.ProductDetails;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional(readOnly = true)
	public ResponseDto searchProduct(String search, String orderby, Pageable pageable) {

		List<Product> result;
		if (orderby.equals("pricedesc")) { // 높은 가격순 조회
			result = productRepository.findByProductNameContainingOrderByPriceDesc(search, pageable);
		} else if (orderby.equals("priceasc")) { // 낮은 가격순 조회
			result = productRepository.findByProductNameContainingOrderByPriceAsc(search, pageable);
		} else { // 판매량 순 조회
			result = productRepository.findByProductNameContainingOrderBySalesDesc(search, pageable);
		}

		return new ResponseDto("success", "상품 검색 성공", result);
	}

	@Transactional(readOnly = true)
	public ResponseDto getProductDetails(Long productId) {

		Product selectedProduct = productRepository.findById(productId).orElseThrow(
			() -> new IllegalArgumentException("해당 상품은 존재하지 않습니다.")
		);

		ProductDetails details = ProductDetails.from(selectedProduct);
		return new ResponseDto("success", "상세 조회 성공", details);
	}
}
