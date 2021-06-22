package spring.beans;

import java.util.List;

public interface BeanDefinition {
    String getBeanClassName();

    boolean isSingleton();

    boolean isPrototype();

    ScopeType getScope();

    void setScope(ScopeType scopeType);

    List<PropertyValue> getPropertyValues();

}
