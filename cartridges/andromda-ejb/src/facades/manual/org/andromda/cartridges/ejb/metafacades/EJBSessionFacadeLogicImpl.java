package org.andromda.cartridges.ejb.metafacades;


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
    public java.util.Collection getCreateMethods(boolean follow) {
        return EJBMetafacadeUtils.getCreateMethods(this, follow);
    }

	/**
	 * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getHomeInterfaceName()
	 */
    public java.lang.String getHomeInterfaceName() {
        return EJBMetafacadeUtils.getHomeInterfaceName(this);
    }

	/**
	 * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getViewType()
	 */
    public java.lang.String getViewType() {
        return EJBMetafacadeUtils.getViewType(this);
    }

}
