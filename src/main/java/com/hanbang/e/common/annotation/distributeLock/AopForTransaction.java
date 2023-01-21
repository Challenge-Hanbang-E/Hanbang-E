package com.hanbang.e.common.annotation.distributeLock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/*
    @Transactional은 프록시 기반으로 동작하기에 Aop내에서 트랜잭션을 별도로 가져가기 위해 클래스를 분리
    이 클래스는 @DistributeLock가 선언된 메소드의 로직을 수행
 */
@Component
public class AopForTransaction {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
