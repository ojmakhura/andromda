package org.andromda.cartridges.hibernate.metafacades;

import java.text.MessageFormat;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateEmbeddedValue.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateEmbeddedValue
 * @author Klaus Schultz
 */
public class HibernateEmbeddedValueLogicImpl
    extends HibernateEmbeddedValueLogic
{
    // ---------------- constructor -------------------------------
    public HibernateEmbeddedValueLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEmbeddedValue#isImmutable()
     */
    protected boolean handleIsImmutable()
    {
        boolean immutable = false;
        Object value = this.findTaggedValue(HibernateProfile.TAGGEDVALUE_PERSISTENCE_IMMUTABLE);
        if (value != null)
        {
            immutable = Boolean.valueOf(ObjectUtils.toString(value)).booleanValue();
        }
        return immutable;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEmbeddedValue#getImplementationName()
     */
    protected java.lang.String handleGetImplementationName()
    {
        final String implNamePattern =
            String.valueOf(this.getConfiguredProperty(HibernateGlobals.EMBEDDED_VALUE_IMPLEMENTATION_NAME_PATTERN));
        return MessageFormat.format(
            implNamePattern,
            new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntity#getEntityName()
     */
    protected String handleGetEntityName()
    {
        final String entityNamePattern = (String)this.getConfiguredProperty(HibernateGlobals.ENTITY_NAME_PATTERN);
        return MessageFormat.format(
            entityNamePattern,
            new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * analogy of HibernateEntityLogicImpl
     *
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEmbeddedValue#getFullyQualifiedEntityName()
     */
    protected java.lang.String handleGetFullyQualifiedEntityName()
    {
        return HibernateMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getName(),
            null);
    }

    /**
     * see HibernateEntityLogicImpl
     */
    protected java.util.Collection handleGetBusinessOperations()
    {
        return HibernateMetafacadeUtils.filterBusinessOperations(this.getOperations());
    }
}