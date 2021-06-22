package spring.contex.support;

import org.junit.Test;
import spring.contex.ApplicationContext;
import spring.service.UserService;

import static org.junit.Assert.*;

public class FileSystemApplicationContextTest {
    @Test
    public void getBean() {
        ApplicationContext context = new FileSystemApplicationContext("src/test/resources/bean-v1.xml");
        UserService userService = (UserService) context.getBean("userService");
        assertNotNull(userService);
    }
}