package com.hanbang.e.common.annotation.distributeLock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/*
- @DistributeLock 을 선언한 메소드를 호출했을때 실행되는 aop클래스
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributeLockAop {
    private static final String REDISSON_KEY_PREFIX = "RLOCK_";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.hanbang.e.common.annotation.distributeLock.DistributeLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // @DistributeLock annotation을 가져옴
        DistributeLock distributeLock = method.getAnnotation(DistributeLock.class);

        // @DistributeLock에 전달한 key를 가져오기 위해 SpringEL 표현식을 파싱
        String key = REDISSON_KEY_PREFIX + distributeLock.name() + "_" +CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributeLock.key());

        // Redisson에 해당 락의 RLock 인터페이스를 가져옴
        RLock rLock = redissonClient.getLock(key);

        try {
            // Redisson의 tryLock method를 이용해 Lock 획득을 시도 (획득 실패시 Lock이 해제 될 때까지 subscribe)
            boolean available = rLock.tryLock(distributeLock.waitTime(), distributeLock.leaseTime(), distributeLock.timeUnit());
            if (!available) {
                return false;
            }

            log.info("get lock success {}" , key);
            // @DistributeLock이 선언된 메소드의 로직 수행(별도 트랜잭션으로 분리)
            // 동시성 처리를 하기 위해서는 락을 획득 이후 트랜잭션이 시작되어야 하고 트랜잭션이 커밋되고 난 이후 락이 해제되어야 함.
            return aopForTransaction.proceed(joinPoint);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new InterruptedException(e.getMessage());
        } finally {
            // 종료 혹은 예외 발생시 finally에서 Lock을 해제
            rLock.unlock();
            log.info("release lock success {}" , key);
        }
    }
}
