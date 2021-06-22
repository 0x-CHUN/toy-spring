package spring.beans.factory.support;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import spring.beans.BeanDefinition;
import spring.beans.factory.BeanFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {
    private static final Map<String, BeanDefinition> BEAN_MAP = new ConcurrentHashMap<>();

    public DefaultBeanFactory() {
    }

    @Override
    public Object getBean(String beanId) {
        // todo: check bean exist
        // todo: solve exception
        // todo: constructor

        BeanDefinition definition = BEAN_MAP.get(beanId);
        if (definition.isSingleton()) {
            Object bean = this.getSingletonObject(beanId);
            if (bean == null) {
                bean = createBean(definition);
                this.registerSingleton(beanId, bean);
            }
            return bean;
        }
        return createBean(definition);
    }

    private Object createBean(BeanDefinition definition) {
        Class target = null;
        try {
            target = Thread.currentThread().getContextClassLoader().loadClass(definition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            return target.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanId) {
        return BEAN_MAP.get(beanId);
    }

    @Override
    public void registryBeanDefinition(String beanId, BeanDefinition beanDefinition) {
        BEAN_MAP.put(beanId, beanDefinition);
    }
}
