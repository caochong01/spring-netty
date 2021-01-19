package com.autumn;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;

@Configuration
public class RouteImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private final String DEFAULT_RESOURACE_PATTERN = "**/*.class";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        System.out.println("------------------ooo");
        importingClassMetadata.getAnnotationTypes().forEach(System.out::println);
        Arrays.stream(registry.getBeanDefinitionNames()).forEach(System.out::println);


        /**
         * 拿到注解信息
         */
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(RouteMapping.class.getName()));

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        // bean 的名字生成规则在AnnotationBeanNameGenerator
        scanner.setBeanNameGenerator(new AnnotationBeanNameGenerator());
        // 设置哪些注解的扫描
        scanner.addIncludeFilter(new AnnotationTypeFilter(RouteMapping.class));
        scanner.scan("**/*.class");
    }
}
