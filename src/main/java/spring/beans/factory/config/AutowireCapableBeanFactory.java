package spring.beans.factory.config;

public interface AutowireCapableBeanFactory {
    Object resolveDependency(DependencyDescriptor descriptor);
}
