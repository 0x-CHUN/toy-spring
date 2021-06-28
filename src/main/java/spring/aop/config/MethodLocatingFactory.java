package spring.aop.config;

import spring.beans.BeanUtils;
import spring.beans.factory.BeanFactory;
import spring.util.StringUtils;

import java.lang.reflect.Method;

public class MethodLocatingFactory {
    private String targetBeanName;
    private String methodName;
    private Method method;

    public MethodLocatingFactory() {
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        if (!StringUtils.hasText(this.targetBeanName)) {
            throw new IllegalArgumentException("Property 'targetBeanName' is required");
        }
        if (!StringUtils.hasText(this.methodName)) {
            throw new IllegalArgumentException("Property 'methodName' is required");
        }
        Class<?> beanClass = beanFactory.getType(this.targetBeanName);
        if (beanClass == null) {
            throw new IllegalArgumentException("Can't determine type of bean with name '" + this.targetBeanName + "'");
        }
        this.method = BeanUtils.resolveSignature(this.methodName, beanClass);
        if (this.method == null) {
            throw new IllegalArgumentException("Unable to locate method [" + this.methodName +
                    "] on bean [" + this.targetBeanName + "]");
        }
    }


    public String getTargetBeanName() {
        return targetBeanName;
    }

    public void setTargetBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
