package org.andromda.cartridges.hibernate.metafacades;

import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.hibernate.metafacades.HibernateSessionEJB.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateSessionEJB
 */
public class HibernateSessionEJBLogicImpl
       extends HibernateSessionEJBLogic
       implements org.andromda.cartridges.hibernate.metafacades.HibernateSessionEJB
{
    // ---------------- constructor -------------------------------

    public HibernateSessionEJBLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    // -------------------- business methods ----------------------

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateSessionEJB#getJndiName()
     */
    public java.lang.String handleGetJndiName()
    {
        StringBuffer jndiName = new StringBuffer();
        String jndiNamePrefix = StringUtils.trimToEmpty(this.getEjbJndiNamePrefix());
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
    static final String EJB_JNDI_NAME_PREFIX = "ejbJndiNamePrefix";

    /**
     * Sets the <code>ejbJndiNamePrefix</code> for this EJB.
     * 
     * @param ejbJndiNamePrefix the prefix to use when binding
     *        this EJB to a given JNDI name.  This is useful 
     *        when you have more than on app using the same EJB
     *        within the same container.
     */
    public void setEjbJndiNamePrefix(String ejbJndiNamePrefix)
    {
        this.registerConfiguredProperty(EJB_JNDI_NAME_PREFIX, StringUtils
            .trimToEmpty(ejbJndiNamePrefix));
    }

    /**
     * Gets the <code>ejbJndiNamePrefix</code> for this EJB.
     * 
     * @return the EJB Jndi name prefix.
     */
    protected String getEjbJndiNamePrefix()
    {
        return (String)this.getConfiguredProperty(EJB_JNDI_NAME_PREFIX);
    }    
}