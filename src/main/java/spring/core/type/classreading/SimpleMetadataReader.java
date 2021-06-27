package spring.core.type.classreading;

import org.objectweb.asm.ClassReader;
import spring.core.io.Resource;
import spring.core.type.AnnotationMetadata;
import spring.core.type.ClassMetadata;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class SimpleMetadataReader implements MetadataReader {
    private final ClassMetadata classMetadata;
    private final AnnotationMetadata annotationMetadata;

    public SimpleMetadataReader(Resource resource) throws Exception {
        ClassReader classReader;
        try (InputStream is = new BufferedInputStream(resource.getInputStream())) {
            classReader = new ClassReader(is);
        }
        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        classReader.accept(visitor, ClassReader.SKIP_DEBUG);
        this.annotationMetadata = visitor;
        this.classMetadata = visitor;
    }

    @Override
    public ClassMetadata getClassMetadata() {
        return this.classMetadata;
    }

    @Override
    public AnnotationMetadata getAnnotationMetadata() {
        return this.annotationMetadata;
    }
}
