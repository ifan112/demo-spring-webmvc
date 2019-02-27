# 引入spring-webmvc

Java Servlet Web项目引入Spring WebMVC的方式有两种，第1中基于注解，第2中基于xml配置文件。

1. 监听servlet容器，启动时创建spring容器，使用java注解的形式对spring容器进行配置，注册dispatcherServlet到servlet容器中。

   ```java
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
   ```

   ```java
   @Configuration
   @EnableWebMvc
   @ComponentScan(basePackages = "com.ifan112.demo.swmvc")
   public class DemoSpringWebMvcApplicationConfiguration {
   }
   ```

2. web.xml中注册dispatcherServlet，添加默认的spring配置文件/WEB-INF/dispatcherServlet-servlet.xml。

   web.xml

   ```xml
   <!DOCTYPE web-app PUBLIC
           "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
           "http://java.sun.com/dtd/web-app_2_3.dtd" >
   
   <web-app>
       <display-name>Archetype Created Web Application</display-name>
   
       <servlet>
           <servlet-name>dispatcherServlet</servlet-name>
           <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
           <load-on-startup>1</load-on-startup>
       </servlet>
   
       <servlet-mapping>
           <servlet-name>dispatcherServlet</servlet-name>
           <url-pattern>/</url-pattern>
       </servlet-mapping>
   </web-app>
   ```

   /WEB-INF/dispatcherServlet-servlet.xml

   ```xml
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns:mvc="http://www.springframework.org/schema/mvc"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
               http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
               http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd
               http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd">
   
       <!-- 配置spring扫描组件的路径。即让spring管理controller，service等组件类 -->
       <context:component-scan base-package="com.ifan112.demo.swmvc"/>
   
       <!-- 启用基于注解形式的webmvc功能。即让spring基于@RestController，@RequestMapping等注解分发、处理请求和响应 -->
       <mvc:annotation-driven/>
   
   </beans>
   ```



# spring web容器启动入口

不管是使用java注解配置还是xml配置文件的形式，spring web容器的启动过程都是一致的。将spring的DispatcherServlet注册到了servlet容器（tomcat）中，这就是启动spring web容器的入口。

先看下DispatcherServlet的继承结构：

```
GenericServlet
        .init()	// servlet容器初始化时会调用该方法，子类可以实现自定义逻辑。例如，初始化spring容器。
    |
 HttpServlet		---- servlet规范
    |
HttpServletBean		---- spring实现
        .init()             // 重写了GenericServlet中的方法，调用initServletBean()方法
        .initServletBean()  // 留给子类重写，实现自定义逻辑
    |
FrameworkServlet
        .initServletBean()  // 重写了HtppServletBean中的方法，用于初始化spring容器
    |
DispatcherServlet	---- 最主要的Servlet
```

servlet容器在初始化DispatcherServlet时，会调用继承自其父类GenericServlet的init()方法。spring DispatcherServlet的父类HttpServletBean重写了这个方法，并且留下了initServletBean()这个方法给子类用于实现自定义的逻辑。spring DispatcherServlet的父类FrameworkServlet重写了initServletBean()，并在该方法中初始化了spring容器。

```java
/**
 * servlet规范
 */
public abstract class GenericServlet
    implements Servlet, ServletConfig, java.io.Seralizable { 

    public void init() throws ServletException {
        // DispatcherServlet的父类HttpServletBean重写了这个方法
    }
}


public abstract class HttpServletBean extends HttpServlet implements .. {
    /**
     * spring DisptacherServlet的父类HttpServletBean重写了init()方法，
     */
    @Override
    public final void init() throws ServletException {
        // ... 其它逻辑
        // 留给子类重写的方法
        initServletBean();
    }
}


public abstract class FrameworkServlet extends HttpServletBean implements .. {
    
    /**
     * spring DispatcherServlet的父类FrameworkServlet重写了initSevletBean()方法
     */
    @Override
    protected final void initServletBean() throws ServletException {
        // ... 其它逻辑
        try {
            // 初始化spring web容器
            this.webApplicationContext = initWebApplicationContext();
            initFrameworkServlet();	// 又是留给子类重写的方法
        } catch (Exception e) {
            // ...
        }
    }
}


public class DispatcherServlet extends FrameworkServlet {
}
```

![启动入口截图](https://ws2.sinaimg.cn/large/006tKfTcgy1g0fcxmmx2gj30zh0u04ae.jpg)

spring web容器启动入口调试截图



# spring web容器启动过程分析