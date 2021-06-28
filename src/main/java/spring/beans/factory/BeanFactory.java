package spring.beans.factory;

public interface BeanFactory {
    /**
     * Get bean by beanId
     *
     * @param beanId
     * @return object
     */
    Object getBean(String beanId);

    /**
     * Get type
     *
     * @param name
     * @return Class<?>
     */
    Class<?> getType(String name);
}
