package spring.aop.aspectj;

import org.aspectj.weaver.reflect.ReflectionWorld;
import org.aspectj.weaver.tools.*;
import spring.aop.MethodMatcher;
import spring.aop.Pointcut;
import spring.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.aspectj.weaver.tools.PointcutPrimitive.*;
import static spring.util.StringUtils.replace;

public class AspectJExpressionPointcut implements Pointcut, MethodMatcher {
    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

    static {
        SUPPORTED_PRIMITIVES.add(EXECUTION);
        SUPPORTED_PRIMITIVES.add(ARGS);
        SUPPORTED_PRIMITIVES.add(REFERENCE);
        SUPPORTED_PRIMITIVES.add(THIS);
        SUPPORTED_PRIMITIVES.add(TARGET);
        SUPPORTED_PRIMITIVES.add(WITHIN);
        SUPPORTED_PRIMITIVES.add(AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(AT_ARGS);
        SUPPORTED_PRIMITIVES.add(AT_TARGET);
    }

    private String expression;

    private PointcutExpression pointcutExpression;

    private ClassLoader classLoader;


    public AspectJExpressionPointcut() {
    }

    @Override
    public boolean matches(Method method) {
        checkReadyToMatch();
        ShadowMatch shadowMatch = getShadowMatch(method);
        return shadowMatch.alwaysMatches();
    }

    private ShadowMatch getShadowMatch(Method method) {
        ShadowMatch shadowMatch;
        try {
            shadowMatch = this.pointcutExpression.matchesMethodExecution(method);
        } catch (ReflectionWorld.ReflectionWorldException ex) {
            throw new RuntimeException("not implemented yet");
        }
        return shadowMatch;
    }

    private void checkReadyToMatch() {
        if (getExpression() == null) {
            throw new IllegalStateException("Must set property 'expression' before attempting to match");
        }
        if (this.pointcutExpression == null) {
            this.classLoader = ClassUtils.getDefaultClassLoader();
            this.pointcutExpression = buildPointcutExpression(this.classLoader);
        }
    }

    private PointcutExpression buildPointcutExpression(ClassLoader classLoader) {
        PointcutParser parser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                SUPPORTED_PRIMITIVES,
                classLoader
        );
        return parser.parsePointcutExpression(replaceBooleanOperators(expression), null, new PointcutParameter[0]);
    }

    private String replaceBooleanOperators(String expression) {
        String result = replace(expression, " and ", " && ");
        result = replace(result, " or ", " || ");
        result = replace(result, " not ", " ! ");
        return result;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression){
        this.expression = expression;
    }
}
