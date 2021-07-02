package spring.beans.factory.support;

import spring.beans.BeanDefinition;

public interface BeanNameGenerator {
    String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry);

}