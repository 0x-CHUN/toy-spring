package spring.contex.annotation;

import spring.beans.factory.annotation.AnnotatedBeanDefinition;
import spring.beans.factory.support.GenericBeanDefinition;
import spring.core.type.AnnotationMetadata;

public class ScannedGenericBeanDefinition extends GenericBeanDefinition implements AnnotatedBeanDefinition {
    private final AnnotationMetadata metadata;

    public ScannedGenericBeanDefinition(AnnotationMetadata metadata) {
        super();
        this.metadata = metadata;
        setBeanClassName(this.metadata.getClassName());
    }


    @Override
    public AnnotationMetadata getMetadata() {
        return this.metadata;
    }
}
