package com.hanbang.e.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class TimeAop {

	@Around("execution(public * com.hanbang.e.*.controller..*(..))")
	public synchronized Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		// 측정 시작 시간
		long startTime = System.currentTimeMillis();

		try {
			// 핵심기능 수행
			Object output = joinPoint.proceed();
			return output;
		} finally {
			// 측정 종료 시간
			long endTime = System.currentTimeMillis();
			// 수행시간 = 종료 시간 - 시작 시간
			long responseTime = endTime - startTime;

			log.info("[API Response Time] :" + joinPoint.getSignature().getName() + " " + responseTime + "ms");
		}
	}


}
