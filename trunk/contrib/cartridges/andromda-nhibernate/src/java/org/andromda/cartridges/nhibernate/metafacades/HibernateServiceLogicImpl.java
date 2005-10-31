package org.andromda.cartridges.nhibernate.metafacades;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.nhibernate.metafacades.HibernateService.
 *
 * @see org.andromda.cartridges.nhibernate.metafacades.HibernateService
 */
public class HibernateServiceLogicImpl
    extends HibernateServiceLogic
{
    public HibernateServiceLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.nhibernate.metafacades.HibernateService#getEjbJndiName()
     */
    protected java.lang.String handleGetEjbJndiName()
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
     * @see org.andromda.cartridges.nhibernate.metafacades.HibernateService#getEjbViewType()
     */
    protected java.lang.String handleGetEjbViewType()
    {
        String defaultViewType = String.valueOf(this.getConfiguredProperty("ejbViewType"));
        return HibernateMetafacadeUtils.getViewType(this, defaultViewType);
    }

    /**
     * Gets the <code>ejbJndiNamePrefix</code> for this EJB.
     *
     * @return the EJB Jndi name prefix.
     */
    protected String getEjbJndiNamePrefix()
    {
        final String property = "ejbJndiNamePrefix";
        return this.isConfiguredProperty(property) ? ObjectUtils.toString(this.getConfiguredProperty(property)) : null;
    }

    /**
     * @see org.andromda.cartridges.nhibernate.metafacades.HibernateService#isEjbStateful()
     */
    protected boolean handleIsEjbStateful()
    {
        return !this.getAttributes().isEmpty();
    }

    /**
     * The value used to indicate the interfaces for an EJB are remote.
     */
    private static final String VIEW_TYPE_REMOTE = "remote";

    /**
     * @see org.andromda.cartridges.nhibernate.metafacades.HibernateService#isEjbRemoteView()
     */
    protected boolean handleIsEjbRemoteView()
    {
        return this.getEjbViewType().equalsIgnoreCase(VIEW_TYPE_REMOTE);
    }
}