package spring.beans.factory.support;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import spring.beans.factory.BeanFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultBeanFactory implements BeanFactory {
    private static final Map<String, String> BEAN_MAP = new HashMap<>();

    public DefaultBeanFactory(String configPath) {
        // todo: check the configPath
        URL url = Thread.currentThread().getContextClassLoader().getResource(configPath);
        File file = new File(url.getPath());
        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
            return;
        }
        Element root = document.getRootElement();
        List<Element> elements = root.elements();
        for (Element element : elements) {
            BEAN_MAP.put(element.attribute("id").getValue(), element.attribute("class").getValue());
        }
    }

    @Override
    public Object getBean(String beanId) {
        String beanClassName = BEAN_MAP.get(beanId);
        Class target = null;
        try {
            target = Thread.currentThread().getContextClassLoader().loadClass(beanClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert target != null;
            return target.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
