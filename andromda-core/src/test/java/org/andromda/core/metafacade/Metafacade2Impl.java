package org.andromda.core.metafacade;


/**
 * Fake metafacade number 2 (just used for testing the MetafacadeMappings).
 *
 * @author Chad Brandon
 */
public class Metafacade2Impl
    extends MetafacadeBase
{
    /**
     * @param metaObject
     * @param context
     */
    public Metafacade2Impl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    private boolean mappingProperty = true;

    public boolean isMappingProperty()
    {
        return mappingProperty;
    }
}