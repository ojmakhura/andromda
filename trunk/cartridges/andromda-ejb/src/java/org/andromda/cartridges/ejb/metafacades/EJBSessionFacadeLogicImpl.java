package org.andromda.cartridges.ejb.metafacades;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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

    public EJBSessionFacadeLogicImpl(
        java.lang.Object metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getCreateMethods(boolean)
     */
    public java.util.Collection handleGetCreateMethods(boolean follow)
    {
        return EJBMetafacadeUtils.getCreateMethods(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getHomeInterfaceName()
     */
    public java.lang.String handleGetHomeInterfaceName()
    {
        return EJBMetafacadeUtils.getHomeInterfaceName(this);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getViewType()
     */
    public java.lang.String handleGetViewType()
    {
        return EJBMetafacadeUtils.getViewType(this);
    }

    public List handleGetInheritedInstanceAttributes()
    {
        return EJBMetafacadeUtils.getInheritedInstanceAttributes(this);
    }

    public List handleGetAllInstanceAttributes()
    {
        return EJBMetafacadeUtils.getAllInstanceAttributes(this);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getEnvironmentEntries(boolean)
     */
    public Collection handleGetEnvironmentEntries(boolean follow)
    {
        return EJBMetafacadeUtils.getEnvironmentEntries(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSessionFacade#getConstants(boolean)
     */
    public Collection handleGetConstants(boolean follow)
    {
        return EJBMetafacadeUtils.getConstants(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBSession#getJndiName()
     */
    public java.lang.String handleGetJndiName()
    {
        StringBuffer jndiName = new StringBuffer();
        String jndiNamePrefix = StringUtils.trimToEmpty(this.getJndiNamePrefix());
        if (StringUtils.isNotEmpty(jndiNamePrefix))
        {
            jndiName.append(jndiNamePrefix);
            jndiName.append("/");
        }
        jndiName.append("ejb/");
        jndiName.append(this.getFullyQualifiedName());
        return jndiName.toString();
    }
    
    /**
     * The prefix to use when creating this EJB's JNDI name.
     */
    static final String JNDI_NAME_PREFIX = "jndiNamePrefix";

    /**
     * Sets the <code>jndiNamePrefix</code> for this EJB.
     * 
     * @param jndiNamePrefix the prefix to use when binding this EJB to a given
     *        JNDI name. This is useful when you have more than on app using the
     *        same EJB within the same container.
     */
    public void setJndiNamePrefix(String jndiNamePrefix)
    {
        this.registerConfiguredProperty(JNDI_NAME_PREFIX, StringUtils
            .trimToEmpty(jndiNamePrefix));
    }

    /**
     * Gets the <code>jndiNamePrefix</code> for this EJB.
     * 
     * @return the EJB Jndi name prefix.
     */
    protected String getJndiNamePrefix()
    {
        return (String)this.getConfiguredProperty(JNDI_NAME_PREFIX);
    }
}