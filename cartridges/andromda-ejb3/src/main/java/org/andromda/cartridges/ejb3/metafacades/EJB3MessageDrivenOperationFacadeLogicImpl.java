package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Profile;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenOperationFacade.
 *
 * @see EJB3MessageDrivenOperationFacade
 */
public class EJB3MessageDrivenOperationFacadeLogicImpl
    extends EJB3MessageDrivenOperationFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public EJB3MessageDrivenOperationFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    /**
     * @see EJB3MessageDrivenOperationFacade#isPostConstruct()
     */
    @Override
    protected boolean handleIsPostConstruct()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_POST_CONSTRUCT);
    }

    /**
     * @see EJB3MessageDrivenOperationFacade#isPreDestroy()
     */
    @Override
    protected boolean handleIsPreDestroy()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_PRE_DESTROY);
    }
}
