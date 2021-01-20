package com.autumn;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;

import java.net.InetSocketAddress;

/**
 * SpringNetty启动器
 */
public class SpringNettyApplication {

    private static final Logger log = LoggerFactory.getLogger(SpringNettyApplication.class);

    /**
     * 1. 加载application配置文件，启动spring容器，并初始化；
     * 2. 获取配置参数，使用配置参数初始化Netty的配置（EventLoopGroup、Bootstrap）；
     * 3. 获取自定义的channel及Handle等，构成完整的调度程序（Bootstrap.childHandler）；
     * 4. 启动Netty服务等；
     * 5. 加载Netty关闭钩子、spring容器关闭钩子；
     *
     * <p>
     * 需要做的：
     * 1. 自定义扫描器，实现netty路由功能，并加载到容器中；
     * 2. 自定义netty channelHandle扫描，支持路由，并加载到容器中；
     * 3. 加载application配置文件
     * 4. 主要在于handle的注入：（协议处理和动态注册器、拦截器、过滤器、路由选择器、处理类环切AOP[异常、事务]、真正处理类、Netty堆外内存销毁）
     */
    public static void run(Class<?> cls, String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        System.out.println("启动Netty服务和spring初始化容器");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                SpringNettyPropertySource.class, cls);
        context.registerShutdownHook();

        stopWatch.stop();
        System.out.println(context.getApplicationName());

        // TODO 测试
        EventLoopGroup loopGroup = context.getBean(EventLoopGroup.class);
        Class<?> socketChannelClass = (Class<?>) context.getBean("serverSocketChannel");
        Object tRoute = context.getBean("testRoute");

        System.out.println(loopGroup);
        System.out.println(socketChannelClass);
        System.out.println(tRoute);

        Environment environment = SpringStaticEnv.getENVIRONMENT();
        String yml = environment.getProperty("json.config.yml");
        System.out.println(yml);
        try {
            start(8848);
        } catch (InterruptedException e) {
            log.error("Netty 服务启动异常: " + e.getMessage());
            log.error("Netty 服务启动异常: ", e);
        }


        /*****************************启动规划********************************/

        // 读取application配置文件，并缓存

        // 启动spring容器并初始化

        // 使用配置参数初始化Netty的外部配置值

        // 读取自定义注解，并组织好逻辑（过滤、路由、json格式化、异常处理、事务等功能）

        // 将逻辑注入到Netty的channel中

        // 注册Netty关闭钩子、spring容器关闭钩子；

        // 完成。


    }

    private static void start(int port) throws InterruptedException {
        final long sTime = System.currentTimeMillis();

        // Configure the server.
        final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            bossGroup.shutdownGracefully().syncUninterruptibly();
            workerGroup.shutdownGracefully().syncUninterruptibly();
            System.out.println(bossGroup.isTerminated() + " / " + workerGroup.isTerminated());
            System.out.println(bossGroup.isShuttingDown() + " + " + workerGroup.isShuttingDown());
            System.out.println(bossGroup.isShutdown() + " * " + workerGroup.isShutdown());
        }));

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
//                .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

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
                    });

            /* 绑定到端口，sync()方法 阻塞等待直到连接完成 */
            final ChannelFuture future = bootstrap.bind().sync();
            log.info("***** Welcome To LoServer on port [{}], startting spend {}ms *****",
                    port, System.currentTimeMillis() - sTime);

            /* 阻塞，直到channel关闭 */
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }

}
