package spring.beans.factory.annotation;

import org.junit.Assert;
import org.junit.Test;
import spring.beans.factory.config.DependencyDescriptor;
import spring.beans.factory.support.DefaultBeanFactory;
import spring.service.component.ComponentService;
import spring.service.component.DataService;
import spring.service.component.UserService;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;

public class AutowiredAnnotationProcessorTest {
    UserService userService = new UserService();
    DataService dataService = new DataService();

    DefaultBeanFactory factory = new DefaultBeanFactory() {
        @Override
        public Object resolveDependency(DependencyDescriptor descriptor) {
            if (descriptor.getDependencyType().equals(UserService.class)) {
                return userService;
            }
            if (descriptor.getDependencyType().equals(DataService.class)) {
                return dataService;
            }
            throw new RuntimeException("can't support types except AccountDao and ItemDao");
        }
    };

    @Test
    public void testGetInjectionMetadata() {
        AutowiredAnnotationProcessor processor = new AutowiredAnnotationProcessor();
        processor.setBeanFactory(factory);

        InjectionMetadata injectionMetadata = processor.buildAutowiringMetadata(ComponentService.class);
        List<InjectionElement> elements = injectionMetadata.getInjectionElements();

        Assert.assertEquals(2, elements.size());

        assertFieldExists(elements, "userService");
        assertFieldExists(elements, "dataService");

        ComponentService componentService = new ComponentService();
        injectionMetadata.inject(componentService);
        assertNotNull(componentService.getUserService());
        assertNotNull(componentService.getDataService());
    }

    private void assertFieldExists(List<InjectionElement> elements, String fieldName) {
        for (InjectionElement ele : elements) {
            AutowiredFieldElement fieldEle = (AutowiredFieldElement) ele;
            Field f = fieldEle.getField();
            if (f.getName().equals(fieldName)) {
                return;
            }
        }
        Assert.fail(fieldName + "does not exist!");
    }
}