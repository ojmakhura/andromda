package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.cartridges.hibernate.HibernateProfile;
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
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#isManagesRelationalLink()
     */
    public boolean handleIsManagesRelationalLink()
    {
        return AssociationLinkManagerFinder.managesRelationalLink(this);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#isLazy()
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
     * Stores the default outerjoin setting for this association end.
     */
    private static final String PROPERTY_ASSOCIATION_END_OUTERJOIN = "hibernateAssociationEndOuterJoin";

    /**
     * Defines the <code>true</code> value for the hibernate outer join
     * option.
     */
    public static final String OUTER_JOIN_TRUE = "true";

    /**
     * Defines the <code>false</code> value for the hibernate outer join
     * option.
     */
    public static final String OUTER_JOIN_FALSE = "false";

    /**
     * Defines the <code>auto</code> value for the hibernate outer join
     * option.
     */
    public static final String OUTER_JOIN_AUTO = "auto";

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
        String outerJoin = StringUtils.trimToEmpty(String.valueOf(value));
        if (!outerJoin.equalsIgnoreCase(OUTER_JOIN_FALSE)
            && !outerJoin.equalsIgnoreCase(OUTER_JOIN_TRUE))
        {
            outerJoin = OUTER_JOIN_AUTO;
        }
        return outerJoin;
    }

}