package com.backend.service;

import com.backend.mybatis.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IOrderService extends IService<Order> {

    boolean saveOrder(Order order);

}
