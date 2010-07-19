package org.andromda.translation.ocl.validation;

/**
 * A simple object used with OCLIntrospectorTest
 *
 * @author Chad Brandon
 */
public class OCLIntrospectorTestObject
{
    /**
     * 
     */
    public void methodOne()
    {
    }

    /**
     * @return "methodTwo"
     */
    public String methodTwo()
    {
        return "methodTwo";
    }

    /**
     * @param argOne
     */
    public void methodThree(String argOne)
    {
    }

    /**
     * @param argOne
     * @return argOne
     */
    public String methodFour(String argOne)
    {
        return argOne;
    }

    /**
     * @param argOne
     * @param argTwo
     * @return argTwo
     */
    public Integer methodFive(String argOne, Integer argTwo)
    {
        return argTwo;
    }

    /**
     * 
     */
    protected static String propertyOne = "One";

    /**
     * @return propertyOne
     */
    public String getPropertyOne()
    {
        return propertyOne;
    }

    /**
     * 
     */
    protected static Integer propertyTwo = 2;

    /**
     * @return propertyTwo
     */
    public Integer getPropertyTwo()
    {
        return propertyTwo;
    }
}
