package spring.beans.factory.support;

import spring.beans.BeanDefinition;

public class GenericBeanDefinition implements BeanDefinition {
    private String beanId;
    private String beanClassName;

    public GenericBeanDefinition(String beanId, String beanClassName) {
        this.beanId = beanId;
        this.beanClassName = beanClassName;
    }

    @Override
    public String getBeanClassName() {
        return this.beanClassName;
    }
}
