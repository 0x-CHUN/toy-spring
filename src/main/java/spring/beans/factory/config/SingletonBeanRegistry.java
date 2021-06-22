package spring.beans.factory.config;

public interface SingletonBeanRegistry {
    void registerSingleton(String beanId, Object singletonObject);

    Object getSingletonObject(String beanId);
}
