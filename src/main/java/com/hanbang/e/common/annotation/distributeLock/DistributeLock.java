package com.hanbang.e.common.annotation.distributeLock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/*
    커스텀 어노테이션 @DistributeLock을 만들어준다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributeLock {
    String key(); // 락의 이름

    String name();
    TimeUnit timeUnit() default TimeUnit.SECONDS; // 시간 단위 설정(초)

    long waitTime() default 5L; // 락을 획득하기 위한 대기 시간

    long leaseTime() default 3L; // 락의 타임아웃 설장

}
