package spring.core.type.classreading;

import org.junit.Test;
import spring.beans.factory.annotation.AnnotationAttributes;
import spring.core.io.ClassPathResource;
import spring.core.type.AnnotationMetadata;
import spring.stereotype.Component;

import static org.junit.Assert.*;

public class SimpleMetadataReaderTest {
    @Test
    public void testGetMetadata() throws Exception {
        ClassPathResource resource = new ClassPathResource("spring/service/component/ComponentService.class");
        assertNotNull(resource);
        MetadataReader reader = new SimpleMetadataReader(resource);


        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();

        String annotation = Component.class.getName();

        assertTrue(annotationMetadata.hasAnnotation(annotation));
        AnnotationAttributes attributes = annotationMetadata.getAnnotationAttributes(annotation);
        assertEquals("componentService", attributes.get("value"));

        assertFalse(annotationMetadata.isAbstract());
        assertFalse(annotationMetadata.isFinal());
        assertEquals("spring.service.component.ComponentService", annotationMetadata.getClassName());
    }
}