package com.hanbang.e.order.facade;

import com.hanbang.e.order.dto.OrderReq;
import com.hanbang.e.order.service.OrderService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class OrderRedissonLockFacade {

    private final RedissonClient redissonClient;

    private final OrderService orderService;

    public OrderRedissonLockFacade(RedissonClient redissonClient, OrderService orderService) {
        this.redissonClient = redissonClient;
        this.orderService = orderService;
    }

    public void insertOrder(Long key, Long memberId, Long productId, OrderReq orderReq){
        String inserKey = String.valueOf(1+key);
        RLock lock = redissonClient.getLock(inserKey);

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if(!available) {
                return;
            }
            orderService.insertOrder(memberId, productId, orderReq);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void deleteOrder(Long key, Long memberId, Long orderId){
        String deleteKey = String.valueOf(0+key);
        RLock lock = redissonClient.getLock(deleteKey);

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if(!available) {
                return;
            }
            orderService.deleteOrder(memberId, orderId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

    }

}


