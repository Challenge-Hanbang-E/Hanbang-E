package com.hanbang.e.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {

	// product
	NOT_EXIST_PRODUCT_MSG("해당 상품은 존재하지 않습니다.");

	private final String msg;

	ExceptionMessage(String msg) {
		this.msg = msg;
	}

}
