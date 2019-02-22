package com.ifan112.demo.swmvc.api;

import com.ifan112.demo.swmvc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/a")
    public String a(@RequestParam String username,
                    @RequestParam long goodId) {
        orderService.newOrder(username, goodId);

        return "test-a";
    }

}
