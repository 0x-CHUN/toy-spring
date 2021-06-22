package spring.contex.support;

import org.junit.Test;
import spring.contex.ApplicationContext;
import spring.service.UserService;

import static org.junit.Assert.*;

public class ClassPathXmlApplicationContextTest {

    @Test
    public void getBean() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean-v1.xml");
        UserService userService = (UserService) context.getBean("userService");
        assertNotNull(userService);
    }
}