package spring.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Before;
import org.junit.Test;
import spring.aop.aspectj.AspectJAfterReturningAdvice;
import spring.aop.aspectj.AspectJAfterThrowingAdvice;
import spring.aop.aspectj.AspectJBeforeAdvice;
import spring.service.aop.UserService;
import spring.transaction.TransactionManager;
import spring.util.MessageTracker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ReflectiveMethodInvocationTest {
    private AspectJBeforeAdvice beforeAdvice = null;
    private AspectJAfterReturningAdvice afterAdvice = null;
    private AspectJAfterThrowingAdvice afterThrowingAdvice = null;
    private UserService service = null;
    private TransactionManager transactionManager;

    @Before
    public void setUp() throws Exception {
        service = new UserService();
        transactionManager = new TransactionManager();
        MessageTracker.clearMsgs();

        beforeAdvice = new AspectJBeforeAdvice(
                TransactionManager.class.getMethod("start"),
                null,
                transactionManager
        );

        afterAdvice = new AspectJAfterReturningAdvice(
                TransactionManager.class.getMethod("commit"),
                null,
                transactionManager
        );
        afterThrowingAdvice = new AspectJAfterThrowingAdvice(
                TransactionManager.class.getMethod("rollback"),
                null,
                transactionManager
        );
    }

    @Test
    public void testMethodInvocation() throws Throwable {
        Method targetMethod = UserService.class.getMethod("placeOrder");

        List<MethodInterceptor> interceptors = new ArrayList<>();
        interceptors.add(beforeAdvice);
        interceptors.add(afterAdvice);

        ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(
                service, targetMethod, new Object[0], interceptors
        );

        methodInvocation.proceed();
        List<String> msgs = MessageTracker.getMsgs();

        assertEquals(msgs.size(), 3);
        assertEquals("start tx", msgs.get(0));
        assertEquals("place order", msgs.get(1));
        assertEquals("commit tx", msgs.get(2));
    }

    @Test
    public void testMethodInvocation2() throws Throwable {
        Method targetMethod = UserService.class.getMethod("placeOrder");

        List<MethodInterceptor> interceptors = new ArrayList<>();
        interceptors.add(afterAdvice);
        interceptors.add(beforeAdvice);

        ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(
                service, targetMethod, new Object[0], interceptors
        );

        methodInvocation.proceed();
        List<String> msgs = MessageTracker.getMsgs();

        assertEquals(msgs.size(), 3);
        assertEquals("start tx", msgs.get(0));
        assertEquals("place order", msgs.get(1));
        assertEquals("commit tx", msgs.get(2));
    }

    @Test
    public void testAfterThrowing() throws Throwable {
        Method targetMethod = UserService.class.getMethod("placeOrderWithException");

        List<MethodInterceptor> interceptors = new ArrayList<>();
        interceptors.add(afterThrowingAdvice);
        interceptors.add(beforeAdvice);

        ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(
                service, targetMethod, new Object[0], interceptors
        );
        try {
            methodInvocation.proceed();
        } catch (Throwable throwable) {
            List<String> msgs = MessageTracker.getMsgs();
            assertEquals(2, msgs.size());
            assertEquals("start tx", msgs.get(0));
            assertEquals("rollback tx", msgs.get(1));
            return;
        }
        fail("No Exception thrown");
    }
}