package spring.contex.support;

import org.junit.Test;
import spring.aop.config.AspectInstanceFactory;
import spring.beans.factory.BeanFactory;
import spring.beans.factory.support.DefaultBeanFactory;
import spring.beans.factory.xml.XmlBeanDefinitionReader;
import spring.core.io.ClassPathResource;
import spring.core.io.Resource;
import spring.transaction.TransactionManager;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class AbstractApplicationContextTest {
    protected BeanFactory getBeanFactory(String configFile) {
        DefaultBeanFactory beanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        Resource resource = new ClassPathResource(configFile);
        reader.loadBeanDefinitions(resource);
        return beanFactory;
    }

    protected Method getAdviceMethod(String methodName) throws Exception{
        return TransactionManager.class.getMethod(methodName);
    }

    protected AspectInstanceFactory getAspectInstanceFactory(String targetBeanName){
        AspectInstanceFactory factory = new AspectInstanceFactory();
        factory.setAspectBeanName(targetBeanName);
        return factory;
    }

    @Test
    public void init(){}
}