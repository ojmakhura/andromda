package org.andromda.core.metafacade;


/**
 * Fake metafacade number 5 (just used for testing the MetafacadeMappings).
 *
 * @author Chad Brandon
 */
public class Metafacade5Impl
    extends MetafacadeBase
{
    /**
     * @param metaObject
     * @param context
     */
    public Metafacade5Impl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    public boolean isProperty()
    {
        return false;
    }
}