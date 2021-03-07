package com.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 作用范围：spring容器初始化
 * 处理时机：所有bean定义都被加载，但尚未实例化任何bean。
 */
@Component
public class TestBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    /**
     * 在标准初始化之后，修改应用程序上下文的内部Bean工厂。
     * 所有bean定义都将被加载，但尚未实例化任何bean。
     * 这甚至可以覆盖或添加属性，甚至可以用于初始化bean。
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        System.out.println(beanFactory.containsBean("testBeanFactoryPostProcessor")); // true
    }
}
