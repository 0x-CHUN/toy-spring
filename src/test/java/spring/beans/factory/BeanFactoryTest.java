package spring.beans.factory;

import org.junit.Test;
import spring.beans.factory.support.DefaultBeanFactory;
import spring.service.UserService;

import static org.junit.Assert.*;

public class BeanFactoryTest {

    @Test
    public void testGetBean() {
        BeanFactory beanFactory = new DefaultBeanFactory("bean-v1.xml");
        UserService userService = (UserService) beanFactory.getBean("userService");
        assertNotNull(userService);
    }
}