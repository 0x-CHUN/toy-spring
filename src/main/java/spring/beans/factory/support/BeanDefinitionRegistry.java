package spring.beans.factory.support;

import spring.beans.BeanDefinition;

public interface BeanDefinitionRegistry {


    BeanDefinition getBeanDefinition(String beanID);

    void registerBeanDefinition(String beanID, BeanDefinition bd);
}
