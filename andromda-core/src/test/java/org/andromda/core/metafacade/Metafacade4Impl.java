package org.andromda.core.metafacade;

/**
 * Fake metafacade number 4 (just used for testing the MetafacadeMappings).
 *
 * @author Chad Brandon
 */
public class Metafacade4Impl
    extends Metafacade2Impl
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public Metafacade4Impl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }
}
