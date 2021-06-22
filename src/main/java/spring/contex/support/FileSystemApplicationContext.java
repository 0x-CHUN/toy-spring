package spring.contex.support;

import spring.core.io.FileSystemResource;
import spring.core.io.Resource;

public class FileSystemApplicationContext extends AbstractApplicationContext {

    public FileSystemApplicationContext(String configPath) {
        super(configPath);
    }

    @Override
    protected Resource getResourceByPath(String configPath) {
        return new FileSystemResource(configPath);
    }

}
