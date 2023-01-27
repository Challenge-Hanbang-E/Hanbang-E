package com.hanbang.e.common.dto;

import lombok.Getter;

@Getter
public class PageResponseDto<T> {
	private final String result;
	private final String msg;
	private final T data;

	private final boolean hasNestPage;

	public PageResponseDto(String result, String msg, T data, boolean hasNestPage){
		this.result = result;
		this.msg = msg;
		this.data = data;
		this.hasNestPage = hasNestPage;
	}
}