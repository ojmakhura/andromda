package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd.
 * 
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd
 */
public class HibernateAssociationEndLogicImpl
    extends HibernateAssociationEndLogic
    implements
    org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd
{

    // ---------------- constructor -------------------------------

    public HibernateAssociationEndLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAssociationEnd#isOne2OnePrimary()
     */
    protected boolean handleIsOne2OnePrimary()
    {
        boolean primaryOne2One = super.isOne2One();
        HibernateAssociationEnd otherEnd = (HibernateAssociationEnd)this
            .getOtherEnd();
        if (primaryOne2One)
        {
            primaryOne2One = super.isAggregation() || this.isComposition();
        }
        // if the flag is false delegate to the super class
        if (!primaryOne2One)
        {
            primaryOne2One = super.isOne2One() && !otherEnd.isAggregation()
                && !otherEnd.isComposition();
        }
        return primaryOne2One;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterSetterTypeName()
     */
    public String getGetterSetterTypeName()
    {
        String getterSetterTypeName = super.getGetterSetterTypeName();
        if (!this.isMany())
        {
            ClassifierFacade type = this.getType();
            if (type != null
                && HibernateEntity.class.isAssignableFrom(type.getClass()))
            {
                String typeName = ((HibernateEntity)type)
                    .getFullyQualifiedEntityName();
                if (StringUtils.isNotEmpty(typeName))
                {
                    getterSetterTypeName = typeName;
                }
            }
        }
        return getterSetterTypeName;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isLazy()
     */
    protected boolean handleIsLazy()
    {
        String lazyString = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_LAZY);
        boolean lazy;
        if (lazyString == null)
        {
            lazy = !isComposition();
        }
        else
        {
            lazy = Boolean.valueOf(lazyString).booleanValue();
        }
        return lazy;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAssociationEnd#isOne2OneSecondary()
     */
    protected boolean handleIsOne2OneSecondary()
    {
        boolean secondary = false;
        Object type = this.getType();
        Object otherType = this.getOtherEnd().getType();
        if (type != null
            && HibernateEntity.class.isAssignableFrom(type.getClass())
            && otherType != null
            && HibernateEntity.class.isAssignableFrom(otherType.getClass()))
        {
            HibernateEntity entity = (HibernateEntity)type;
            HibernateEntity otherEntity = (HibernateEntity)otherType;
            secondary = (this.isChild() && entity
                .isForeignHibernateGeneratorClass())
                || otherEntity.isForeignHibernateGeneratorClass()
                || (!this.isNavigable() && this.getOtherEnd().isNavigable() && !this
                    .isOne2OnePrimary());
        }
        return secondary;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAssociationEnd#getHibernateCascade()
     */
    protected String handleGetHibernateCascade()
    {
        String cascade = HibernateGlobals.HIBERNATE_CASCADE_DELETE;
        Object type = this.getType();
        if (type != null
            && HibernateEntity.class.isAssignableFrom(type.getClass()))
        {
            HibernateEntity entity = (HibernateEntity)type;
            if (entity.getHibernateDefaultCascade().equalsIgnoreCase(
                HibernateGlobals.HIBERNATE_CASCADE_SAVE_UPDATE)
                && this.getOtherEnd().isMany())
            {
                cascade = HibernateGlobals.HIBERNATE_CASCADE_ALL_DELETE_ORPHAN;
            }
        }
        return cascade;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateEntityAssociationEnd#isHibernateInverse()
     */
    protected boolean handleIsHibernateInverse()
    {
        // inverse can only be true if the relation is bidirectional
        boolean inverse = this.isNavigable()
            && this.getOtherEnd().isNavigable();
        if (inverse)
        {
            inverse = this.isMany2One();
            // for many-to-many we just put the flag on the side that 
            // has the lexically longer fully qualified name for 
            // it's type
            if (this.isMany2Many() && !inverse)
            {
                String endTypeName = StringUtils.trimToEmpty(this.getType()
                    .getFullyQualifiedName(true));
                String otherEndTypeName = StringUtils.trimToEmpty(this
                    .getOtherEnd().getType().getFullyQualifiedName(true));
                int compareTo = endTypeName.compareTo(otherEndTypeName);
                // if for some reason the fully qualified names are equal,
                // compare the names.
                if (compareTo == 0)
                {
                    String endName = StringUtils.trimToEmpty(this.getName());
                    String otherEndName = StringUtils.trimToEmpty(this
                        .getOtherEnd().getName());
                    compareTo = endName.compareTo(otherEndName);
                }
                inverse = compareTo < 0;
            }
        }
        return inverse;
    }

    /**
     * Stores the default outerjoin setting for this association end.
     */
    private static final String PROPERTY_ASSOCIATION_END_OUTERJOIN = "hibernateAssociationEndOuterJoin";

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#getOuterJoin()
     */
    protected String handleGetOuterJoin()
    {
        Object value = this
            .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_OUTER_JOIN);
        if (value == null)
        {
            value = this
                .getConfiguredProperty(PROPERTY_ASSOCIATION_END_OUTERJOIN);
        }
        return StringUtils.trimToEmpty(String.valueOf(value));
    }

}