package com.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 作用范围：针对于单个Bean实例
 * 执行时机：对象实例化完成到结束阶段，且只执行一次。
 * 可把 InitializingBean, DisposableBean当作使用Bean阶段的Spring提供的自动执行操作。
 */
@Component
public class TestInitOrDispost implements InitializingBean, DisposableBean {

    /**
     * 执行时机：Bean初始化完成后被调用，但在 BeanPostProcessor接口的postProcessBeforeInitialization方法之后执行。
     * <p>
     * 例如：执行自定义初始化，或仅检查是否已设置所有必填属性。此时属性都已注入。
     * 我们有时候必须要等某些Bean完全注入才能进行初始化操作，但使用static块等方式又不太行。
     * <p>
     * 该方法类似于 @PostConstruct注解，此方法会在该注解之后执行。
     */
    @Override
    public void afterPropertiesSet() throws Exception { // InitializingBean接口方法
        System.out.println(this.getClass().getName() + "执行对象完全实例化后一些初始化操作");
    }

    /**
     * 执行时机：Bean将要被Spring销毁的时候被调用。
     * <p>
     * 例如：执行一些关闭或清理，类似于一个对象的关闭钩子程序。
     * <p>
     * 该方法类似于 @PreConstruct注解，此方法会在该注解之后执行。
     */
    @Override
    public void destroy() throws Exception { // DisposableBean接口方法
        System.out.println(this.getClass().getName() + "被销毁了");
    }

    // 注解方式都优先执行
    @PostConstruct
    public void postConstruct() {
        System.out.println("PostConstruct执行");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("PreDestroy执行");
    }
}
