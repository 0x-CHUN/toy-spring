package spring.beans.factory.annotation;

import org.junit.Test;
import spring.beans.factory.support.DefaultBeanFactory;
import spring.service.component.ComponentService;
import spring.service.component.DataService;
import spring.service.component.UserService;

import static org.junit.Assert.*;

public class ClassPathBeanDefinitionScannerTest {
    @Test
    public void testParseScannerBean() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        assertNotNull(factory);
        String basePackage = "spring.service,spring.dao";
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(factory);
        scanner.doScan(basePackage);
        ComponentService componentService = (ComponentService) factory.getBean("componentService");
        DataService dataService = (DataService) factory.getBean("dataService");
        UserService userService = (UserService) factory.getBean("userService");
        assertNotNull(componentService);
        assertNotNull(dataService);
        assertNotNull(userService);
    }
}