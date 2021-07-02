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


    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public Object getTargetObject() {
        return this.targetObject;
    }

    public Class<?> getTargetClass() {
        return this.targetObject.getClass();
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

    public Class<?>[] getProxiedInterfaces() {
        return this.interfaces.toArray(new Class[this.interfaces.size()]);
    }

    public boolean isInterfaceProxied(Class<?> intf) {
        for (Class proxyIntf : this.interfaces) {
            if (intf.isAssignableFrom(proxyIntf)) {
                return true;
            }
        }
        return false;
    }

    public void addAdvice(Advice advice) {
        this.advices.add(advice);
    }


    public boolean isProxyTargetClass() {

        return proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    public List<Advice> getAdvices() {

        return this.advices;
    }

    public List<Advice> getAdvices(Method method) {
        List<Advice> result = new ArrayList<>();
        for (Advice advice : this.getAdvices()) {
            Pointcut pc = advice.getPointcut();
            if (pc.getMethodMatcher().matches(method)) {
                result.add(advice);
            }
        }
        return result;
    }
}
