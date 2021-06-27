package spring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import spring.beans.BeanDefinition;
import spring.beans.ConstructorArgument;
import spring.beans.PropertyValue;
import spring.beans.ScopeType;
import spring.beans.factory.annotation.ClassPathBeanDefinitionScanner;
import spring.beans.factory.config.RuntimeBeanReference;
import spring.beans.factory.config.TypedStringValue;
import spring.beans.factory.support.BeanDefinitionRegistry;
import spring.beans.factory.support.GenericBeanDefinition;
import spring.core.io.Resource;
import spring.util.StringUtils;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class XmlBeanDefinitionReader {
    public static final String ID_ATTRIBUTE = "id";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String SCOPE_ATTRIBUTE = "scope";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
    public static final String TYPE_ATTRIBUTE = "type";
    public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";
    public static final String CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/context";
    public static final String AOP_NAMESPACE_URI = "http://www.springframework.org/schema/aop";
    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";


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
                String namespaceUri = element.getNamespaceURI();
                if (this.isDefaultNamespace(namespaceUri)) {
                    // normal bean
                    parseDefaultElement(element);
                } else if (this.isContextNamespace(namespaceUri)) {
                    parseComponentElement(element);
                }
            }
        } catch (Exception e) {
            // todo: exception
            throw new RuntimeException("parse bean definition file error", e);
        }
    }

    private void parseComponentElement(Element element) {
        String basePackages = element.attributeValue(BASE_PACKAGE_ATTRIBUTE);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.doScan(basePackages);
    }

    private void parseDefaultElement(Element element) {
        BeanDefinition beanDefinition = new GenericBeanDefinition(element.attribute(ID_ATTRIBUTE).getValue(),
                element.attribute(CLASS_ATTRIBUTE).getValue());
        if (element.attribute(SCOPE_ATTRIBUTE) != null) {
            beanDefinition.setScope(ScopeType.getType(element.attribute(SCOPE_ATTRIBUTE).getValue()));
        }
        // inject constructor
        parseConstructorArgElements(element, beanDefinition);
        // inject value of element into bean
        parsePropertyElement(element, beanDefinition);
        registry.registryBeanDefinition(element.attribute(ID_ATTRIBUTE).getValue(), beanDefinition);
    }

    public boolean isDefaultNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || BEANS_NAMESPACE_URI.equals(namespaceUri));
    }

    public boolean isContextNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || CONTEXT_NAMESPACE_URI.equals(namespaceUri));
    }


    private void parseConstructorArgElements(Element element, BeanDefinition beanDefinition) {
        Iterator iterator = element.elementIterator(CONSTRUCTOR_ARG_ELEMENT);
        while (iterator.hasNext()) {
            Element ele = (Element) iterator.next();
            parseConstructorArgElement(ele, beanDefinition);
        }
    }

    private void parseConstructorArgElement(Element element, BeanDefinition beanDefinition) {
        String type = element.attributeValue(TYPE_ATTRIBUTE);
        String name = element.attributeValue(NAME_ATTRIBUTE);
        Object value = parsePropertyValue(element, null);

        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(value);
        if (StringUtils.hasLength(type)) {
            valueHolder.setType(type);
        }
        if (StringUtils.hasLength(name)) {
            valueHolder.setName(name);
        }
        beanDefinition.getConstructorArgument().addArgumentValue(valueHolder);
    }

    private void parsePropertyElement(Element beanElem, BeanDefinition beanDefinition) {
        Iterator iter = beanElem.elementIterator(PROPERTY_ELEMENT); // get the iterator of property
        while (iter.hasNext()) {
            Element propElem = (Element) iter.next();
            String propertyName = propElem.attributeValue(NAME_ATTRIBUTE); // get the property name
            if (!StringUtils.hasLength(propertyName)) {
                System.out.println("Tag 'property' must have a 'name' attribute");
                return;
            }

            Object val = parsePropertyValue(propElem, propertyName); // get the property value
            PropertyValue propertyValue = new PropertyValue(propertyName, val); // new a PropertyValue
            beanDefinition.getPropertyValues().add(propertyValue);
        }
    }

    private Object parsePropertyValue(Element ele, String propertyName) {
        String elementName = (propertyName != null) ?
                "<property> element for property '" + propertyName + "'" :
                "<constructor-arg> element";

        boolean hasRefAttribute = (ele.attribute(REF_ATTRIBUTE) != null);
        boolean hasValueAttribute = (ele.attribute(VALUE_ATTRIBUTE) != null);

        if (hasRefAttribute) {
            //  <property name="xxx" ref="xxx"/>
            String refName = ele.attributeValue(REF_ATTRIBUTE);
            if (!StringUtils.hasText(refName)) {
                System.out.println(elementName + " contains empty 'ref' attribute");
            }
            return new RuntimeBeanReference(refName);
        } else if (hasValueAttribute) {
            //   <property name="xxx" value="xxx"/>
            return new TypedStringValue(ele.attributeValue(VALUE_ATTRIBUTE));
        } else {
            throw new RuntimeException(elementName + " must specify a ref or value");
        }
    }
}
