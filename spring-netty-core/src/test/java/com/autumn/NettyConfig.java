package com.autumn;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.nio.channels.ServerSocketChannel;

@ComponentScan
@Configuration
@PropertySource(value="classpath:application.yml",factory=YamlPropertySourceFactory.class)
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
