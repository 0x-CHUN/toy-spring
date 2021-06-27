package spring.beans.factory.annotation;

import spring.beans.BeanDefinition;
import spring.core.type.AnnotationMetadata;

public interface AnnotatedBeanDefinition extends BeanDefinition {
    AnnotationMetadata getMetadata();
}
