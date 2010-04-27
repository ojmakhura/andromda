package org.andromda.cartridges.ejb3.metafacades;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3ValueObjectFacade.
 *
 * @see EJB3ValueObjectFacade
 */
public class EJB3ValueObjectFacadeLogicImpl
    extends EJB3ValueObjectFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public EJB3ValueObjectFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    /**
     * @see EJB3ValueObjectFacade#isSeamComponent()
     */
    @Override
    protected boolean handleIsSeamComponent()
    {
        return EJB3MetafacadeUtils.isSeamComponent(this);
    }

    /**
     * @see EJB3ValueObjectFacade#getSeamComponentName()
     */
    @Override
    protected String handleGetSeamComponentName()
    {
        return EJB3MetafacadeUtils.getSeamComponentName(this);
    }

    /**
     * @see EJB3ValueObjectFacade#getSeamComponentScopeType()
     */
    @Override
    protected String handleGetSeamComponentScopeType()
    {
        return EJB3MetafacadeUtils.getSeamComponentScopeType(this, false);
    }
}
