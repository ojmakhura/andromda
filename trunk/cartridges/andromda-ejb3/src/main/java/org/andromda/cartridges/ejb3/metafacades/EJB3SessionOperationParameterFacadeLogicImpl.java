package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Profile;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationParameterFacade.
 *
 * @see EJB3SessionOperationParameterFacade
 */
public class EJB3SessionOperationParameterFacadeLogicImpl
    extends EJB3SessionOperationParameterFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public EJB3SessionOperationParameterFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    /**
     * @see EJB3SessionOperationParameterFacade#isSeamAsynchronousDuration()
     */
    @Override
    protected boolean handleIsSeamAsynchronousDuration()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_ASYNCHRONOUS_DURATION);
    }

    /**
     * @see EJB3SessionOperationParameterFacade#isSeamAsynchronousExpiration()
     */
    @Override
    protected boolean handleIsSeamAsynchronousExpiration()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_ASYNCHRONOUS_EXPIRATION);
    }

    /**
     * @see EJB3SessionOperationParameterFacade#isSeamAsynchronousIntervalDuration()
     */
    @Override
    protected boolean handleIsSeamAsynchronousIntervalDuration()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_ASYNCHRONOUS_INTERVAL_DURATION);
    }
}
