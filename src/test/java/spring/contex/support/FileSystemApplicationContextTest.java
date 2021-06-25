package spring.contex.support;

import org.junit.Test;
import spring.contex.ApplicationContext;
import spring.service.ItemService;
import spring.service.UserService;

import static org.junit.Assert.*;

public class FileSystemApplicationContextTest {
    @Test
    public void getBean() {
        ApplicationContext context = new FileSystemApplicationContext("src/test/resources/bean-v1.xml");
        UserService userService = (UserService) context.getBean("userService");
        assertNotNull(userService);
    }

    @Test
    public void getBeanV2() {
        ApplicationContext context = new FileSystemApplicationContext("src/test/resources/bean-v2.xml");
        UserService userService = (UserService) context.getBean("userService");
        assertNotNull(userService);

        assertNotNull(userService.getItemDao());
        assertNotNull(userService.getAccountDao());
        assertEquals(userService.getOwner(), "test");
        assertEquals(userService.getVersion(), 2);
        assertTrue(userService.isChecked());
    }

    @Test
    public void getBeanV3() {
        ApplicationContext context = new FileSystemApplicationContext("src/test/resources/bean-v3.xml");
        ItemService itemService = (ItemService) context.getBean("itemService");
        assertNotNull(itemService);
        assertNotNull(itemService.getItemDao());
        assertNotNull(itemService.getAccountDao());
        assertEquals(itemService.getVersion(), 1);
    }
}