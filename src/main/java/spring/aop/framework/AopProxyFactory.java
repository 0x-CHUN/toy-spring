package spring.aop.framework;

public interface AopProxyFactory {

    Object getProxy() throws AopConfigException; // get the proxy, usually, the thread context class loader.

    Object getProxy(ClassLoader classLoader) throws AopConfigException;
}
