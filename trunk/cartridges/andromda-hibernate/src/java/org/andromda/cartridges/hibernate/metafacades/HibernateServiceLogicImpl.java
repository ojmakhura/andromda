package org.andromda.cartridges.hibernate.metafacades;

import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.hibernate.metafacades.HibernateService.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateService
 */
public class HibernateServiceLogicImpl
       extends HibernateServiceLogic
       implements org.andromda.cartridges.hibernate.metafacades.HibernateService
{
    // ---------------- constructor -------------------------------

    public HibernateServiceLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateService#getEjbJndiName()
     */
    protected java.lang.String handleGetEjbJndiName()
    {
        StringBuffer jndiName = new StringBuffer();
        String jndiNamePrefix = StringUtils.trimToEmpty(this
            .getEjbJndiNamePrefix());
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
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateService#getEjbViewType()
     */
    protected java.lang.String handleGetEjbViewType()
    {
        return HibernateMetafacadeUtils.getViewType(this);
    }

    /**
     * Gets the <code>ejbJndiNamePrefix</code> for this EJB.
     * 
     * @return the EJB Jndi name prefix.
     */
    protected String getEjbJndiNamePrefix()
    {
        return (String)this.getConfiguredProperty("ejbJndiNamePrefix");
    }
    
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateService#isEjbStateful()
     */
    protected boolean handleIsEjbStateful() 
    {
        return !this.getAttributes().isEmpty();
    }
    
}
