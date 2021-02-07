package com.autumn;

import com.autumn.annotation.PathVariable;
import com.autumn.mode.RequestMethod;
import com.autumn.router.Routed;
import com.autumn.router.RouterManager;
import com.autumn.router.Routing;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;

public class MappingHandleAdapter extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

        /**
         * 1. 设置请求的前置拦截器、过滤器（可扩展）入口过滤
         * 2. 从Mapping中获取Methods，并检查注解要求及方法参数的配置
         * 3. 反射执行方法并返回响应值
         * 4. 响应解释器（主要对响应内容转换）
         * 5. 执行后置拦截器、过滤器（可扩展）出口过滤
         */
        HttpMappingHandler.beforeHandel(ctx, request);
        Routed<Routing> routed = HttpMappingHandler.checkMethods(ctx, request);// 理论上需要返回一个封装，供下一步使用
        if (routed == null || routed.notFound()) {
            HttpMappingHandler.afterHandel(ctx, request, "请求找不到");
            return;
        }
        HttpMappingHandler.doAction(ctx, request);
    }

    static class HttpMappingHandler {

        public static void beforeHandel(ChannelHandlerContext ctx, FullHttpRequest request) {

        }

        public static void doAction(ChannelHandlerContext ctx, FullHttpRequest request) {
            HttpMappingHandler.afterHandel(ctx, request, "请求完成");
        }

        public static Routed<Routing> checkMethods(
                ChannelHandlerContext ctx,
                FullHttpRequest request)
                throws InvocationTargetException,
                IllegalAccessException, NoSuchMethodException {
            /**
             * 1. 检查方法注解的请求类型（GET、POST等）
             * 2. 检查方法的参数注解等 TODO 暂时先不做
             */
            String uri = request.uri();
            HttpMethod method = request.method();
            HttpHeaders headers = request.headers();
            HttpVersion httpVersion = request.protocolVersion();
            System.out.println(uri + " " + method + " " + httpVersion);
            System.out.println(headers);
            RouterManager routerManager = RouterManager.manager();
            Routed<Routing> routed = routerManager.route(RequestMethod.parseOf(method.name()), uri);
            System.out.println(routed);

            if (routed != null && routed.target() != null) {
                Method r_method = routed.target().getMethod();

                Class<?> returnType = r_method.getReturnType();
                System.out.println("返回类型: " + returnType);
                Class<?>[] exceptionTypes = r_method.getExceptionTypes();
                for (Type exceptionType : exceptionTypes) {
                    System.out.println("异常类型: " + exceptionType);
                }

                /**
                 * 1. 获取执行方法、具体实例化对象、参数类型、结果类型等；
                 * 2. 根据参数类型，填充默认参数类型（request、response）和映射类型，映射类型找不到则默认为Null；
                 * 3. 执行invoke方法，完成返回
                 */

                Class<?>[] parameterTypes = r_method.getParameterTypes();
                Annotation[][] parameterAnnotations = routed.target().getParameterAnnotations();
                Object[] parameter = new Object[parameterTypes.length];

                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    Annotation[] parameterAnnotation = parameterAnnotations[i];

                    // TODO 此处以后应该采用类似于适配器模式的方式，
                    // TODO 对不同的类型适配不同的解析，此模块应该单列出来。

                    // 默认填充初始化及判断
                    if (HttpRequest.class.isAssignableFrom(parameterType)) {
                        System.out.println("HttpRequest.class.isAssignableFrom == " + true);
                        parameter[i] = request;
                    } else if (HttpResponse.class.isAssignableFrom(parameterType)) {
                        System.out.println("HttpResponse.class.isAssignableFrom == " + true);
                        parameter[i] = new DefaultFullHttpResponse(
                                HttpVersion.HTTP_1_1,
                                HttpResponseStatus.OK);
                    } else { // TODO 参数类型解析
                        // 入口参数映射
                        parameter[i] = null; // 默认为空

                        for (Annotation p_annotation : parameterAnnotation) {
                            // TODO 形参注解解析，此处也可以适配扩展
                            if (PathVariable.class.equals(p_annotation.annotationType())) { // 对PathVariable注解的解析
                                Method required = p_annotation.annotationType().getDeclaredMethod("value");
                                Object rPathVal = required.invoke(p_annotation);
                                // TODO url入参类型和形参类型的校验， 可以暂时不做
                                parameter[i] = routed.params().get(rPathVal.toString());
                            }
                        }
                    }
                }

                Object invoke = r_method.invoke(routed.target().getClassBean(), parameter);
                System.out.println("执行结果: " + invoke);

            }

            return routed;

//            RouteNode routeNode = RouterManager.routeMap().add(uri);
//            if (routeNode == null) {
//                ctx.writeAndFlush(null).addListener(ChannelFutureListener.CLOSE);
//                return null;
//            }
//            System.out.println(routeNode.toString());
//
//            RouteMapping routeAttr = routeNode.getRoute();
//
//            RequestMethod[] requestMethods = routeAttr.method();
//            if (requestMethods.length > 0) {
//
//            }
//
//            while (!routeNode.isNullChildNode() && routeNode.isClass()) {
//                routeAttr.value();
//            }
        }

        public static void afterHandel(ChannelHandlerContext ctx, FullHttpRequest request, CharSequence string) {
            // 创建http响应
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.copiedBuffer(string, CharsetUtil.UTF_8));
            // 设置头信息
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            //response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            // 将html write到客户端
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

}
