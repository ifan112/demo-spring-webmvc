package com.ifan112.demo.swmvc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.ifan112.demo.swmvc")
public class DemoSpringWebMvcApplicationConfiguration {
}
