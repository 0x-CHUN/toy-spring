package spring.aop.framework;

import org.junit.Before;
import org.junit.Test;
import spring.aop.aspectj.AspectJAfterReturningAdvice;
import spring.aop.aspectj.AspectJBeforeAdvice;
import spring.aop.aspectj.AspectJExpressionPointcut;
import spring.aop.config.AopConfig;
import spring.aop.config.AspectInstanceFactory;
import spring.beans.factory.BeanFactory;
import spring.contex.support.AbstractApplicationContextTest;
import spring.service.aop.UserService;
import spring.transaction.TransactionManager;
import spring.util.MessageTracker;

import java.util.List;

import static org.junit.Assert.*;

public class CglibProxyFactoryTest extends AbstractApplicationContextTest {
    private AspectJBeforeAdvice beforeAdvice = null;
    private AspectJAfterReturningAdvice afterAdvice = null;
    private AspectJExpressionPointcut pointcut = null;
    private BeanFactory factory = null;
    private AspectInstanceFactory aspectInstanceFactory = null;

    @Before
    public void setup() throws NoSuchMethodException {
        MessageTracker.clearMsgs();
        String expression = "execution(* spring.service.aop.*.placeOrder(..))";
        pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        factory = this.getBeanFactory("bean-v5.xml");
        aspectInstanceFactory = this.getAspectInstanceFactory("tx");
        aspectInstanceFactory.setBeanFactory(factory);
        beforeAdvice = new AspectJBeforeAdvice(
                TransactionManager.class.getMethod("start"),
                pointcut,
                aspectInstanceFactory);

        afterAdvice = new AspectJAfterReturningAdvice(
                TransactionManager.class.getMethod("commit"),
                pointcut,
                aspectInstanceFactory);
    }


    @Test
    public void testGetProxy() {
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