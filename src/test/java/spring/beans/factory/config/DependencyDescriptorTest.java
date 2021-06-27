package spring.beans.factory.config;

import org.junit.Before;
import org.junit.Test;
import spring.beans.factory.support.DefaultBeanFactory;
import spring.beans.factory.xml.XmlBeanDefinitionReader;
import spring.core.io.ClassPathResource;
import spring.service.component.ComponentService;
import spring.service.component.DataService;
import spring.service.component.UserService;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class DependencyDescriptorTest {

    private DefaultBeanFactory factory;

    @Before
    public void setup() {
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource("bean-v4.xml"));
    }

    @Test
    public void testResolveDependency() throws Exception {
        Field field = ComponentService.class.getDeclaredField("userService");
        DependencyDescriptor descriptor = new DependencyDescriptor(field, true);
        Object resolveDependency = factory.resolveDependency(descriptor);
        assertTrue(resolveDependency instanceof UserService);

        field = ComponentService.class.getDeclaredField("dataService");
        descriptor = new DependencyDescriptor(field, true);
        resolveDependency = factory.resolveDependency(descriptor);
        assertTrue(resolveDependency instanceof DataService);
    }

}