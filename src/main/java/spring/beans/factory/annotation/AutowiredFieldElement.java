package spring.beans.factory.annotation;

import spring.beans.factory.config.AutowireCapableBeanFactory;
import spring.beans.factory.config.DependencyDescriptor;
import spring.util.ReflectionUtils;

import java.lang.reflect.Field;

public class AutowiredFieldElement extends InjectionElement {
    boolean required;

    public AutowiredFieldElement(Field f, boolean required, AutowireCapableBeanFactory factory) {
        super(f, factory);
        this.required = required;
    }

    public Field getField() {
        return (Field) this.member;
    }

    @Override
    public void inject(Object target) {
        Field field = getField();
        try {
            DependencyDescriptor descriptor = new DependencyDescriptor(field, this.required);
            Object value = this.factory.resolveDependency(descriptor);
            if (value != null) {
                ReflectionUtils.makeAccessible(field);
                field.set(target, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not autowire field: " + field);
        }
    }
}
