package com.ifan112.demo.swmvc.service.impl;

import com.ifan112.demo.swmvc.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public boolean newOrder(String username, long goodId) {
        System.out.println("---------- 创建新订单 ----------");
        System.out.println(username + " | " + goodId);
        System.out.println("-------------------------------");

        return true;
    }
}
