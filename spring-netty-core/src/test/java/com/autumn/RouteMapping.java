package com.autumn;

import com.autumn.mode.RequestMethod;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

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

