package spring.aop.config;

import org.junit.Test;
import spring.beans.factory.support.DefaultBeanFactory;
import spring.beans.factory.xml.XmlBeanDefinitionReader;
import spring.core.io.ClassPathResource;
import spring.core.io.Resource;
import spring.transaction.TransactionManager;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class MethodLocatingFactoryTest {
    @Test
    public void testGetMethod() throws Exception {
        DefaultBeanFactory beanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        Resource resource = new ClassPathResource("bean-v5.xml");
        reader.loadBeanDefinitions(resource);
        MethodLocatingFactory methodLocatingFactory = new MethodLocatingFactory();
        methodLocatingFactory.setTargetBeanName("tx");
        methodLocatingFactory.setMethodName("start");
        methodLocatingFactory.setBeanFactory(beanFactory);
        Method method = methodLocatingFactory.getObject();
        assertNotNull(method);
        assertEquals(TransactionManager.class, method.getDeclaringClass());
        assertEquals(method, TransactionManager.class.getMethod("start"));
    }
}