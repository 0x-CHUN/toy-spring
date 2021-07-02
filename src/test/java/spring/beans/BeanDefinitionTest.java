package spring.beans;

import org.junit.Test;
import spring.aop.aspectj.AspectJBeforeAdvice;
import spring.aop.aspectj.AspectJExpressionPointcut;
import spring.aop.config.AspectInstanceFactory;
import spring.aop.config.MethodLocatingFactory;
import spring.beans.factory.config.RuntimeBeanReference;
import spring.beans.factory.support.DefaultBeanFactory;
import spring.contex.support.AbstractApplicationContextTest;
import spring.transaction.TransactionManager;

import java.util.List;

import static org.junit.Assert.*;

public class BeanDefinitionTest extends AbstractApplicationContextTest {
    @Test
    public void testAOPBean() {
        DefaultBeanFactory factory = (DefaultBeanFactory) this.getBeanFactory("bean-v5.xml");
        {
            BeanDefinition definition = factory.getBeanDefinition("tx");
            assertNotNull(definition);
            assertEquals(definition.getBeanClassName(), TransactionManager.class.getName());
        }
        {
            BeanDefinition definition = factory.getBeanDefinition("placeOrder");
            assertNotNull(definition);
            assertTrue(definition.isSynthetic());
            assertEquals(definition.getBeanClass(), AspectJExpressionPointcut.class);

            PropertyValue propertyValue = definition.getPropertyValues().get(0);
            assertEquals("expression", propertyValue.getName());
            assertEquals("execution(* spring.service.aop.*.placeOrder(..))", propertyValue.getValue());
        }
        {
            String name = AspectJBeforeAdvice.class.getName() + "#0";
            BeanDefinition bd = factory.getBeanDefinition(name);
            assertTrue(bd.getBeanClass().equals(AspectJBeforeAdvice.class));
            assertTrue(bd.isSynthetic());
            List<ConstructorArgument.ValueHolder> args = bd.getConstructorArgument().getArgumentValues();
            assertEquals(3, args.size());


            {
                BeanDefinition innerBeanDef = (BeanDefinition) args.get(0).getValue();
                assertTrue(innerBeanDef.isSynthetic());
                assertTrue(innerBeanDef.getBeanClass().equals(MethodLocatingFactory.class));
                List<PropertyValue> pvs = innerBeanDef.getPropertyValues();
                assertEquals("targetBeanName", pvs.get(0).getName());
                assertEquals("tx", pvs.get(0).getValue());
                assertEquals("methodName", pvs.get(1).getName());
                assertEquals("start", pvs.get(1).getValue());
            }

            {
                RuntimeBeanReference ref = (RuntimeBeanReference) args.get(1).getValue();
                assertEquals("placeOrder", ref.getBeanName());
            }

            {
                BeanDefinition innerBeanDef = (BeanDefinition) args.get(2).getValue();
                assertTrue(innerBeanDef.isSynthetic());
                assertTrue(innerBeanDef.getBeanClass().equals(AspectInstanceFactory.class));

                List<PropertyValue> pvs = innerBeanDef.getPropertyValues();
                assertEquals("aspectBeanName", pvs.get(0).getName());
                assertEquals("tx", pvs.get(0).getValue());
            }
        }
    }
}