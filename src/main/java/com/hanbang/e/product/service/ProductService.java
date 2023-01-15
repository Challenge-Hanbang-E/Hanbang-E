package com.hanbang.e.product.service;

import static com.hanbang.e.common.exception.ExceptionMessage.NOT_EXIST_PRODUCT_MSG;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanbang.e.product.dto.ProductDetailResp;
import com.hanbang.e.product.dto.ProductListResp;
import com.hanbang.e.product.dto.ProductSimpleResp;
import com.hanbang.e.product.entity.Product;
import com.hanbang.e.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional(readOnly = true)
	public ProductListResp searchProduct(String search, String orderby, Pageable pageable) {

		List<Product> searchedProductList;
		if (orderby.equals("pricedesc")) { // 높은 가격순 조회
			searchedProductList = productRepository.findByProductNameContainingOrderByPriceDesc(search, pageable);
		} else if (orderby.equals("priceasc")) { // 낮은 가격순 조회
			searchedProductList = productRepository.findByProductNameContainingOrderByPriceAsc(search, pageable);
		} else { // 판매량 순 조회
			searchedProductList = productRepository.findByProductNameContainingOrderBySalesDesc(search, pageable);
		}

		List<ProductSimpleResp> result = new ArrayList<>();
		for (Product product : searchedProductList) {
			result.add(ProductSimpleResp.from(product));
		}

		return new ProductListResp(result);
	}

	@Transactional(readOnly = true)
	public ProductDetailResp getProductDetails(Long productId) {

		Product selectedProduct = productRepository.findById(productId).orElseThrow(
			() -> new IllegalArgumentException(NOT_EXIST_PRODUCT_MSG.getMsg())
		);

		return ProductDetailResp.from(selectedProduct);
	}
}
