package spring.context.support;

import spring.aop.aspectj.AspectJAutoProxyCreator;
import spring.beans.factory.annotation.AutowiredAnnotationProcessor;
import spring.beans.factory.config.ConfigurableBeanFactory;
import spring.beans.factory.support.DefaultBeanFactory;
import spring.beans.factory.xml.XmlBeanDefinitionReader;

import spring.context.ApplicationContext;
import spring.core.io.Resource;

import java.util.List;

public abstract class AbstractApplicationContext implements ApplicationContext {
    private DefaultBeanFactory factory;

    public AbstractApplicationContext(String configPath) {
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(getResourceByPath(configPath));
        registerBeanPostProcessors(factory);
    }

    protected void registerBeanPostProcessors(ConfigurableBeanFactory beanFactory) {
        {
            AutowiredAnnotationProcessor postProcessor = new AutowiredAnnotationProcessor();
            postProcessor.setBeanFactory(beanFactory);
            beanFactory.addBeanPostProcessor(postProcessor);
        }
        {
            AspectJAutoProxyCreator postProcessor = new AspectJAutoProxyCreator();
            postProcessor.setBeanFactory(beanFactory);
            beanFactory.addBeanPostProcessor(postProcessor);
        }

    }

    /**
     * Get Resource by config path
     *
     * @param configPath: the config path : ClassPath or FileSystemPath
     * @return Resource
     */
    protected abstract Resource getResourceByPath(String configPath);

    public Object getBean(String beanId) {
        return this.factory.getBean(beanId);
    }

    public Class<?> getType(String name) {
        return this.factory.getType(name);
    }

    public List<Object> getBeansByType(Class<?> type) {
        return this.factory.getBeansByType(type);
    }
}
