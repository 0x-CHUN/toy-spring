package spring.beans.factory.support;

import spring.beans.BeanDefinition;
import spring.util.StringUtils;

public class BeanDefinitionReaderUtils {
    public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";

    public static String generateBeanName(
            BeanDefinition definition, BeanDefinitionRegistry registry, boolean isInnerBean) {
        String generatedBeanName = definition.getBeanClassName();
        if (!StringUtils.hasText(generatedBeanName)) {
            throw new RuntimeException("Unnamed bean definition specifies neither " +
                    "'class' nor 'parent' nor 'factory-bean' - can't generate bean name");
        }
        String id = generatedBeanName;
        if (isInnerBean) {
            id = generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR + Integer.toHexString(System.identityHashCode(definition));
        } else {
            int counter = -1;
            while (counter == -1 || (registry.getBeanDefinition(id) != null)) {
                counter++;
                id = generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR + counter;
            }
        }
        return id;
    }

    public static String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        return generateBeanName(beanDefinition, registry, false);
    }

    public static String registerWithGeneratedName(GenericBeanDefinition definition, BeanDefinitionRegistry registry) {
        String generatedName = generateBeanName(definition, registry, false);
        registry.registerBeanDefinition(generatedName, definition);
        return generatedName;
    }
}
