package spring.core.type;

import spring.beans.factory.annotation.AnnotationAttributes;

import java.util.Set;

public interface AnnotationMetadata extends ClassMetadata {
    /**
     * 注解类型
     *
     * @return
     */
    Set<String> getAnnotationTypes();

    /**
     * 是否存在某个注解
     *
     * @param annotationType
     * @return
     */
    boolean hasAnnotation(String annotationType);

    /**
     * 注解属性
     *
     * @param annotationType
     * @return
     */
    AnnotationAttributes getAnnotationAttributes(String annotationType);
}
