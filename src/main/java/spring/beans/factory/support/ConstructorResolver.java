package spring.beans.factory.support;

import spring.beans.BeanDefinition;
import spring.beans.ConstructorArgument;
import spring.beans.SimpleTypeConverter;
import spring.beans.TypeConverter;
import spring.beans.factory.BeanFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ConstructorResolver {
    private final BeanFactory factory;

    public ConstructorResolver(BeanFactory factory) {
        this.factory = factory;
    }

    public Object autowireConstructor(final BeanDefinition beanDefinition) {
        Constructor<?> constructor = null;
        Object[] args = null;
        Class<?> beanClass = null;
        try {
            beanClass = Thread.currentThread().getContextClassLoader().loadClass(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(beanDefinition.getID() + "Instantiation of bean failed, can't resolve class", e);
        }
        Constructor<?>[] candidates = beanClass.getConstructors();
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.factory);
        ConstructorArgument constructorArgument = beanDefinition.getConstructorArgument();
        TypeConverter converter = new SimpleTypeConverter();

        for (int i = 0; i < candidates.length; i++) {
            Class<?>[] parameterType = candidates[i].getParameterTypes();
            if (parameterType.length != constructorArgument.getArgumentCount()) {
                continue;
            }
            args = new Object[parameterType.length];
            boolean result = this.valuesMatchTypes(
                    parameterType,
                    constructorArgument.getArgumentValues(),
                    args,
                    valueResolver,
                    converter);
            if (result) {
                constructor = candidates[i];
                break;
            }
        }
        if (constructor == null) {
            throw new RuntimeException(beanDefinition.getID() + "can't find a apporiate constructor");
        }

        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(beanDefinition.getID() + "can't find a create instance using " + constructor);
        }
    }

    private boolean valuesMatchTypes(Class<?>[] parameterTypes,
                                     List<ConstructorArgument.ValueHolder> valueHolders,
                                     Object[] argsToUse,
                                     BeanDefinitionValueResolver valueResolver,
                                     TypeConverter typeConverter) {
        for (int i = 0; i < parameterTypes.length; i++) {
            ConstructorArgument.ValueHolder valueHolder = valueHolders.get(i);
            Object originalValue = valueHolder.getValue();
            try {
                Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);
                Object convertedValue = typeConverter.convertIfNecessary(resolvedValue, parameterTypes[i]);
                argsToUse[i] = convertedValue;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
