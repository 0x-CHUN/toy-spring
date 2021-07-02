package spring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import spring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

public class AspectJBeforeAdvice extends AbstractAspectJAdvice {

    public AspectJBeforeAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod, pointcut, adviceObjectFactory);
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        this.invokeAdviceMethod();
        return methodInvocation.proceed();
    }


}