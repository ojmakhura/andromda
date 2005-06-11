package org.andromda.cartridges.hibernate.metafacades;

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
    // ---------------- constructor -------------------------------
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
    protected java.lang.String handleGetFullyQualifiedHibernateType()
    {
        return super.getFullyQualifiedName();
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEnumeration#getEnumerationName()
     */
    protected String handleGetEnumerationName()
    {
        return StringUtils.trimToEmpty(this.getEnumerationNamePattern()).replaceAll(
            "\\{0\\}",
            super.getName());
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEnumeration#getFullyQualifiedHibernateEnumerationType()
     */
    protected String handleGetFullyQualifiedHibernateEnumerationType()
    {
        return HibernateMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEnumerationName(),
            null);
    }
}