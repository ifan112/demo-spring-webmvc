package com.ifan112.demo.swmvc;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * spring context事件监听器
 */

@Component
public class DemoSpringContextListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("接收事件 " + event);
    }
}
