package spring.aop.framework;

import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.proxy.*;
import spring.aop.Advice;
import spring.aop.config.AopConfig;
import spring.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


public class CglibProxyFactory implements AopProxyFactory {
    private static final int AOP_PROXY = 0;
    private static final int INVOKE_TARGET = 1;
    private static final int NO_OVERRIDE = 2;
    private static final int DISPATCH_TARGET = 3;
    private static final int DISPATCH_ADVISED = 4;
    private static final int INVOKE_EQUALS = 5;
    private static final int INVOKE_HASHCODE = 6;


    protected final AopConfig config;

    private Object[] constructorArgs;

    private Class<?>[] constructorArgTypes;

    public CglibProxyFactory(AopConfig config) throws AopConfigException {
        Assert.notNull(config, "AdvisedSupport must not be null");
        if (config.getAdvices().size() == 0 /*&& config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE*/) {
            throw new AopConfigException("No advisors and no TargetSource specified");
        }
        this.config = config;
    }

    @Override
    public Object getProxy() throws AopConfigException {
        return getProxy(null);
    }

    @Override
    public Object getProxy(ClassLoader classLoader) throws AopConfigException {
        try {
            Class<?> rootClass = this.config.getTargetClass();
            Enhancer enhancer = new Enhancer();
            if (classLoader != null) {
                enhancer.setClassLoader(classLoader);
            }
            enhancer.setSuperclass(rootClass);
            enhancer.setInterceptDuringConstruction(false);

            Callback[] callbacks = getCallbacks(rootClass);
            Class<?>[] types = new Class<?>[callbacks.length];
            for (int x = 0; x < types.length; x++) {
                types[x] = callbacks[x].getClass();
            }
            enhancer.setCallbackFilter(new ProxyCallbackFilter(this.config));
            enhancer.setCallbackTypes(types);
            enhancer.setCallbacks(callbacks);

            return enhancer.create();
        } catch (CodeGenerationException | IllegalArgumentException ex) {
            throw new AopConfigException("Could not generate CGLIB subclass of class [" +
                    this.config.getTargetClass() + "]: " +
                    "Common causes of this problem include using a final class or a non-visible class",
                    ex);
        } catch (Exception ex) {
            throw new AopConfigException("Unexpected AOP exception", ex);
        }
    }

    private Callback[] getCallbacks(Class<?> rootClass) {
        Callback aopInterceptor = new DynamicAdvisedInterceptor(this.config);
        return new Callback[]{
                aopInterceptor,
        };
    }

    private static class DynamicAdvisedInterceptor implements MethodInterceptor, Serializable {

        private final AopConfig config;

        public DynamicAdvisedInterceptor(AopConfig advised) {
            this.config = advised;
        }

        public Object intercept(Object proxy, Method method,
                                Object[] args, MethodProxy methodProxy) throws Throwable {
            Object target = this.config.getTargetObject();
            List<Advice> chain = this.config.getAdvices(method);
            Object retVal;
            if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
                retVal = methodProxy.invoke(target, args);
            } else {
                List<org.aopalliance.intercept.MethodInterceptor> interceptors = new ArrayList<>(chain);
                retVal = new ReflectiveMethodInvocation(target, method, args, interceptors).proceed();
            }
            return retVal;

        }
    }

    private static class ProxyCallbackFilter implements CallbackFilter {

        private final AopConfig config;

        public ProxyCallbackFilter(AopConfig advised) {
            this.config = advised;

        }

        public int accept(Method method) {
            return AOP_PROXY;
        }

    }
}
