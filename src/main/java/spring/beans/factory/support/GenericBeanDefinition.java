package spring.beans.factory.support;

import spring.beans.BeanDefinition;
import spring.beans.ConstructorArgument;
import spring.beans.PropertyValue;
import spring.beans.ScopeType;

import java.util.ArrayList;
import java.util.List;

public class GenericBeanDefinition implements BeanDefinition {
    private String beanId;
    private String beanClassName;
    private Class<?> beanClass;

    private ScopeType scopeType = ScopeType.DEFAULT;
    private List<PropertyValue> propertyValues = new ArrayList<>();
    private ConstructorArgument constructorArgument = new ConstructorArgument();

    public GenericBeanDefinition(String beanId, String beanClassName) {
        this.beanId = beanId;
        this.beanClassName = beanClassName;
    }

    public GenericBeanDefinition() {
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

    @Override
    public List<PropertyValue> getPropertyValues() {
        return this.propertyValues;
    }

    @Override
    public ConstructorArgument getConstructorArgument() {
        return this.constructorArgument;
    }

    @Override
    public boolean hasConstructorArgumentValues() {
        return !this.constructorArgument.isEmpty();
    }

    @Override
    public String getID() {
        return this.beanId;
    }

    @Override
    public boolean hasBeanClass() {
        return this.beanClass != null;
    }

    @Override
    public Class<?> getBeanClass() throws IllegalStateException {
        if (this.beanClass == null) {
            throw new IllegalStateException(
                    "Bean class name [" + this.getBeanClassName() + "] has not been resolved into an actual Class");
        }
        return this.beanClass;
    }

    @Override
    public Class<?> resolveBeanClass() throws ClassNotFoundException {
        String className = getBeanClassName();
        if (className == null)
            return null;
        Class<?> resolvedClass = Thread.currentThread().getContextClassLoader().loadClass(className);
        this.beanClass = resolvedClass;
        return resolvedClass;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }
}
