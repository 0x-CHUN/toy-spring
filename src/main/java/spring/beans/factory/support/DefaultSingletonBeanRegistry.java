package spring.beans.factory.support;

import spring.beans.factory.config.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();


    @Override
    public void registerSingleton(String beanId, Object singletonObject) {
        if (beanId == null || beanId.trim().equals("")) {
            throw new RuntimeException("bean name null error");
        }
        Object oldObject = this.singletonObjects.get(beanId);
        if (oldObject != null) {
            throw new IllegalStateException("Could not register object [" + singletonObject +
                    "] under bean name '" + beanId + "': there is already object [" + oldObject + "] bound");
        }
        this.singletonObjects.put(beanId, singletonObject);
    }

    @Override
    public Object getSingleton(String beanId) {
        return this.singletonObjects.get(beanId);
    }
}
