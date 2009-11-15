package org.andromda.core.metafacade;


/**
 * Fake metafacade number 8 (just used for testing the MetafacadeMappings).
 *
 * @author Chad Brandon
 */
public class Metafacade8Impl
    extends MetafacadeBase
{
    /**
     * @param metaObject
     * @param context
     */
    public Metafacade8Impl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    private boolean propertyOne = true;

    public boolean getPropertyOne()
    {
        return propertyOne;
    }

    private String propertyTwo = "SomeValue";

    public String getPropertyTwo()
    {
        return propertyTwo;
    }
}