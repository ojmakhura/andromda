package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacade
 */
public class EJB3OperationFacadeLogicImpl
    extends EJB3OperationFacadeLogic
{

    // ---------------- constructor -------------------------------
	
    public EJB3OperationFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // ---------------- methods -------------------------------
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacade#getTransactionType()
     */
    protected java.lang.String handleGetTransactionType()
    {
    	String transType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_TYPE, true);
        if (StringUtils.isNotBlank(transType))
        {
            transType = EJB3MetafacadeUtils.convertTransactionType(transType);
        }
        else
        {
            transType = StringUtils.trimToEmpty(
                    ObjectUtils.toString(this.getConfiguredProperty(EJB3Globals.TRANSACTION_TYPE)));
        }
        return transType;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacade#isBusinessOperation()
     */
    protected boolean handleIsBusinessOperation()
    {
        return !this.hasStereotype(EJB3Profile.STEREOTYPE_CREATE_METHOD) &&
                !this.hasStereotype(EJB3Profile.STEREOTYPE_FINDER_METHOD) &&
                !this.hasStereotype(EJB3Profile.STEREOTYPE_SELECT_METHOD);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3OperationFacade#isSelectMethod()
     */
    protected boolean handleIsSelectMethod()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SELECT_METHOD);
    }

}