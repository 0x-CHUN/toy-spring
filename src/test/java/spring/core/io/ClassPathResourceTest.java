package spring.core.io;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class ClassPathResourceTest {

    @Test
    public void getInputStream() throws Exception {
        Resource resource = new ClassPathResource("bean-v1.xml");
        try (InputStream inputStream = resource.getInputStream()) {
            assertNotNull(inputStream);
        }
    }
}