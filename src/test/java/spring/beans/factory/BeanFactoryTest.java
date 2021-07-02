package spring.beans.factory;

import org.junit.Assert;
import org.junit.Test;
import spring.aop.Advice;
import spring.aop.aspectj.AspectJAfterReturningAdvice;
import spring.aop.aspectj.AspectJAfterThrowingAdvice;
import spring.aop.aspectj.AspectJBeforeAdvice;
import spring.aop.framework.AopConfigException;
import spring.contex.support.AbstractApplicationContextTest;
import spring.transaction.TransactionManager;

import java.util.List;

import static org.junit.Assert.*;

public class BeanFactoryTest extends AbstractApplicationContextTest {

    static String expectedExpression = "execution(* spring.service.aop.*.placeOrder(..))";

    @Test
    public void testGetBeanByType() throws Exception {
        BeanFactory factory = this.getBeanFactory("bean-v5.xml");
        assertNotNull(factory);
        List<Object> advices = factory.getBeansByType(Advice.class);
        assertEquals(3, advices.size());
        {
            AspectJBeforeAdvice advice = (AspectJBeforeAdvice)
                    this.getAdvice(AspectJBeforeAdvice.class, advices);
            assertEquals(TransactionManager.class.getMethod("start"), advice.getAdviceMethod());
            assertEquals(expectedExpression, advice.getPointcut().getExpression());
            assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());
        }
        {
            AspectJAfterReturningAdvice advice = (AspectJAfterReturningAdvice)
                    this.getAdvice(AspectJAfterReturningAdvice.class, advices);
            assertEquals(TransactionManager.class.getMethod("commit"), advice.getAdviceMethod());
            assertEquals(expectedExpression, advice.getPointcut().getExpression());
            assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());
        }
        {
            AspectJAfterThrowingAdvice advice = (AspectJAfterThrowingAdvice)
                    this.getAdvice(AspectJAfterThrowingAdvice.class, advices);
            assertEquals(TransactionManager.class.getMethod("rollback"), advice.getAdviceMethod());
            assertEquals(expectedExpression, advice.getPointcut().getExpression());
            assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());
        }
    }

    public Object getAdvice(Class<?> type, List<Object> advices) {
        for (Object o : advices) {
            if (o.getClass().equals(type)) {
                return o;
            }
        }
        return null;
    }
}