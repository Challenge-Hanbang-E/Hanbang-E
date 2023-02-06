package com.hanbang.e.common.dto;

import lombok.Getter;

@Getter
public class PageResponseDto<T> {
	private final String result;
	private final String msg;
	private final T data;

	private final boolean hasNextPage;

	public PageResponseDto(String result, String msg, T data, boolean hasNextPage){
		this.result = result;
		this.msg = msg;
		this.data = data;
		this.hasNextPage = hasNextPage;
	}
}
