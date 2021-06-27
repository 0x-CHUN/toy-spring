package spring.beans;

import java.util.List;

public interface BeanDefinition {
    String getBeanClassName();

    boolean isSingleton();

    boolean isPrototype();

    ScopeType getScope();

    void setScope(ScopeType scopeType);

    List<PropertyValue> getPropertyValues();

    ConstructorArgument getConstructorArgument();

    boolean hasConstructorArgumentValues();

    String getID();

    boolean hasBeanClass();

    Class<?> getBeanClass() throws IllegalStateException;

    // TODO 注意ClassLoader
    Class<?> resolveBeanClass() throws ClassNotFoundException;

}
