package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.cartridges.hibernate.HibernateProfile;

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
        String lazyString = (String) findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_LAZY);
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

}
