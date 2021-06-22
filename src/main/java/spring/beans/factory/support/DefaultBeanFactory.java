package spring.beans.factory.support;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import spring.beans.BeanDefinition;
import spring.beans.PropertyValue;
import spring.beans.SimpleTypeConverter;
import spring.beans.factory.BeanFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
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

    private Object createBean(BeanDefinition beanDefinition) {
        // init bean
        Object bean = instantiateBean(beanDefinition);
        // inject ref bean into beanDefinition
        populateBean(beanDefinition, bean);
        return bean;
    }

    private Object instantiateBean(BeanDefinition definition) {
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

    private void populateBean(BeanDefinition beanDefinition, Object bean) {
        // get the propertyValues from beanDefinition
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();

        if (propertyValues == null || propertyValues.isEmpty()) {
            return;
        }
        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(this);
        SimpleTypeConverter converter = new SimpleTypeConverter();

        try {
            for (PropertyValue propertyValue : propertyValues) {
                String propertyName = propertyValue.getName(); // get the property name
                Object originalValue = propertyValue.getValue();// get the original value
                Object resolvedValue = resolver.resolveValueIfNecessary(originalValue); // use resolver transfer
                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass()); // get the bean info
                PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors(); // get the property descriptor
                for (PropertyDescriptor pd : pds) {
                    if (pd.getName().equals(propertyName)) { // check the property descriptor is equal to property name
                        // convert the resolvedValue e.g "2"-> 2., "true"->true
                        Object convertedValue = converter.convertIfNecessary(resolvedValue, pd.getPropertyType());
                        // set the value
                        pd.getWriteMethod().invoke(bean, convertedValue);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to obtain BeanInfo for class [" + beanDefinition.getBeanClassName() +
                    "]", ex);
        }
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
