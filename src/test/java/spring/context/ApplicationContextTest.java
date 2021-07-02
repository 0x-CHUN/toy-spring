package spring.context;

import org.junit.Before;
import org.junit.Test;
import spring.context.support.ClassPathXmlApplicationContext;
import spring.service.aop.UserService;
import spring.service.aop.LogInterface;
import spring.util.MessageTracker;

import java.util.List;

import static org.junit.Assert.*;

public class ApplicationContextTest {
    @Before
    public void setup() {
        MessageTracker.clearMsgs();
    }

    @Test
    public void testPlaceOrder() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean-v5.xml");
        UserService userService = (UserService) context.getBean("userService");
        assertNotNull(userService);
        assertNotNull(userService.getAccountDao());
        assertNotNull(userService.getItemDao());

        userService.placeOrder();
        List<String> msgs = MessageTracker.getMsgs();

        assertEquals(3, msgs.size());
        assertEquals("start tx", msgs.get(0));
        assertEquals("place order", msgs.get(1));
        assertEquals("commit tx", msgs.get(2));
    }

    @Test
    public void testGetBeanProperty() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean-v6.xml");
        LogInterface log = (LogInterface) context.getBean("logService");
        assertNotNull(log);
        log.log();
        List<String> msgs = MessageTracker.getMsgs();

        assertEquals(3, msgs.size());
        assertEquals("start tx", msgs.get(0));
        assertEquals("place order", msgs.get(1));
        assertEquals("commit tx", msgs.get(2));
    }
}