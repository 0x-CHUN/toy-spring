package spring.core.type;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import spring.beans.factory.annotation.AnnotationAttributes;
import spring.core.io.ClassPathResource;
import spring.core.type.classreading.AnnotationMetadataReadingVisitor;
import spring.core.type.classreading.ClassMetadataReadingVisitor;
import spring.service.component.ComponentService;
import spring.stereotype.Component;

import static org.junit.Assert.*;

public class ClassMetadataTest {
    @Test
    public void testGetClassMetaData() throws Exception {
        ClassPathResource resource = new ClassPathResource("spring/service/component/ComponentService.class");
        ClassReader reader = new ClassReader(resource.getInputStream());
        ClassMetadataReadingVisitor visitor = new ClassMetadataReadingVisitor();
        reader.accept(visitor, ClassReader.SKIP_DEBUG);
        assertFalse(visitor.isAbstract());
        assertFalse(visitor.isInterface());
        assertFalse(visitor.isFinal());
        assertEquals(ComponentService.class.getName(), visitor.getClassName());
        assertEquals(visitor.getSuperClassName(), Object.class.getName());
        assertEquals(visitor.getInterfaceNames().length, 0);
    }

    @Test
    public void testGetAnnotation() throws Exception {
        ClassPathResource resource = new ClassPathResource("spring/service/component/ComponentService.class");
        ClassReader reader = new ClassReader(resource.getInputStream());
        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        reader.accept(visitor, ClassReader.SKIP_DEBUG);
        String annotation = Component.class.getName();
        assertTrue(visitor.hasAnnotation(annotation));
        AnnotationAttributes attributes = visitor.getAnnotationAttributes(annotation);
        assertEquals("componentService", attributes.get("value"));
    }
}