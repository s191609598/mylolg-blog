package com.mylog.common.config;

import com.mylog.common.filter.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;


/**
 * @author pss
 * @date 2025/3/28
 */
@Configuration
public class FilterConfig {

//    @Bean
//    public FilterRegistrationBean<XssFilter> xssFilterRegistration() {
//        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(new XssFilter());
//        registration.addUrlPatterns("/*");
//        registration.setName("xssFilter");
//        registration.setOrder(Ordered.HIGHEST_PRECEDENCE); // 最高优先级
//        return registration;
//    }
}
