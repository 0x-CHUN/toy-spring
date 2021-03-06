package spring.beans;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeConverterTest {
    @Test
    public void testConvertStringToInt() {

        TypeConverter converter = new SimpleTypeConverter();
        Integer i = converter.convertIfNecessary("3", Integer.class);
        Assert.assertEquals(3, i.intValue());

        try {
            converter.convertIfNecessary("3.1", Integer.class);
        } catch (Exception e) {
            return;
        }
        fail();
    }

    @Test
    public void testConvertStringToBoolean() {
        TypeConverter converter = new SimpleTypeConverter();
        Boolean b = converter.convertIfNecessary("true", Boolean.class);
        Assert.assertEquals(true, b.booleanValue());

        try {
            converter.convertIfNecessary("xxxyyyzzz", Boolean.class);
        } catch (Exception e) {
            return;
        }
        fail();
    }
}