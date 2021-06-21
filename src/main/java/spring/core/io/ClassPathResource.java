package spring.core.io;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ClassPathResource implements Resource {
    private String path;

    public ClassPathResource(String path) {
        this.path = path;
    }

    @Override
    public InputStream getInputStream() throws Exception {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(this.path);
        if (inputStream == null) {
            throw new FileNotFoundException(path + " cannot be opened");
        }
        return inputStream;
    }
}
