package org.andromda.cartridges.hibernate.metafacades;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityAssociationEndFacade;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd.
 * 
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd
 */
public class HibernateAssociationEndLogicImpl
    extends HibernateAssociationEndLogic
{
    /**
     * Value for set
     */
    private static final String COLLECTION_TYPE_SET = "set";

    /**
     * Value for map
     */
    private static final String COLLECTION_TYPE_MAP = "map";

    /**
     * Value for bags
     */
    private static final String COLLECTION_TYPE_BAG = "bag";

    /**
     * Value for list
     */
    private static final String COLLECTION_TYPE_LIST = "list";

    /**
     * Stores the valid inheritance strategies.
     */
    private static final Collection collectionTypes = new ArrayList();

    static
    {
        collectionTypes.add(COLLECTION_TYPE_SET);
        collectionTypes.add(COLLECTION_TYPE_MAP);
        collectionTypes.add(COLLECTION_TYPE_BAG);
        collectionTypes.add(COLLECTION_TYPE_LIST);
    }

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
     * Stores the property indicating whether or not composition should define
     * the eager loading strategy.
     */
    private static final String COMPOSITION_DEFINES_EAGER_LOADING = "compositionDefinesEagerLoading";

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isLazy()
     */
    protected boolean handleIsLazy()
    {
        String lazyString = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_LAZY);
        boolean lazy = true;
        if (lazyString == null)
        {
            // check whether or not composition defines eager loading is turned
            // on
            boolean compositionDefinesEagerLoading = Boolean.valueOf(
                String.valueOf(this
                    .getConfiguredProperty(COMPOSITION_DEFINES_EAGER_LOADING)))
                .booleanValue();
            if (compositionDefinesEagerLoading)
            {
                lazy = !this.getOtherEnd().isComposition();
            }
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
        String cascade = HibernateGlobals.HIBERNATE_CASCADE_NONE;
        if (this.isChild())
        {
            cascade = HibernateGlobals.HIBERNATE_CASCADE_DELETE;
            Object type = this.getType();
            if (type != null
                && HibernateEntity.class.isAssignableFrom(type.getClass()))
            {
                HibernateEntity entity = (HibernateEntity)type;
                final String defaultCascade = entity.getHibernateDefaultCascade();
                if (defaultCascade
                    .equalsIgnoreCase(HibernateGlobals.HIBERNATE_CASCADE_SAVE_UPDATE)
                    || defaultCascade
                        .equalsIgnoreCase(HibernateGlobals.HIBERNATE_CASCADE_ALL))
                {
                    if (this.isMany())
                    {
                        cascade = HibernateGlobals.HIBERNATE_CASCADE_ALL_DELETE_ORPHAN;
                    }
                    else
                    {
                        cascade = HibernateGlobals.HIBERNATE_CASCADE_ALL;
                    }
                }
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

    /**
     * Overridden to provide handling of inheritance.
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isRequired()
     */
    public boolean isRequired()
    {
        boolean required = super.isRequired();
        Object type = this.getType();
        if (type != null
            && HibernateEntity.class.isAssignableFrom(type.getClass()))
        {
            HibernateEntity entity = (HibernateEntity)type;
            if (entity.isHibernateInheritanceClass()
                && entity.getGeneralization() != null)
            {
                required = false;
            }
        }
        return required;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#getCollectionType()
     */
    protected String handleGetCollectionType()
    {
        String collectionType = (String) this
            .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ASSOCIATION_COLLECTION_TYPE);
        if (!collectionTypes.contains(collectionType))
        {
            collectionType = (String) this
                .getConfiguredProperty(HibernateGlobals.HIBERNATE_ASSOCIATION_COLLECTION_TYPE);
        }
        return collectionType;

    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#getSortType()
     */
    protected String handleGetSortType()
    {
        String sortType = (String) this
            .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ASSOCIATION_SORT_TYPE);
        return sortType;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#getOrderByColumns()
     */
    protected String handleGetOrderByColumns()
    {
        String orderColumns = (String) this
            .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ASSOCIATION_ORDER_BY_COLUMNS);
        if (orderColumns == null)
        {
            orderColumns = (String) ((EntityAssociationEndFacade) this.getOtherEnd())
                .getColumnName();

        }
        return orderColumns;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#getWhereClause()
     */
    protected String handleGetWhereClause()
    {
        String whereClause = (String) this
            .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ASSOCIATION_WHERE_CLAUSE);
        return whereClause;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#isIndexedCollection()
     */
    protected boolean handleIsIndexedCollection()
    {
        boolean indexed=false;
        if (this.isOrdered())
        {
            if (this.getCollectionType().equals(COLLECTION_TYPE_LIST)
                    || this.getCollectionType().equals(COLLECTION_TYPE_MAP))
            {
                indexed=true;
            }
        }
        return indexed;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#getCollectionIndexName()
     */
    protected String handleGetCollectionIndexName()
    {
        String indexName = (String) this
            .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ASSOCIATION_INDEX_COLUMN);
        return indexName;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#isMap()
     */
    protected boolean handleIsMap()
    {
        return this.getCollectionType().equalsIgnoreCase(
                COLLECTION_TYPE_MAP);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#isList()
     */
    protected boolean handleIsList() {
        return this.getCollectionType().equalsIgnoreCase(
                COLLECTION_TYPE_LIST);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#isSet()
     */
    protected boolean handleIsSet() {
        return this.getCollectionType().equalsIgnoreCase(
                COLLECTION_TYPE_SET);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#isBag()
     */
    protected boolean handleIsBag() {
        return this.getCollectionType().equalsIgnoreCase(
                COLLECTION_TYPE_BAG);
    }

}