package com.backend.service.impl;

import com.backend.mybatis.entity.Order;
import com.backend.mybatis.mapper.OrderMapper;
import com.backend.redis.RedisLockUtil;
import com.backend.service.IOrderService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private static final String LOCK_KEY = "ORDER_KEY_";
    private static final int KEY_EXPIRETIME = 10;

    @Autowired
    private RedisLockUtil redisLockUtil;

    @Override
    public boolean saveOrder(Order order) {
        String requestId = IdWorker.get32UUID();
        boolean lock = redisLockUtil.getLock(LOCK_KEY, requestId, KEY_EXPIRETIME);
        boolean tryLock = redisLockUtil.tryLock(LOCK_KEY, requestId, KEY_EXPIRETIME);
        boolean tryLock1 = redisLockUtil.tryLock1(LOCK_KEY, requestId, KEY_EXPIRETIME, TimeUnit.SECONDS);
        return false;
    }


}
