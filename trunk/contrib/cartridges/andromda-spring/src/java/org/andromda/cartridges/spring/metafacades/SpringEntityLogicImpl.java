package org.andromda.cartridges.spring.metafacades;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringEntity.
 * 
 * @see org.andromda.cartridges.spring.metafacades.SpringEntity
 */
public class SpringEntityLogicImpl
    extends SpringEntityLogic
    implements org.andromda.cartridges.spring.metafacades.SpringEntity
{
    // ---------------- constructor -------------------------------

    public SpringEntityLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoName()
     */
    public java.lang.String handleGetDaoName()
    {
        return this.getName() + SpringGlobals.DAO_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoName()
     */
    public java.lang.String handleGetFullyQualifiedDaoName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this
            .getPackageName(), this.getName(), SpringGlobals.DAO_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoImplementationName()
     */
    public java.lang.String handleGetDaoImplementationName()
    {
        return this.getName() + SpringGlobals.DAO_IMPLEMENTATION_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoImplementationName()
     */
    public java.lang.String handleGetFullyQualifiedDaoImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getName(),
            SpringGlobals.DAO_IMPLEMENTATION_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getDaoBaseName()
     */
    public java.lang.String handleGetDaoBaseName()
    {
        return this.getName() + SpringGlobals.DAO_BASE_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedDaoBaseName()
     */
    public java.lang.String handleGetFullyQualifiedDaoBaseName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this
            .getPackageName(), this.getName(), SpringGlobals.DAO_BASE_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getImplementationName()
     */
    public java.lang.String handleGetEntityImplementationName()
    {
        return this.getEntityName() + SpringGlobals.IMPLEMENTATION_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedEntityImplementationName()
     */
    public java.lang.String handleGetFullyQualifiedEntityImplementationName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEntityName(),
            SpringGlobals.IMPLEMENTATION_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getBeanName(boolean)
     */
    public java.lang.String handleGetBeanName(boolean targetSuffix)
    {
        StringBuffer beanName = new StringBuffer(StringUtils
            .uncapitalize(StringUtils.trimToEmpty(this.getName())));
        beanName.append(SpringGlobals.DAO_SUFFIX);
        if (targetSuffix)
        {
            beanName.append(SpringGlobals.BEAN_NAME_TARGET_SUFFIX);
        }
        return beanName.toString();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getEntityName()
     */
    public String handleGetEntityName()
    {
        String entityNamePattern = (String)this
            .getConfiguredProperty("entityNamePattern");
        return MessageFormat.format(entityNamePattern, new String[]
        {
            StringUtils.trimToEmpty(this.getName())
        });
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntity#getFullyQualifiedEntityName()
     */
    public String handleGetFullyQualifiedEntityName()
    {
        return SpringMetafacadeUtils.getFullyQualifiedName(this
            .getPackageName(), this.getEntityName(), null);
    }
}