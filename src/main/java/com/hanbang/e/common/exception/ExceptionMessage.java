package com.hanbang.e.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {

	//member
	OVERLAP_EMAIL_MSG("중복된 이메일이 존재합니다."),
	NOT_FOUND_MEMBER_MSG("등록된 사용자가 없습니다."),
	NOT_MATCH_PASSWORD_MSG("비밀번호가 일치하지 않습니다."),
	// product
	NOT_EXIST_PRODUCT_MSG("해당 상품은 존재하지 않습니다."),
	//order
	NOT_EXIST_MEMBER_MSG("존재하지 않는 회원입니다."),
	NOT_EXIST_ORDER_MSG("존재하지 않는 주문입니다."),
	NOT_SELL_PRODUCT_MSG("현재 판매하는 상품이 아닙니다."),
	PRODUCT_OUTOF_STOCK_MSG("현재 재고가 없는 상품입니다."),
	BE_IN_STOCK_CHECK_MSG("현재 남은 재고는 %d개 입니다."),
	NOT_HAVE_PERMISSION_DELETE_MSG("주문 삭제 권한이 없습니다."),
	ORDER_FAIL_MSG("너무 많은 요청으로 인해 상품 주문에 실패했습니다. 다시 시도해주세요.");


	private final String msg;

	ExceptionMessage(String msg) {
		this.msg = msg;
	}

}
