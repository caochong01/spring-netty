package com.example;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.channels.ServerSocketChannel;

/**
 * Netty Bean配置示例 Test（用户级别配置）
 */
@Configuration
public class NettyConfig {

    @Bean
    public EventLoopGroup eventLoopGroup() {
        return new NioEventLoopGroup(1);
    }

}
