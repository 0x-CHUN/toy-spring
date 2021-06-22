package spring.beans.factory.support;

import spring.beans.factory.BeanFactory;
import spring.beans.factory.config.RuntimeBeanReference;
import spring.beans.factory.config.TypedStringValue;

public class BeanDefinitionValueResolver {
    private final BeanFactory factory;

    public BeanDefinitionValueResolver(BeanFactory factory) {
        this.factory = factory;
    }

    /**
     * Resolve the value
     *
     * @param value
     * @return
     */
    public Object resolveValueIfNecessary(Object value) {
        if (value instanceof RuntimeBeanReference) { // <property name="xxx" ref="xxx"/>
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName(); // get the name
            Object bean = this.factory.getBean(refName); // get the ref value from the factory
            return bean;
        } else if (value instanceof TypedStringValue) { // <property name="version" value="2"/>
            return ((TypedStringValue) value).getValue(); // just return value e.g "2"
        } else {
            //TODO
            throw new RuntimeException("the value " + value + " has not implemented");
        }
    }
}
