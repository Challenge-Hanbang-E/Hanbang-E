package com.hanbang.e.product.repository;

import static com.hanbang.e.product.entity.QProduct.*;
import static org.apache.logging.log4j.util.Strings.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import com.hanbang.e.product.dto.ProductSimpleResp;
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

	public Slice<ProductSimpleResp> searchFirstPage(String keyword, Pageable pageable) {

		Map<String, Object> checkOrderValue = checkOrder(pageable);

		List<ProductSimpleResp> results = queryFactory
			.select(Projections.constructor(ProductSimpleResp.class,
				product.productId,
				product.productName,
				product.price,
				product.img))
			.from(product)
			.where(keywordEq(keyword))
			.offset(0)
			.limit(pageable.getPageSize() + 1)
			.orderBy(productSort(checkOrderValue))
			.fetch();

		return checkLastPage(pageable, results);

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

	@Override
	public Slice<ProductSimpleResp> searchPageCursorFilter(String keyword, Long cursorId, Pageable pageable) {

		if (cursorId > 0L) {

			Map<String, Object> checkOrderValue = checkOrder(pageable);

			Object cursorCondition = cursorConditionEq(cursorId, checkOrderValue);

			List<ProductSimpleResp> results = queryFactory
				.select(Projections.constructor(ProductSimpleResp.class,
					product.productId,
					product.productName,
					product.price,
					product.img))
				.from(product)
				.where(cursor(cursorId, cursorCondition, checkOrderValue), keywordEq(keyword))
				.orderBy(productSort(checkOrderValue))
				.limit(pageable.getPageSize())
				.fetch();

			return checkLastPage(pageable, results);
		}
		return searchFirstPage(keyword, pageable);
	}

	private Slice<ProductSimpleResp> checkLastPage(Pageable pageable, List<ProductSimpleResp> results) {

		boolean hasNext = false;

		if (results.size() > pageable.getPageSize()) {
			hasNext = true;
			results.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(results, pageable, hasNext);
	}

	private Object cursorConditionEq(Long cursorId, Map<String, Object> checkOrderValue) {

		if (checkOrderValue.get("order").equals("sales")) {
			return queryFactory
				.select(product.sales)
				.from(product)
				.where(productIdEq(cursorId))
				.fetchOne();
		}
		return queryFactory
			.select(product.price)
			.from(product)
			.where(productIdEq(cursorId))
			.fetchOne();
	}

	private Map<String, Object> checkOrder(Pageable pageable) {

		if (!pageable.getSort().isEmpty()) {
			Map<String, Object> result = new HashMap();

			for (Sort.Order order : pageable.getSort()) {
				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
				result.put("order", order.getProperty());
				result.put("direction", direction);

				return result;
			}
		}

		return null;
	}

	private BooleanExpression cursor(Long cursorId, Object cursorCondition, Map<String, Object> checkOrderValue) {
		String order = (String)checkOrderValue.get("order");
		Order direction = (Order)checkOrderValue.get("direction");

		if (cursorId != null) {
			if (order.equals("sales")) {
				return (product.sales.eq((Integer)cursorCondition)
					.and(product.productId.lt(cursorId))
				).or(product.sales.lt((Integer)cursorCondition));
			} else if (direction.equals(Order.ASC)) {
				return (product.price.eq((Long)cursorCondition)
					.and(product.productId.gt(cursorId))
				).or(product.price.gt((Long)cursorCondition));
			} else {
				return (product.price.eq((Long)cursorCondition)
					.and(product.productId.lt(cursorId))
				).or(product.price.lt((Long)cursorCondition));
			}
		}
		return null;
	}

	private OrderSpecifier<?> productSort(Map<String, Object> checkOrderValue) {
		String order = (String)checkOrderValue.get("order");
		Order direction = (Order)checkOrderValue.get("direction");

		if (order.equals("sales") && direction.equals(Order.DESC)) {
			return new OrderSpecifier<>(direction, product.sales);
		} else if (order.equals("price")) {
			return new OrderSpecifier<>(direction, product.price);
		}
		return null;
	}

	private BooleanExpression productIdEq(Long productId) {
		return productId == null ? null : product.productId.eq(productId);
	}

	private BooleanExpression keywordEq(String keyword) {
		return isEmpty(keyword) ? null : product.productName.contains(keyword);
	}

}
