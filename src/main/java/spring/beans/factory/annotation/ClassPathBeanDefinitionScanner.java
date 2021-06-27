package spring.beans.factory.annotation;

import spring.beans.BeanDefinition;
import spring.beans.factory.support.BeanDefinitionRegistry;
import spring.beans.factory.support.BeanNameGenerator;
import spring.contex.annotation.AnnotationBeanNameGenerator;
import spring.contex.annotation.ScannedGenericBeanDefinition;
import spring.core.io.Resource;
import spring.core.io.support.PackageResourceLoader;
import spring.core.type.classreading.MetadataReader;
import spring.core.type.classreading.SimpleMetadataReader;
import spring.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

import static spring.util.StringUtils.tokenizeToStringArray;

public class ClassPathBeanDefinitionScanner {

    private final BeanDefinitionRegistry registry;
    private PackageResourceLoader loader = new PackageResourceLoader();
    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public Set<BeanDefinition> doScan(String packagePath) {
        String[] basePackages = tokenizeToStringArray(packagePath, ",");
        Set<BeanDefinition> beanDefinitions = new LinkedHashSet<>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                beanDefinitions.add(candidate);
                registry.registryBeanDefinition(candidate.getID(), candidate);
            }
        }
        return beanDefinitions;
    }

    private Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        try {
            Resource[] resources = this.loader.getResources(basePackage);
            for (Resource resource : resources) {
                try {
                    MetadataReader reader = new SimpleMetadataReader(resource);
                    if (reader.getAnnotationMetadata().hasAnnotation(Component.class.getName())) {
                        ScannedGenericBeanDefinition definition = new ScannedGenericBeanDefinition(reader.getAnnotationMetadata());
                        String beanName = this.beanNameGenerator.generateBeanName(definition, this.registry);
                        definition.setBeanId(beanName);
                        candidates.add(definition);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to read candidate component class: " + resource);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("I/O failure during classpath scanning", e);
        }
        return candidates;
    }
}
