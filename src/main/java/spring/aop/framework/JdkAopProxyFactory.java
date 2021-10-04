package spring.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import spring.aop.Advice;
import spring.aop.config.AopConfig;
import spring.util.Assert;
import spring.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class JdkAopProxyFactory implements AopProxyFactory, InvocationHandler {
    private final AopConfig config;

    public JdkAopProxyFactory(AopConfig config) {
        Assert.notNull(config, "AdvisedSupport must not be null");
        if (config.getAdvices().size() == 0) {
            throw new AopConfigException("No advices specified");
        }
        this.config = config;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = this.config.getTargetObject();
        Object retVal;

        List<Advice> chain = this.config.getAdvices();
        if (chain.isEmpty()) {
            retVal = method.invoke(target, args);
        } else {
            List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
            interceptors.addAll(chain);
            retVal = new ReflectiveMethodInvocation(target, method, args, interceptors).proceed();
        }
        return retVal;
    }

    @Override
    public Object getProxy() {
        return getProxy(ClassUtils.getDefaultClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        Class<?>[] proxiedInterfaces = config.getProxiedInterfaces();
        return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
    }
}
