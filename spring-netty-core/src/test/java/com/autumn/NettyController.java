package com.autumn;

import com.autumn.mode.Mapping;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

/**
 * 提供给Netty Http方式扫描的一种注解
 * 其提供的功能与 spring @Controller 相同，但它有自己的用途（减少扫描次数）
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
@Controller
public @interface NettyController {

    @AliasFor(annotation = Controller.class)
    String value() default "";

}

