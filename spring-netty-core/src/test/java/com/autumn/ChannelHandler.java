package com.autumn;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 出入的Netty通道处理注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ChannelHandler {
}
