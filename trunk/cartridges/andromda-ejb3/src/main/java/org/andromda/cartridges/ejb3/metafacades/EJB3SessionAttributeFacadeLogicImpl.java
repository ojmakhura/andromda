package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Profile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacade
 */
public class EJB3SessionAttributeFacadeLogicImpl
    extends EJB3SessionAttributeFacadeLogic
{

    // ---------------- constructor -------------------------------
	
    public EJB3SessionAttributeFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // ---------------- methods -------------------------------
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacade#getTransactionType()
     */
    protected java.lang.String handleGetTransactionType()
    {
    	return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_TYPE, true);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamComponentIn()
     */
    protected boolean handleIsSeamComponentIn()
    {
        boolean isSeamComponentIn = false;
        if (this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_COMPONENT_IN))
        {
            isSeamComponentIn = true;
        }
        return isSeamComponentIn;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamComponentOut()
     */
    protected boolean handleIsSeamComponentOut()
    {
        boolean isSeamComponentOut = false;
        if (this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_COMPONENT_OUT))
        {
            isSeamComponentOut = true;
        }
        return isSeamComponentOut;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamLifecyleCreate()
     */
    protected boolean handleIsSeamLifecyleCreate()
    {
        boolean isSeamLifecycleCreate = false;
        if (this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_LIFECYCLE_CREATE))
        {
            isSeamLifecycleCreate = true;
        }
        return isSeamLifecycleCreate;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamLifecycleDestroy()
     */
    protected boolean handleIsSeamLifecycleDestroy()
    {
        boolean isSeamLifecycleCreate = false;
        if (this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_LIFECYCLE_DESTROY))
        {
            isSeamLifecycleCreate = true;
        }
        return isSeamLifecycleCreate;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamValidationValid()
     */
    protected boolean handleIsSeamValidationValid()
    {
        boolean isSeamValidComponent = false;
        if (this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_VALIDATION_VALID))
        {
            isSeamValidComponent = true;
        }
        return isSeamValidComponent;
    }

}