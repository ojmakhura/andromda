package org.andromda.cartridges.ejb.metafacades;

import org.andromda.cartridges.ejb.EJBGlobals;
import org.andromda.cartridges.ejb.EJBProfile;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.EJBOperationFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.EJBOperationFacade
 */
public class EJBOperationFacadeLogicImpl
        extends EJBOperationFacadeLogic
{
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public EJBOperationFacadeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBOperationFacade#getTransactionType()
     */
    protected String handleGetTransactionType()
    {
        String transactionType = (String)this.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE, true);
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = String.valueOf(this.getConfiguredProperty(EJBGlobals.TRANSACTION_TYPE));
        }
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = "Required";
        }
        return transactionType;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBOperationFacade#isBusinessOperation()
     */
    protected boolean handleIsBusinessOperation()
    {
        return !this.hasStereotype(EJBProfile.STEREOTYPE_CREATE_METHOD) &&
                !this.hasStereotype(EJBProfile.STEREOTYPE_FINDER_METHOD) &&
                !this.hasStereotype(EJBProfile.STEREOTYPE_SELECT_METHOD);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBOperationFacade#isSelectMethod()
     */
    protected boolean handleIsSelectMethod()
    {
        return this.hasStereotype(EJBProfile.STEREOTYPE_SELECT_METHOD);
    }
}