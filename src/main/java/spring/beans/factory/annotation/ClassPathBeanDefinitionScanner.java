package spring.beans.factory.annotation;

import spring.beans.BeanDefinition;
import spring.beans.factory.support.BeanDefinitionRegistry;
import spring.beans.factory.support.BeanNameGenerator;
import spring.context.annotation.AnnotationBeanNameGenerator;
import spring.context.annotation.ScannedGenericBeanDefinition;
import spring.core.io.Resource;
import spring.core.io.support.PackageResourceLoader;
import spring.core.type.classreading.MetadataReader;
import spring.core.type.classreading.SimpleMetadataReader;
import spring.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import static spring.util.StringUtils.tokenizeToStringArray;

public class ClassPathBeanDefinitionScanner {
    private final BeanDefinitionRegistry registry;
    private PackageResourceLoader resourceLoader = new PackageResourceLoader();
    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public Set<BeanDefinition> doScan(String packagesToScan) {
        String[] basePackages = tokenizeToStringArray(packagesToScan, ",");
        Set<BeanDefinition> beanDefinitions = new LinkedHashSet<>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                beanDefinitions.add(candidate);
                registry.registerBeanDefinition(candidate.getID(), candidate);
            }
        }
        return beanDefinitions;
    }


    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        try {
            Resource[] resources = this.resourceLoader.getResources(basePackage);
            for (Resource resource : resources) {
                try {
                    MetadataReader metadataReader = new SimpleMetadataReader(resource);
                    if (metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName())) {
                        ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader.getAnnotationMetadata());
                        String beanName = this.beanNameGenerator.generateBeanName(sbd, this.registry);
                        sbd.setId(beanName);
                        candidates.add(sbd);
                    }
                } catch (Throwable ex) {
                    throw new RuntimeException("Failed to read candidate component class: " + resource);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("I/O failure during classpath scanning", ex);
        }
        return candidates;
    }
}
