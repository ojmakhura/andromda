package org.andromda.cartridges.spring.metafacades;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringService.
 * 
 * @see org.andromda.cartridges.spring.metafacades.SpringService
 */
public class SpringServiceLogicImpl
    extends SpringServiceLogic
    implements org.andromda.cartridges.spring.metafacades.SpringService
{
    // ---------------- constructor -------------------------------

    public SpringServiceLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbJndiName()
     */
    public java.lang.String handleGetEjbJndiName()
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

    public boolean handleHasReferences()
    {
        return (this.getEntityReferences() != null && !this
            .getEntityReferences().isEmpty())
            || (this.getServiceReferences() != null && !this
                .getServiceReferences().isEmpty());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbImplementationName()
     */
    public java.lang.String handleGetEjbImplementationName()
    {
        return this.getName() + SpringGlobals.EJB_IMPLEMENTATION_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#etImplementationName()
     */
    public java.lang.String handleGetImplementationName()
    {
        return this.getName() + SpringGlobals.IMPLEMENTATION_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedImplementationName()
     */
    public java.lang.String handleGetFullyQualifiedEjbImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getEjbPackageName(),
            this.getName(),
            SpringGlobals.EJB_IMPLEMENTATION_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedEjbName()
     */
    public java.lang.String handleGetFullyQualifiedEjbName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this
            .getEjbPackageName(), this.getName(), null);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedImplementationName()
     */
    public java.lang.String handleGetFullyQualifiedImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getName(),
            SpringGlobals.IMPLEMENTATION_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getBaseName()
     */
    public java.lang.String handleGetBaseName()
    {
        return this.getName() + SpringGlobals.SERVICE_BASE_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getFullyQualifiedBaseName()
     */
    public java.lang.String handleGetFullyQualifiedBaseName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getName(),
            SpringGlobals.SERVICE_BASE_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbPackageName()
     */
    public java.lang.String handleGetEjbPackageName()
    {
        return MessageFormat.format(
            this.getEjbPackageNamePattern(),
            new String[]
            {
                StringUtils.trimToEmpty(this.getPackageName())
            });
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getEjbPackageNamePath()
     */
    public java.lang.String handleGetEjbPackageNamePath()
    {
        return this.getEjbPackageName().replace('.', '/');
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getBeanName()
     */
    public java.lang.String handleGetBeanName()
    {
        return this.getBeanName(false);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringService#getBeanName(boolean)
     */
    public java.lang.String handleGetBeanName(boolean targetSuffix)
    {
        StringBuffer beanName = new StringBuffer(StringUtils
            .uncapitalize(StringUtils.trimToEmpty(this.getName())));
        if (targetSuffix)
        {
            beanName.append(SpringGlobals.BEAN_NAME_TARGET_SUFFIX);
        }
        return beanName.toString();
    }

    /**
     * Gets the <code>ejbPackageNamePattern</code> for this EJB.
     * 
     * @return the defined package pattern.
     */
    protected String getEjbPackageNamePattern()
    {
        return (String)this.getConfiguredProperty("ejbPackageNamePattern");
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
}