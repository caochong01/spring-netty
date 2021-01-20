package com.autumn;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * TODO 封装一个 DefaultChannelInitializerAdapter
 * 1. 需要用户级别的配置或者选项
 * 2. 自动加载级别的配置，如：过滤器、路由器、组件支持、解析器
 */
public class DefaultChannelInitializerAdapter extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new HttpServerCodec())
                // 把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse
                .addLast(new HttpObjectAggregator(65536))
                // 压缩Http消息
                // .addLast(new HttpChunkContentCompressor())
                // 大文件支持
                .addLast(new ChunkedWriteHandler())

                // 路由适配处理
                .addLast();
    }
}
