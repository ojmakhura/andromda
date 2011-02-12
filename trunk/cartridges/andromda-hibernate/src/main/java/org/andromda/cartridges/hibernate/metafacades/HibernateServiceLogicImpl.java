package org.andromda.cartridges.hibernate.metafacades;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateService.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateService
 */
public class HibernateServiceLogicImpl
    extends HibernateServiceLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public HibernateServiceLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateService#getEjbJndiName()
     */
    @Override
    protected String handleGetEjbJndiName()
    {
        StringBuilder jndiName = new StringBuilder();
        String jndiNamePrefix = StringUtils.trimToEmpty(this.getEjbJndiNamePrefix());
        if (StringUtils.isNotBlank(jndiNamePrefix))
        {
            jndiName.append(jndiNamePrefix);
            jndiName.append('/');
        }
        jndiName.append("ejb/");
        jndiName.append(this.getFullyQualifiedName());
        return jndiName.toString();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateService#getEjbViewType()
     */
    @Override
    protected String handleGetEjbViewType()
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
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateService#isEjbStateful()
     */
    @Override
    protected boolean handleIsEjbStateful()
    {
        return !this.getAttributes().isEmpty();
    }

    /**
     * The value used to indicate the interfaces for an EJB are remote.
     */
    private static final String VIEW_TYPE_REMOTE = "remote";

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateService#isEjbRemoteView()
     */
    @Override
    protected boolean handleIsEjbRemoteView()
    {
        return this.getEjbViewType().equalsIgnoreCase(VIEW_TYPE_REMOTE);
    }
}