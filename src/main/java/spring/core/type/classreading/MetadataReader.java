package spring.core.type.classreading;

import spring.core.type.AnnotationMetadata;
import spring.core.type.ClassMetadata;

public interface MetadataReader {
    ClassMetadata getClassMetadata();

    AnnotationMetadata getAnnotationMetadata();
}
