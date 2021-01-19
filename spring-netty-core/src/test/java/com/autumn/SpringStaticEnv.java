package com.autumn;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * 存储与 Application相关的资源
 */
public class SpringStaticEnv {
    // spring 的上下文信息
    private static ApplicationContext APPLICATION_CONTEXT;

    private static Environment ENVIRONMENT;

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    // 只允许本包对其设置
    protected static void setApplicationContext(ApplicationContext applicationContext) {
        APPLICATION_CONTEXT = applicationContext;
        ENVIRONMENT = APPLICATION_CONTEXT.getEnvironment();
    }

    public static Environment getENVIRONMENT() {
        return ENVIRONMENT;
    }

}
