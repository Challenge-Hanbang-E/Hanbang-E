package com.hanbang.e.common.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hanbang.e.common.dto.ResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity IllegalArgumentExceptionHandler(Exception e) {
		logger.error("", e);
		ResponseDto response = new ResponseDto("fail", e.getMessage(), null);

		return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity ValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
		logger.warn("MethodArgumentNotValidException 발생: url: {}", request.getRequestURI());

		String[] messageParsing = e.getMessage().split("\\[|\\]");
		String message = messageParsing[messageParsing.length - 3];

		ResponseDto response = new ResponseDto("fail", message, null);
		return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
	}
}
