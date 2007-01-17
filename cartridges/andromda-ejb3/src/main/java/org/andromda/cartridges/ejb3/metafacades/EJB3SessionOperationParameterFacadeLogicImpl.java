package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Profile;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationParameterFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationParameterFacade
 */
public class EJB3SessionOperationParameterFacadeLogicImpl
    extends EJB3SessionOperationParameterFacadeLogic
{

    public EJB3SessionOperationParameterFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationParameterFacade#isSeamAsynchronousDuration()
     */
    protected boolean handleIsSeamAsynchronousDuration()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_ASYNCHRONOUS_DURATION);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationParameterFacade#isSeamAsynchronousExpiration()
     */
    protected boolean handleIsSeamAsynchronousExpiration()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_ASYNCHRONOUS_EXPIRATION);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationParameterFacade#isSeamAsynchronousIntervalDuration()
     */
    protected boolean handleIsSeamAsynchronousIntervalDuration()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_ASYNCHRONOUS_INTERVAL_DURATION);
    }

}