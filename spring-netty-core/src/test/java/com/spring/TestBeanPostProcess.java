package com.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * BeanPostProcessor接口默认实现了postProcessBeforeInitialization和postProcessAfterInitialization两个方法，
 * 作用范围：任何Bean实例化对象
 * 方法执行时机：参照各方法注释
 */
@Component
public class TestBeanPostProcess implements BeanPostProcessor {

    /**
     * Apply this {@code BeanPostProcessor} to the given new bean instance <i>before</i> any bean
     * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
     * or a custom init-method).
     *
     * 执行时机：InitializingBean's afterPropertiesSet方法或者init-method之前执行。
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor-Before: " + beanName);
        return bean;
    }

    /**
     * Apply this {@code BeanPostProcessor} to the given new bean instance <i>after</i> any bean
     * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
     * or a custom init-method).
     * <p>In case of a FactoryBean, this callback will be invoked for both the FactoryBean
     * instance and the objects created by the FactoryBean (as of Spring 2.0).
     *
     * 执行时机：InitializingBean's afterPropertiesSet方法或者init-method之后。
     *
     * 注意：Spring2.0后，FactoryBean也要走这个方法。
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor-After: " + beanName);
        return bean;
    }
}
