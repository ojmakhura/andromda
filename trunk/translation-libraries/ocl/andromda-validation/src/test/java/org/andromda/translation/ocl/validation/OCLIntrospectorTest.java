package org.andromda.translation.ocl.validation;

import junit.framework.TestCase;

/**
 * Tests the OCLIntrospector
 *
 * @author Chad Brandon
 */
public class OCLIntrospectorTest extends TestCase
{

    private OCLIntrospectorTestObject object = new OCLIntrospectorTestObject();

    /**
     * Constructor for OCLIntrospectorTest.
     *
     * @param name
     */
    public OCLIntrospectorTest(String name)
    {
        super(name);
    }

    /**
     * Class under test for Object invoke(Object, String)
     */
    public void testInvokeObjectString()
    {
        OCLIntrospector.invoke(object, "methodOne()");
        String methodName = "methodTwo";
        assertEquals(OCLIntrospector.invoke(object, "methodTwo()"), methodName);
        assertEquals(OCLIntrospectorTestObject.propertyOne, OCLIntrospector.invoke(object, "propertyOne"));
        assertEquals(OCLIntrospectorTestObject.propertyTwo, OCLIntrospector.invoke(object, "propertyTwo"));
    }

    /**
     * Class under test for Object invoke(Object, String, Object[])
     */
    public void testInvokeObjectStringObjectArray()
    {
        OCLIntrospector.invoke(object, "methodThree (argOne) ", new Object[]{"argOne"});
        String stringValue = "argOne";
        assertEquals(OCLIntrospector.invoke(object, "methodFour( argOne )", new Object[]{stringValue}), stringValue);
        Integer integerValue = 76;
        assertEquals(OCLIntrospector.invoke(object, "methodFive(argOne, argTwo)",
                new Object[]{stringValue, integerValue}), integerValue);
    }

}