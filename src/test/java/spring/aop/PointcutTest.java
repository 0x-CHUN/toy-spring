package spring.aop;

import org.junit.Test;
import spring.aop.aspectj.AspectJExpressionPointcut;
import spring.service.UserService;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class PointcutTest {
    @Test
    public void testPointcut() throws NoSuchMethodException {
        String expression = "execution(* spring.service.*.getVersion(..))";
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        MethodMatcher methodMatcher = pointcut.getMethodMatcher();
        {
            Class<?> targetClass = UserService.class;
            Method method1 = targetClass.getMethod("getAccountDao");
            assertFalse(methodMatcher.matches(method1));
            Method method2 = targetClass.getMethod("getVersion");
            assertTrue(methodMatcher.matches(method2));
        }
    }
}