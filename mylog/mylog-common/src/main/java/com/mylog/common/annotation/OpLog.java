package com.mylog.common.annotation;

import com.mylog.common.enums.BusinessType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 *
 * @author Pss
 * @date 2022/1/12 10:15
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpLog {
    /**
     * 功能
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 业务的名称,例如:"登录"
     */
    String title() default "";

}
