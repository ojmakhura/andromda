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

}