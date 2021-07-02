package spring;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import spring.aop.CGlibTest;
import spring.aop.PointcutTest;
import spring.aop.config.MethodLocatingFactoryTest;
import spring.aop.framework.CglibProxyFactoryTest;
import spring.aop.framework.ReflectiveMethodInvocationTest;
import spring.beans.BeanDefinitionTest;
import spring.beans.TypeConverterTest;
import spring.beans.factory.BeanFactoryTest;
import spring.beans.factory.annotation.AutowiredAnnotationProcessorTest;
import spring.beans.factory.annotation.ClassPathBeanDefinitionScannerTest;
import spring.beans.factory.annotation.InjectionMetadataTest;
import spring.beans.factory.config.DependencyDescriptorTest;
import spring.beans.factory.xml.XmlBeanDefinitionReaderTest;
import spring.beans.propertyeditors.CustomBooleanEditorTest;
import spring.beans.propertyeditors.CustomNumberEditorTest;
import spring.contex.support.AbstractApplicationContextTest;
import spring.contex.support.ClassPathXmlApplicationContextTest;
import spring.contex.support.FileSystemApplicationContextTest;
import spring.context.ApplicationContextTest;
import spring.core.io.ClassPathResourceTest;
import spring.core.io.FileSystemResourceTest;
import spring.core.io.support.PackageResourceLoaderTest;
import spring.core.type.ClassMetadataTest;
import spring.core.type.classreading.SimpleMetadataReaderTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MethodLocatingFactoryTest.class,
        CglibProxyFactoryTest.class,
        ReflectiveMethodInvocationTest.class,
        CGlibTest.class,
        PointcutTest.class,
        AutowiredAnnotationProcessorTest.class,
        ClassPathBeanDefinitionScannerTest.class,
        InjectionMetadataTest.class,
        DependencyDescriptorTest.class,
        XmlBeanDefinitionReaderTest.class,
        BeanFactoryTest.class,
        CustomBooleanEditorTest.class,
        CustomNumberEditorTest.class,
        BeanDefinitionTest.class,
        TypeConverterTest.class,
        AbstractApplicationContextTest.class,
        ClassPathXmlApplicationContextTest.class,
        FileSystemApplicationContextTest.class,
        ApplicationContextTest.class,
        PackageResourceLoaderTest.class,
        ClassPathResourceTest.class,
        FileSystemResourceTest.class,
        SimpleMetadataReaderTest.class,
        ClassMetadataTest.class,
})
public class AllTest {
}
