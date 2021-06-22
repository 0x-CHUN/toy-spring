package spring.beans.factory.xml;

import org.junit.Before;
import org.junit.Test;
import spring.beans.factory.support.DefaultBeanFactory;
import spring.core.io.ClassPathResource;
import spring.core.io.FileSystemResource;
import spring.service.UserService;

import static org.junit.Assert.*;

public class XmlBeanDefinitionReaderTest {
    private DefaultBeanFactory factory;
    private XmlBeanDefinitionReader reader;

    @Before
    public void init() {
        factory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(factory);
    }

    @Test
    public void testClassPathResource() {
        reader.loadBeanDefinitions(new ClassPathResource("bean-v1.xml"));
        UserService userService = (UserService) factory.getBean("userService");
        assertNotNull(userService);
    }

    @Test
    public void testFileSystemResource() {
        reader.loadBeanDefinitions(new FileSystemResource("src/test/resources/bean-v1.xml"));
        UserService userService = (UserService) factory.getBean("userService");
        assertNotNull(userService);
    }
}