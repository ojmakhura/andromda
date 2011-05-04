package org.andromda.core.common;

import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;

/**
 * JUnit tests for {@link org.andromda.core.common.Introspector}
 *
 * @author Chad Brandon
 */
public class IntrospectorTest
    extends TestCase
{
    /**
     *
     */
    public void testGetProperty()
    {
        TestBean testBean = new TestBean();
        assertEquals(testBean.getStringProperty(), Introspector.instance().getProperty(testBean, "stringProperty"));
        assertEquals(testBean.getNestedBean().getStringProperty(), Introspector.instance().getProperty(testBean, "nestedBean.stringProperty"));
        try
        {
            Introspector.instance().getProperty(testBean, "intProperty");
            fail();
        }
        catch (IntrospectorException exception) {}
    }

    /**
     *
     */
    public void testSetProperty()
    {
        TestBean testBean = new TestBean();
        Introspector.instance().setProperty(testBean, "stringProperty", "A New String Value");
        assertEquals(testBean.getStringProperty(), "A New String Value");
        assertTrue(testBean.getBooleanProperty());
        Introspector.instance().setProperty(testBean, "booleanProperty", "false");
        assertFalse(testBean.getBooleanProperty());
        assertEquals(testBean.getNestedBean().getLongProperty(), 222222);
        Introspector.instance().setProperty(testBean, "nestedBean.longProperty", "9999");
        assertEquals(testBean.getNestedBean().getLongProperty(), 9999);
        assertEquals(testBean.getNestedBean().getNestedNestedBean().getIntProperty(), 5);
        Introspector.instance().setProperty(testBean, "nestedBean.nestedNestedBean.intProperty", "10");
        assertEquals(testBean.getNestedBean().getNestedNestedBean().getIntProperty(), 10);
        Introspector.instance().setProperty(testBean, "aPropertyName", "SomeValue");
        assertEquals(testBean.getAPropertyName(), "SomeValue");
        try
        {
            Introspector.instance().getProperty(testBean, "intProperty");
            fail();
        }
        catch (IntrospectorException exception) {}
    }

    /**
     *
     */
    public void testIsReadable()
    {
        TestBean testBean = new TestBean();
        assertTrue(Introspector.instance().isReadable(testBean, "stringProperty"));
        assertFalse(Introspector.instance().isWritable(testBean, "byteProperty"));
        assertTrue(Introspector.instance().isReadable(testBean, "nestedBean.stringProperty"));
        assertTrue(Introspector.instance().isWritable(testBean, "nestedBean.stringProperty"));
        assertFalse(Introspector.instance().isReadable(testBean, "nestedBean.intProperty"));
        assertFalse(Introspector.instance().isWritable(testBean, "nestedBean.intProperty"));
    }

    /**
     *
     */
    public void testConstainsValidProperty()
    {
        TestBean testBean = new TestBean();
        assertTrue(Introspector.instance().containsValidProperty(testBean, "booleanProperty", null));
        assertTrue(Introspector.instance().containsValidProperty(testBean, "nestedBean.booleanProperty", "false"));
        assertFalse(Introspector.instance().containsValidProperty(testBean, "nestedBean.emptyCollectionProperty", null));
        assertTrue(Introspector.instance().containsValidProperty(testBean, "nestedBean.nonEmptyCollectionProperty", null));
        assertTrue(Introspector.instance().containsValidProperty(testBean, "aBCProperty", "true"));
    }

    private static final class TestBean
    {
        private boolean abcProperty = true;
        private boolean booleanProperty = true;
        private Integer integerProperty = 1;
        private String stringProperty = "TestBean";
        private int intProperty = 5;
        private long longProperty = 1111111111;
        private byte byteProperty = 1;
        private NestedBean nestedBean = new NestedBean();
        private String aPropertyName;
        @SuppressWarnings("unused")
        public boolean isABCProperty()
        {
            return abcProperty;
        }
        @SuppressWarnings("unused")
        public void setABCProperty(boolean abcProperty)
        {
            this.abcProperty = abcProperty;
        }
        @SuppressWarnings("unused")
        public boolean isBooleanProperty()
        {
            return booleanProperty;
        }
        @SuppressWarnings("unused")
        public void setBooleanProperty(boolean booleanProperty)
        {
            this.booleanProperty = booleanProperty;
        }
        protected boolean getBooleanProperty()
        {
            return this.booleanProperty;
        }
        @SuppressWarnings("unused")
        public byte getByteProperty()
        {
            return byteProperty;
        }
        @SuppressWarnings("unused")
        protected void setByteProperty(byte byteProperty)
        {
            this.byteProperty = byteProperty;
        }
        @SuppressWarnings("unused")
        protected Integer getIntegerProperty()
        {
            return integerProperty;
        }
        @SuppressWarnings("unused")
        protected void setIntegerProperty(Integer integerProperty)
        {
            this.integerProperty = integerProperty;
        }
        @SuppressWarnings("unused")
        protected int getIntProperty()
        {
            return intProperty;
        }
        @SuppressWarnings("unused")
        protected void setIntProperty(int intProperty)
        {
            this.intProperty = intProperty;
        }
        @SuppressWarnings("unused")
        protected long getLongProperty()
        {
            return longProperty;
        }
        @SuppressWarnings("unused")
        protected void setLongProperty(long longProperty)
        {
            this.longProperty = longProperty;
        }
        public String getStringProperty()
        {
            return stringProperty;
        }
        @SuppressWarnings("unused")
        public void setStringProperty(String stringProperty)
        {
            this.stringProperty = stringProperty;
        }
        public NestedBean getNestedBean()
        {
            return nestedBean;
        }
        public String getAPropertyName()
        {
            return aPropertyName;
        }
        @SuppressWarnings("unused")
        public void setAPropertyName(String propertyName)
        {
            aPropertyName = propertyName;
        }
    }

    private static final class NestedBean
    {
        private boolean booleanProperty = false;
        private Integer integerProperty = 10;
        private String stringProperty = "NestedBean";
        private int intProperty = 54;
        private long longProperty = 222222;
        private byte byteProperty = 2;
        private NestedNestedBean nestedNestedBean = new NestedNestedBean();
        private Collection<Object> emptyCollectionProperty = new ArrayList<Object>();
        private Collection<String> nonEmptyCollectionProperty = new ArrayList<String>();
        @SuppressWarnings("unused")
        public boolean isBooleanProperty()
        {
            return booleanProperty;
        }
        @SuppressWarnings("unused")
        protected void setBooleanProperty(boolean booleanProperty)
        {
            this.booleanProperty = booleanProperty;
        }
        @SuppressWarnings("unused")
        protected byte getByteProperty()
        {
            return byteProperty;
        }
        @SuppressWarnings("unused")
        protected void setByteProperty(byte byteProperty)
        {
            this.byteProperty = byteProperty;
        }
        @SuppressWarnings("unused")
        protected Integer getIntegerProperty()
        {
            return integerProperty;
        }
        @SuppressWarnings("unused")
        protected void setIntegerProperty(Integer integerProperty)
        {
            this.integerProperty = integerProperty;
        }
        @SuppressWarnings("unused")
        protected int getIntProperty()
        {
            return intProperty;
        }
        @SuppressWarnings("unused")
        protected void setIntProperty(int intProperty)
        {
            this.intProperty = intProperty;
        }
        public long getLongProperty()
        {
            return longProperty;
        }
        @SuppressWarnings("unused")
        public void setLongProperty(long longProperty)
        {
            this.longProperty = longProperty;
        }
        public String getStringProperty()
        {
            return stringProperty;
        }
        @SuppressWarnings("unused")
        public void setStringProperty(String stringProperty)
        {
            this.stringProperty = stringProperty;
        }
        public NestedNestedBean getNestedNestedBean()
        {
            return nestedNestedBean;
        }
        @SuppressWarnings("unused")
        public Collection<Object> getEmptyCollectionProperty()
        {
            return emptyCollectionProperty;
        }
        @SuppressWarnings("unused")
        public Collection<String> getNonEmptyCollectionProperty()
        {
            this.nonEmptyCollectionProperty.add("A String");
            return nonEmptyCollectionProperty;
        }
    }

    private static final class NestedNestedBean
    {
        private int intProperty = 5;
        public int getIntProperty()
        {
            return this.intProperty;
        }
        @SuppressWarnings("unused")
        public void setIntProperty(int intProperty)
        {
            this.intProperty = intProperty;
        }
    }
}