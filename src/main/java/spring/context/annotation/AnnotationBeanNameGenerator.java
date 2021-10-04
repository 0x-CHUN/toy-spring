package spring.context.annotation;

import spring.beans.BeanDefinition;
import spring.beans.factory.annotation.AnnotatedBeanDefinition;
import spring.beans.factory.annotation.AnnotationAttributes;
import spring.beans.factory.support.BeanDefinitionRegistry;
import spring.beans.factory.support.BeanNameGenerator;
import spring.core.type.AnnotationMetadata;
import spring.util.ClassUtils;
import spring.util.StringUtils;

import java.beans.Introspector;
import java.util.Set;

public class AnnotationBeanNameGenerator implements BeanNameGenerator {
    @Override
    public String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        if (beanDefinition instanceof AnnotatedBeanDefinition) {
            String beanName = deterdetermineBeanNameFromAnnotation((AnnotatedBeanDefinition) beanDefinition);
            if (StringUtils.hasLength(beanName)) {
                return beanName;
            }
        }
        return buildDefaultBeanName(beanDefinition, registry);
    }

    protected String buildDefaultBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        return buildDefaultBeanName(beanDefinition);
    }

    private String buildDefaultBeanName(BeanDefinition beanDefinition) {
        String shortClassName = ClassUtils.getShortName(beanDefinition.getBeanClassName());
        return Introspector.decapitalize(shortClassName);
    }

    protected String deterdetermineBeanNameFromAnnotation(AnnotatedBeanDefinition definition) {
        AnnotationMetadata metadata = definition.getMetadata();
        Set<String> types = metadata.getAnnotationTypes();
        String beanName = null;
        for (String type : types) {
            AnnotationAttributes attributes = metadata.getAnnotationAttributes(type);
            if (attributes.get("value") != null) {
                Object val = attributes.get("value");
                if (val instanceof String) {
                    String value = (String) val;
                    if (StringUtils.hasLength(value)) {
                        beanName = value;
                    }
                }
            }
        }
        return beanName;
    }
}
