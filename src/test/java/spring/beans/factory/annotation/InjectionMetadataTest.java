package spring.beans.factory.annotation;

import org.junit.Before;
import org.junit.Test;
import spring.beans.factory.support.DefaultBeanFactory;
import spring.beans.factory.xml.XmlBeanDefinitionReader;
import spring.core.io.ClassPathResource;
import spring.service.component.ComponentService;

import java.lang.reflect.Field;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class InjectionMetadataTest {
    private DefaultBeanFactory factory;

    @Before
    public void setUp() {
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource("bean-v4.xml"));
    }

    @Test
    public void testInject() throws Exception {
        LinkedList<InjectionElement> elements = new LinkedList<>();
        {
            Field field = ComponentService.class.getDeclaredField("userService");
            InjectionElement injectionElement = new AutowiredFieldElement(field, true, factory);
            elements.add(injectionElement);
        }
        {
            Field field = ComponentService.class.getDeclaredField("dataService");
            InjectionElement injectionElement = new AutowiredFieldElement(field, true, factory);
            elements.add(injectionElement);
        }

        InjectionMetadata metadata = new InjectionMetadata(elements);
        ComponentService componentService = new ComponentService();
        metadata.inject(componentService);

        assertNotNull(componentService.getUserService());
        assertNotNull(componentService.getDataService());
    }
}