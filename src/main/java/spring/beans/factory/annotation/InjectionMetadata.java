package spring.beans.factory.annotation;

import java.util.List;

public class InjectionMetadata {
    private List<InjectionElement> injectionElements;

    public InjectionMetadata(List<InjectionElement> injectionElements) {
        this.injectionElements = injectionElements;
    }

    public List<InjectionElement> getInjectionElements() {
        return injectionElements;
    }

    public void inject(Object target) {
        if (injectionElements == null || injectionElements.isEmpty())
            return;
        for (InjectionElement element : injectionElements) {
            element.inject(target);
        }
    }
}
