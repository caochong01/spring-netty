package com.autumn.annotation;

import com.autumn.annotation.Mapping;
import com.autumn.mode.RequestMethod;

import java.lang.annotation.*;

/**
 * 自定义的路由映射注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RouteMapping {

    String value();

    RequestMethod[] method() default {};

}

