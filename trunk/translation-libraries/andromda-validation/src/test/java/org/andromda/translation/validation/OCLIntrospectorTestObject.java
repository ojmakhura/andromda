package org.andromda.translation.validation;

/**
 * A simple object used with OCLIntrospectorTest
 * @author Chad Brandon
 */
public class OCLIntrospectorTestObject
{
    public void methodOne() 
    {}
    
    public String methodTwo() 
    {
        return "methodTwo";
    }
    
    public void methodThree(String argOne) 
    {}
    
    public String methodFour(String argOne) 
    {
        return argOne;
    }
    
    public Integer methodFive(String argOne, Integer argTwo) 
    {
        return argTwo;
    }
    
    protected static String propertyOne = "One";
    
    public String getPropertyOne() 
    {
        return propertyOne;
    }
    
    protected static Integer propertyTwo = new Integer(2);
    
    public Integer getPropertyTwo() 
    {
        return propertyTwo;
    }
    
}
