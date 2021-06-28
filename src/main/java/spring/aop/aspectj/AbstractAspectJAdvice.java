package spring.aop.aspectj;

import spring.aop.Advice;
import spring.aop.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractAspectJAdvice implements Advice {
    protected Method adviceMethod;
    protected AspectJExpressionPointcut pointcut;
    protected Object adviceObject;

    public AbstractAspectJAdvice(
            Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject
    ) {
        this.adviceMethod = adviceMethod;
        this.pointcut = pointcut;
        this.adviceObject = adviceObject;
    }

    public void invokeAdviceMethod() throws InvocationTargetException, IllegalAccessException {
        this.adviceMethod.invoke(this.adviceObject);
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    public Method getAdviceMethod() {
        return adviceMethod;
    }

}
