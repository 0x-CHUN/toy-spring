package spring.contex.support;

import spring.beans.factory.support.DefaultBeanFactory;
import spring.beans.factory.xml.XmlBeanDefinitionReader;
import spring.contex.ApplicationContext;
import spring.core.io.Resource;

public abstract class AbstractApplicationContext implements ApplicationContext {
    private DefaultBeanFactory factory;

    public AbstractApplicationContext(String configPath) {
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(getResourceByPath(configPath));
    }


    /**
     * Get Resource by config path
     *
     * @param configPath: the config path : ClassPath or FileSystemPath
     * @return Resource
     */
    protected abstract Resource getResourceByPath(String configPath);

    @Override
    public Object getBean(String beanId) {
        return factory.getBean(beanId);
    }

    @Override
    public Class<?> getType(String name) {
        return this.factory.getType(name);
    }
}
