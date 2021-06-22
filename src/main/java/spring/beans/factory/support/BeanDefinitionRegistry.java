package spring.beans.factory.support;

import spring.beans.BeanDefinition;

public interface BeanDefinitionRegistry {
    /**
     * Get Bean Definition
     *
     * @param beanId
     * @return BeanDefinition
     */
    BeanDefinition getBeanDefinition(String beanId);

    /**
     * Registry BeanDefinition
     *
     * @param beanId
     * @param beanDefinition
     */
    void registryBeanDefinition(String beanId, BeanDefinition beanDefinition);
}
