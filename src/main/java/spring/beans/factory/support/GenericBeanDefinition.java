package spring.beans.factory.support;

import spring.beans.BeanDefinition;
import spring.beans.ScopeType;

public class GenericBeanDefinition implements BeanDefinition {
    private String beanId;
    private String beanClassName;

    private ScopeType scopeType = ScopeType.DEFAULT;

    public GenericBeanDefinition(String beanId, String beanClassName) {
        this.beanId = beanId;
        this.beanClassName = beanClassName;
    }

    @Override
    public String getBeanClassName() {
        return this.beanClassName;
    }

    @Override
    public boolean isSingleton() {
        return this.scopeType.equals(ScopeType.SINGLETON) || this.scopeType.equals(ScopeType.DEFAULT);
    }

    @Override
    public boolean isPrototype() {
        return this.scopeType.equals(ScopeType.PROTOTYPE);
    }

    @Override
    public ScopeType getScope() {
        return this.scopeType;
    }

    @Override
    public void setScope(ScopeType scopeType) {
        this.scopeType = scopeType;
    }
}
