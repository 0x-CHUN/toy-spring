package spring.aop.framework;

import org.junit.Before;
import org.junit.Test;
import spring.aop.aspectj.AspectJAfterReturningAdvice;
import spring.aop.aspectj.AspectJBeforeAdvice;
import spring.aop.aspectj.AspectJExpressionPointcut;
import spring.aop.config.AopConfig;
import spring.service.aop.UserService;
import spring.transaction.TransactionManager;
import spring.util.MessageTracker;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.*;

public class CglibProxyFactoryTest {
    private static AspectJBeforeAdvice beforeAdvice = null;
    private static AspectJAfterReturningAdvice afterAdvice = null;

    @Before
    public void setup() throws Exception {
        MessageTracker.clearMsgs();

        TransactionManager transactionManager = new TransactionManager();
        String expression = "execution(* spring.service.aop.*.placeOrder(..))";
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        beforeAdvice = new AspectJBeforeAdvice(
                TransactionManager.class.getMethod("start"),
                pointcut,
                transactionManager);

        afterAdvice = new AspectJAfterReturningAdvice(
                TransactionManager.class.getMethod("commit"),
                pointcut,
                transactionManager);

    }


    @Test
    public void testGetProxy() throws AopConfigException {
        AopConfig config = new AopConfigSupport();
        config.addAdvice(beforeAdvice);
        config.addAdvice(afterAdvice);
        config.setTargetObject(new UserService());
        CglibProxyFactory proxyFactory = new CglibProxyFactory(config);

        UserService proxy = (UserService) proxyFactory.getProxy();
        proxy.placeOrder();

        List<String> msgs = MessageTracker.getMsgs();
        assertEquals(3, msgs.size());
        assertEquals("start tx", msgs.get(0));
        assertEquals("place order", msgs.get(1));
        assertEquals("commit tx", msgs.get(2));
        proxy.toString();
    }
}