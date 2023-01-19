package com.hanbang.e.product.repository;

import static com.hanbang.e.product.entity.QProduct.*;
import static org.apache.logging.log4j.util.Strings.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.hanbang.e.product.dto.ProductSimpleResp;
import com.hanbang.e.product.entity.Product;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public ProductRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<ProductSimpleResp> searchPageFilter(String keyword, Pageable pageable) {

		List<ProductSimpleResp> results = queryFactory
			.select(Projections.constructor(ProductSimpleResp.class,
				product.productId,
				product.productName,
				product.price,
				product.img))
			.from(product)
			.where(keywordEq(keyword))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(productSort(pageable))
			.fetch();

		return results;
	}

	@Override
	public List<Product> searchProductPageFilter(String keyword, Pageable pageable) {
		List<Product> results = queryFactory
			.select(product)
			.from(product)
			.where(keywordEq(keyword))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(productSort(pageable))
			.fetch();

		return results;
	}

	private OrderSpecifier<?> productSort(Pageable pageable) {
		if (pageable.getSort().isEmpty()) {
			return null;
		}

		for (Sort.Order order : pageable.getSort()) {
			Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
			if (order.getProperty().equals("sales")) {
				return new OrderSpecifier<>(direction, product.sales);
			} else if (direction.equals(Order.DESC) && order.getProperty().equals("price")) {
				return new OrderSpecifier<>(direction, product.price);
			} else if (direction.equals(Order.ASC) && order.getProperty().equals("price")) {
				return new OrderSpecifier<>(direction, product.price);
			}
		}

		return null;
	}

	private BooleanExpression keywordEq(String keyword) {
		return isEmpty(keyword) ? null : product.productName.contains(keyword);
	}

}
