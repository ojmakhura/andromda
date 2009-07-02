package org.andromda.core.common;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;


/**
 * JUnit tests for {@link org.andromda.common.Introspector}
 *
 * @author Chad Brandon
 */
public class IntrospectorTest
    extends TestCase
{
    
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
        public boolean isABCProperty()
        {
            return abcProperty;
        }
        public void setABCProperty(boolean abcProperty)
        {
            this.abcProperty = abcProperty;
        }
        public boolean isBooleanProperty()
        {
            return booleanProperty;
        }
        public void setBooleanProperty(boolean booleanProperty)
        {
            this.booleanProperty = booleanProperty;
        }
        protected boolean getBooleanProperty()
        {
            return this.booleanProperty;
        }
        public byte getByteProperty()
        {
            return byteProperty;
        }
        protected void setByteProperty(byte byteProperty)
        {
            this.byteProperty = byteProperty;
        }
        protected Integer getIntegerProperty()
        {
            return integerProperty;
        }
        protected void setIntegerProperty(Integer integerProperty)
        {
            this.integerProperty = integerProperty;
        }
        protected int getIntProperty()
        {
            return intProperty;
        }
        protected void setIntProperty(int intProperty)
        {
            this.intProperty = intProperty;
        }
        protected long getLongProperty()
        {
            return longProperty;
        }
        protected void setLongProperty(long longProperty)
        {
            this.longProperty = longProperty;
        }
        public String getStringProperty()
        {
            return stringProperty;
        }
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
        private Collection emptyCollectionProperty = new ArrayList();
        private Collection nonEmptyCollectionProperty = new ArrayList();
        public boolean isBooleanProperty()
        {
            return booleanProperty;
        }
        protected void setBooleanProperty(boolean booleanProperty)
        {
            this.booleanProperty = booleanProperty;
        }
        protected byte getByteProperty()
        {
            return byteProperty;
        }
        protected void setByteProperty(byte byteProperty)
        {
            this.byteProperty = byteProperty;
        }
        protected Integer getIntegerProperty()
        {
            return integerProperty;
        }
        protected void setIntegerProperty(Integer integerProperty)
        {
            this.integerProperty = integerProperty;
        }
        protected int getIntProperty()
        {
            return intProperty;
        }
        protected void setIntProperty(int intProperty)
        {
            this.intProperty = intProperty;
        }
        public long getLongProperty()
        {
            return longProperty;
        }
        public void setLongProperty(long longProperty)
        {
            this.longProperty = longProperty;
        }
        public String getStringProperty()
        {
            return stringProperty;
        }
        public void setStringProperty(String stringProperty)
        {
            this.stringProperty = stringProperty;
        }
        public NestedNestedBean getNestedNestedBean()
        {
            return nestedNestedBean;
        }
        public Collection getEmptyCollectionProperty()
        {
            return emptyCollectionProperty;
        }
        public Collection getNonEmptyCollectionProperty()
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
        public void setIntProperty(int intProperty)
        {
            this.intProperty = intProperty;
        }
    }  
}