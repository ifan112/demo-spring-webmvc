package com.ifan112.demo.swmvc;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class DemoSpringWebMvcApplicationInitializer implements ServletContextListener {

    /**
     * spring容器
     */
    private AnnotationConfigWebApplicationContext springContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        // 初始化一个基于java注解的Web应用类型的spring容器
        springContext = new AnnotationConfigWebApplicationContext();
        // 向spring容器注册配置类，spring将解析该类上的注解，然后对容器进行配置
        springContext.register(DemoSpringWebMvcApplicationConfiguration.class);
        // 设置spring容器运行时所在的servlet容器
        springContext.setServletContext(servletContext);

        // 注册dispatcherServlet到Servlet容器中。此后，该servlet将会接收和分发所有请求
        ServletRegistration.Dynamic dispatcherServlet
                = servletContext.addServlet("dispatcherServlet", new DispatcherServlet(springContext));
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (springContext != null) {
            // 关闭spring容器
            springContext.close();
        }
    }
}
