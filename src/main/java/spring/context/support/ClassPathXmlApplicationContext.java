package spring.context.support;

import spring.core.io.ClassPathResource;
import spring.core.io.Resource;


public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    public ClassPathXmlApplicationContext(String configPath) {
        super(configPath);
    }

    @Override
    protected Resource getResourceByPath(String configPath) {
        return new ClassPathResource(configPath);
    }

}