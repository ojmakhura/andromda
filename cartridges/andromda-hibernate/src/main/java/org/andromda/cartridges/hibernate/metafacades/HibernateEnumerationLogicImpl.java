package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.utils.JavaTypeConverter;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateEnumeration.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateEnumeration
 */
public class HibernateEnumerationLogicImpl
    extends HibernateEnumerationLogic
{
    private static final long serialVersionUID = 34L;
    // ---------------- constructor -------------------------------
    /**
     * @param metaObject
     * @param context
     */
    public HibernateEnumerationLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * The pattern to use when constructing the enumeration name.
     */
    private static final String ENUMERATION_NAME_PATTERN = "enumerationNamePattern";

    /**
     * Returns the value of the enumeration name pattern.
     *
     * @return the enumeration name pattern.
     */
    private String getEnumerationNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(ENUMERATION_NAME_PATTERN));
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEnumeration#getFullyQualifiedHibernateType()
     */
    @Override
    protected String handleGetFullyQualifiedHibernateType()
    {
        return JavaTypeConverter.getJavaLangTypeName(super.getFullyQualifiedName());
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEnumeration#getEnumerationName()
     */
    @Override
    protected String handleGetEnumerationName()
    {
        return StringUtils.trimToEmpty(this.getEnumerationNamePattern()).replaceAll(
            "\\{0\\}",
            super.getName());
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEnumeration#getFullyQualifiedHibernateEnumerationType()
     */
    @Override
    protected String handleGetFullyQualifiedHibernateEnumerationType()
    {
        return HibernateMetafacadeUtils.getFullyQualifiedName(
            this.getHibernateEnumerationPackageName(),
            this.getEnumerationName(),
            null);
    }

    /**
     * Check for optional defined enumTypesPackage (overrides modeled package name) 
     */
    private String getHibernateEnumerationPackageName() {
        String enumPackageName = (String) this.getConfiguredProperty(HibernateGlobals.ENUM_TYPES_PACKAGE);
        if (enumPackageName != null) {
            return enumPackageName;
        } else {
            return super.getPackageName();
        }
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEnumeration#getVersion()
     */
    @Override
    protected int handleGetVersion()
    {
        return Integer.parseInt((String)this.getConfiguredProperty(HibernateGlobals.HIBERNATE_VERSION));
    }
}