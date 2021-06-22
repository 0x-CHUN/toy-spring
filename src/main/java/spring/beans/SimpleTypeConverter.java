package spring.beans;

import spring.beans.propertyeditors.CustomBooleanEditor;
import spring.beans.propertyeditors.CustomNumberEditor;
import spring.util.ClassUtils;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

public class SimpleTypeConverter implements TypeConverter {
    private Map<Class<?>, PropertyEditor> defaultEditors;

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType) {
        if (ClassUtils.isAssignableValue(requiredType, value)) { // if value is assignable, just cast it
            return (T) value;
        } else {
            if (value instanceof String) { // value is string e.g "1", "yes"
                PropertyEditor editor = findDefaultEditor(requiredType);
                try {
                    editor.setAsText((String) value); // setAsText:  Set the property value by parsing a given String.
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("type miss exception");
                }
                return (T) editor.getValue(); // cast and return the value
            } else {
                throw new RuntimeException("Todo : can't convert value for " + value + " class:" + requiredType);
            }
        }
    }

    private PropertyEditor findDefaultEditor(Class<?> requiredType) {
        PropertyEditor editor = this.getDefaultEditor(requiredType);
        if (editor == null) {
            throw new RuntimeException("Editor for " + requiredType + " has not been implemented");
        }
        return editor;
    }

    public PropertyEditor getDefaultEditor(Class<?> requiredType) {

        if (this.defaultEditors == null) {
            createDefaultEditors();
        }
        return this.defaultEditors.get(requiredType);
    }

    private void createDefaultEditors() {
        this.defaultEditors = new HashMap<Class<?>, PropertyEditor>(64);

        // Spring's CustomBooleanEditor accepts more flag values than the JDK's default editor.
        this.defaultEditors.put(boolean.class, new CustomBooleanEditor(false));
        this.defaultEditors.put(Boolean.class, new CustomBooleanEditor(true));

        // The JDK does not contain default editors for number wrapper types!
        // Override JDK primitive number editors with our own CustomNumberEditor.
		/*this.defaultEditors.put(byte.class, new CustomNumberEditor(Byte.class, false));
		this.defaultEditors.put(Byte.class, new CustomNumberEditor(Byte.class, true));
		this.defaultEditors.put(short.class, new CustomNumberEditor(Short.class, false));
		this.defaultEditors.put(Short.class, new CustomNumberEditor(Short.class, true));*/
        this.defaultEditors.put(int.class, new CustomNumberEditor(Integer.class, false));
        this.defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, true));
		/*this.defaultEditors.put(long.class, new CustomNumberEditor(Long.class, false));
		this.defaultEditors.put(Long.class, new CustomNumberEditor(Long.class, true));
		this.defaultEditors.put(float.class, new CustomNumberEditor(Float.class, false));
		this.defaultEditors.put(Float.class, new CustomNumberEditor(Float.class, true));
		this.defaultEditors.put(double.class, new CustomNumberEditor(Double.class, false));
		this.defaultEditors.put(Double.class, new CustomNumberEditor(Double.class, true));
		this.defaultEditors.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
		this.defaultEditors.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));*/


    }
}
