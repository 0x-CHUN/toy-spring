package spring.aop.framework;

import spring.aop.Advice;
import spring.aop.Pointcut;
import spring.aop.config.AopConfig;
import spring.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AopConfigSupport implements AopConfig {
    private boolean proxyTargetClass = false;

    private Object targetObject = null;

    private final List<Advice> advices = new ArrayList<>();
    private final List<Class> interfaces = new ArrayList<>();

    public AopConfigSupport() {
    }

    @Override
    public Class<?> getTargetClass() {
        return this.targetObject.getClass();
    }

    @Override
    public Object getTargetObject() {
        return this.targetObject;
    }

    @Override
    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    @Override
    public Class<?>[] getProxiedInterfaces() {
        return this.interfaces.toArray(new Class[this.interfaces.size()]);
    }

    @Override
    public boolean isInterfaceProxied(Class<?> interf) {
        for (Class proxyInterface : interfaces) {
            if (interf.isAssignableFrom(proxyInterface)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Advice> getAdvices() {
        return this.advices;
    }

    @Override
    public void addAdvice(Advice advice) {
        this.advices.add(advice);
    }

    @Override
    public List<Advice> getAdvices(Method method) {
        List<Advice> results = new ArrayList<>();
        for (Advice advice : advices) {
            Pointcut pointcut = advice.getPointcut();
            if (pointcut.getMethodMatcher().matches(method)){
                results.add(advice);
            }
        }
        return advices;
    }

    @Override
    public void setTargetObject(Object obj) {
        this.targetObject = obj;
    }

    public void addInterface(Class<?> interfaces) {
        Assert.notNull(interfaces, "Interface must not be null");
        if (!interfaces.isInterface()) {
            throw new IllegalArgumentException("[" + interfaces.getName() + "] is not an interface");
        }
        if (!this.interfaces.contains(interfaces)) {
            this.interfaces.add(interfaces);

        }
    }
}
