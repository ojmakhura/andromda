package org.andromda.cartridges.ejb3.metafacades;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3ValueObjectFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3ValueObjectFacade
 */
public class EJB3ValueObjectFacadeLogicImpl
    extends EJB3ValueObjectFacadeLogic
{

    public EJB3ValueObjectFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ValueObjectFacade#isSeamComponent()
     */
    protected boolean handleIsSeamComponent()
    {
        return EJB3MetafacadeUtils.isSeamComponent(this);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ValueObjectFacade#getSeamComponentName()
     */
    protected String handleGetSeamComponentName()
    {
        return EJB3MetafacadeUtils.getSeamComponentName(this);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ValueObjectFacade#getSeamComponentScopeType()
     */
    protected String handleGetSeamComponentScopeType()
    {
        return EJB3MetafacadeUtils.getSeamComponentScopeType(this, false);
    }

}