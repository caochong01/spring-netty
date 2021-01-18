package com.autumn;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
        return new NioEventLoopGroup();
    }

    @Bean("serverSocketChannel")
    public Class<?> serverSocketChannel(EventLoopGroup eventLoopGroup) {
        if (eventLoopGroup instanceof NioEventLoopGroup) {
            return NioServerSocketChannel.class;
        } else if (eventLoopGroup != null) {
            return ServerSocketChannel.class;
        }
        return NioServerSocketChannel.class;
    }

}
