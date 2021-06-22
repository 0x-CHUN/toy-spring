package spring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import spring.beans.BeanDefinition;
import spring.beans.factory.support.BeanDefinitionRegistry;
import spring.beans.factory.support.GenericBeanDefinition;
import spring.core.io.Resource;

import java.io.InputStream;
import java.util.List;

public class XmlBeanDefinitionReader {
    private final BeanDefinitionRegistry registry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void loadBeanDefinitions(Resource resource) {
        SAXReader reader = new SAXReader();
        try (InputStream inputStream = resource.getInputStream()) {
            Document document = reader.read(inputStream);
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            BeanDefinition beanDefinition;
            for (Element element : elements) {
                beanDefinition = new GenericBeanDefinition(element.attribute("id").getValue(),
                        element.attribute("class").getValue());
                registry.registryBeanDefinition(element.attribute("id").getValue(), beanDefinition);
            }
        } catch (Exception e) {
            // todo: exception
            throw new RuntimeException("parse bean definition file error", e);
        }
    }
}
