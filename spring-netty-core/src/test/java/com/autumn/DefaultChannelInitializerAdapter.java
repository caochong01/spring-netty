package com.autumn;

import com.autumn.mode.SupportWay;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * TODO 封装一个 DefaultChannelInitializerAdapter
 * 1. 需要用户级别的配置或者选项
 * 2. 自动加载级别的配置，如：过滤器、路由器、组件支持、解析器
 */
public class DefaultChannelInitializerAdapter extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        if ("http".equalsIgnoreCase(SupportWay.HTTP)) {
            CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin().allowNullOrigin().allowCredentials().build();
            pipeline.addLast(new HttpServerCodec())
                    .addLast(new CorsHandler(corsConfig))
                    // 把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse
                    .addLast(new HttpObjectAggregator(65536))
                    // 长连接心跳
                    // .addLast(new IdleStateHandler(3, 0, 0))

                    // 压缩Http消息
                    // .addLast(new HttpChunkContentCompressor())
                    // 大文件支持
                    .addLast(new ChunkedWriteHandler());


        }

        // 路由适配处理
        pipeline.addLast(new MappingHandleAdapter());

        // 转发处理功能
        // pipeline.addLast(new HttpServerInitializer());
    }


    static class LongTcpInitializer extends ChannelInboundHandlerAdapter {

        public static final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("连接开始了：" + ctx.toString());
//            boolean flag = group.add(ctx.channel());
//            System.out.println("用户连接状态：" + flag);
//            System.out.println("-*打开------------");
//            group.forEach(channel -> {
//                System.out.println(channel.id());
//            });
//            System.out.println("-------------*-");
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//            boolean flag = group.remove(ctx.channel());
//            System.out.println("用户关闭状态：" + flag);
//            System.out.println("-*关闭------------");
//            group.forEach(channel -> {
//                System.out.println(channel.id());
//            });
//            System.out.println("-------------*-");
            super.channelInactive(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("222222222");
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            HttpMethod method = request.method();
            System.out.println(uri + " - " + method.name() + " - " + request.decoderResult().toString());
//            if (uri.equals("/tt")) {
//                // 创建http响应
//                FullHttpResponse response = new DefaultFullHttpResponse(
//                        HttpVersion.HTTP_1_1,
//                        HttpResponseStatus.OK,
//                        Unpooled.copiedBuffer("wo is redhank", CharsetUtil.UTF_8));
//                // 设置头信息
//                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
//
//                group.writeAndFlush(response);
//                ctx.close(); // 关闭
//                group.close();
//            }
            // 创建http响应
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.copiedBuffer("连接成功！", CharsetUtil.UTF_8));
            // 设置头信息
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            ctx.write(response);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }


        private int lossConnectCount = 0;

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state() == IdleState.READER_IDLE) {
                    System.out.println("已经5秒未收到客户端的消息了！");
                    lossConnectCount++;
                    if (lossConnectCount > 2) {
                        System.out.println("关闭这个不活跃通道！");
                        ctx.channel().close();
                        //.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                    }
                }
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }


    }

    static class HttpServerInitializer extends ChannelInboundHandlerAdapter {

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            System.out.println("channelReadComplete");
            ctx.flush();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("channelRead");
            channelRead0(ctx, (FullHttpRequest) msg);
        }

        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
            //100 Continue
            // 获取请求的uri
            String uri = req.uri();
            String msg = "<html><head><title>test</title></head><body>你请求uri为：" + uri + "</body></html>";
            // 创建http响应
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
            // 设置头信息
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            //response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            // 将html write到客户端
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelRegistered");
            super.channelRegistered(ctx);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelUnregistered");
            super.channelUnregistered(ctx);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelActive");
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelInactive");
            super.channelInactive(ctx);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            System.out.println("userEventTriggered");
            super.userEventTriggered(ctx, evt);
        }

        @Override
        public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelWritabilityChanged");
            super.channelWritabilityChanged(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("exceptionCaught");
            super.exceptionCaught(ctx, cause);
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            System.out.println("handlerAdded");
            super.handlerAdded(ctx);
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            System.out.println("handlerRemoved");
            super.handlerRemoved(ctx);
        }
    }

}
