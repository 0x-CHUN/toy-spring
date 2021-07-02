package spring.core.type.classreading;

import org.objectweb.asm.ClassReader;
import spring.core.io.Resource;
import spring.core.type.AnnotationMetadata;
import spring.core.type.ClassMetadata;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.objectweb.asm.ClassReader.SKIP_DEBUG;

public class SimpleMetadataReader implements MetadataReader {

    private final ClassMetadata classMetadata;

    private final AnnotationMetadata annotationMetadata;


    public SimpleMetadataReader(Resource resource) throws IOException {

        ClassReader classReader;

        try (InputStream is = new BufferedInputStream(resource.getInputStream())) {
            classReader = new ClassReader(is);
        }

        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        classReader.accept(visitor, SKIP_DEBUG);

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
