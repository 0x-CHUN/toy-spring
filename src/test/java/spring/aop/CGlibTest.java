package spring.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;
import spring.service.cglib.UserService;
import spring.transaction.TransactionManager;

import java.lang.reflect.Method;

public class CGlibTest {
    public static class TransactionInterceptor implements MethodInterceptor {
        TransactionManager txManager = new TransactionManager();

        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            txManager.start();
            Object result = proxy.invokeSuper(obj, args);
            txManager.commit();
            return result;
        }
    }

    @Test
    public void testCallBack() {
        // need add flag --illegal-access=warn or permit
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserService.class);
        enhancer.setCallback(new TransactionInterceptor());
        UserService userService = (UserService) enhancer.create();
        userService.placeOrder();
    }
}
