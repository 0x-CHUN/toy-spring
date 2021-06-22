package spring.beans;

public interface BeanDefinition {
    String getBeanClassName();

    boolean isSingleton();

    boolean isPrototype();

    ScopeType getScope();

    void setScope(ScopeType scopeType);

}
