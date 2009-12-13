package org.andromda.core.metafacade;


/**
 * Fake metafacade number 7 (just used for testing the MetafacadeMappings).
 *
 * @author Chad Brandon
 */
public class Metafacade7Impl
    extends MetafacadeBase
{
    /**
     * @param metaObject
     * @param context
     */
    public Metafacade7Impl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    private boolean propertyOne;

    public boolean getPropertyOne()
    {
        return propertyOne;
    }

    private boolean propertyTwo = true;

    public boolean getPropertyTwo()
    {
        return propertyTwo;
    }
}