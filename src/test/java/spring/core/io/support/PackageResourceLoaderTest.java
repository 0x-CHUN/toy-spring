package spring.core.io.support;

import org.junit.Test;
import spring.core.io.Resource;

import static org.junit.Assert.*;

public class PackageResourceLoaderTest {
    @Test
    public void testGetPackageResource() {
        PackageResourceLoader loader = new PackageResourceLoader();
        Resource[] resources = loader.getResources("spring.dao");
        assertEquals(2, resources.length);
        resources = loader.getResources("spring.service.component");
        assertEquals(3, resources.length);
    }
}