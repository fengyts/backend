package com.backend.controller;

import com.backend.mybatis.entity.Order;
import com.backend.service.IOrderService;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order/")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("queryPageList")
    public R queryPageList(){
        R<Object> ok = R.ok(null);
        return ok;
    }

    @PostMapping("save")
    public R save(@RequestBody Order order){
        orderService.saveOrder(order);
        R<Object> ok = R.ok(null);
        return ok;
    }

}
