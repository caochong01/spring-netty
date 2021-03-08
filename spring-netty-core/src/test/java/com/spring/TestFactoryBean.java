package com.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 定义一个FactoryBean来获取具体实例化对象，需要实现FactoryBean接口。
 *
 * 同时实现DisposableBean是因为容器只负责FactoryBean的生命周期，
 * 但不负责通过FactoryBean创建的对象的生命周期，所以需要你进行管理。
 *
 * 比如：
 * 1. 你想让spring从工厂方法中获取具体对象
 * 2. 对bean再做一层工厂封装，屏蔽具体实现类细节
 * 3. 你想屏蔽具体对象的创建细节，又想让spring管理Bean
 */
@Component
public class TestFactoryBean implements FactoryBean<TestFactoryBean.TBn>, DisposableBean {

    private static TBn tBn = new TBnImpl();

    // Bean初始化时，会调用这个方法获取将要实例化对象
    @Override
    public TBn getObject() throws Exception {
        System.out.println("Bean 实例化了 TBn");
        return tBn;
    }

    @Override
    public Class<?> getObjectType() {
        return TBn.class;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("销毁TBn");
        tBn = null;
    }

    // @Autowried注入时要注入TBn这个接口，而不是TestFactoryBean
    static class TBnImpl implements TBn {
        public String d;
        public String f;
    }
    public interface TBn {
    }
}
