package org.andromda.cartridges.ejb.metafacades;

import java.util.Collection;
import java.util.List;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade
 */
public class EJBSessionFacadeLogicImpl
       extends EJBSessionFacadeLogic
       implements org.andromda.cartridges.ejb.metafacades.EJBSessionFacade
{
    // ---------------- constructor -------------------------------

    public EJBSessionFacadeLogicImpl (java.lang.Object metaObject, java.lang.String context)
    {
        super (metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class EJBSessionFacade ...

	/**
	 * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getCreateMethods(boolean)
	 */
    public java.util.Collection handleGetCreateMethods(boolean follow) {
        return EJBMetafacadeUtils.getCreateMethods(this, follow);
    }

	/**
	 * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getHomeInterfaceName()
	 */
    public java.lang.String handleGetHomeInterfaceName() {
        return EJBMetafacadeUtils.getHomeInterfaceName(this);
    }

	/**
	 * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getViewType()
	 */
    public java.lang.String handleGetViewType() {
        return EJBMetafacadeUtils.getViewType(this);
    }

    public List handleGetInheritedInstanceAttributes() {
        return EJBMetafacadeUtils.getInheritedInstanceAttributes(this);
    }

    public List handleGetAllInstanceAttributes() {
        return EJBMetafacadeUtils.getAllInstanceAttributes(this);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getEnvironmentEntries(boolean)
     */
    public Collection handleGetEnvironmentEntries(boolean follow) {
        return EJBMetafacadeUtils.getEnvironmentEntries(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getConstants(boolean)
     */
    public Collection handleGetConstants(boolean follow) {
        return EJBMetafacadeUtils.getConstants(this, follow);
    }

}
