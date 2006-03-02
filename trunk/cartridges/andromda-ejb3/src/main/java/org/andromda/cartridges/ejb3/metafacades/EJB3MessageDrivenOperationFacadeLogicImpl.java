package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Profile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenOperationFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenOperationFacade
 */
public class EJB3MessageDrivenOperationFacadeLogicImpl
    extends EJB3MessageDrivenOperationFacadeLogic
{

    public EJB3MessageDrivenOperationFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenOperationFacade#isPostConstruct()
     */
    protected boolean handleIsPostConstruct()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_POST_CONSTRUCT);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenOperationFacade#isPreDestroy()
     */
    protected boolean handleIsPreDestroy()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_PRE_DESTROY);
    }

}