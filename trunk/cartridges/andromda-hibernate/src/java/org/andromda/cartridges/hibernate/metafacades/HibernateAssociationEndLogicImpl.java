package org.andromda.cartridges.hibernate.metafacades;


/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd.
 * 
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd
 */
public class HibernateAssociationEndLogicImpl extends
        HibernateAssociationEndLogic implements
        org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd {

    // ---------------- constructor -------------------------------

    public HibernateAssociationEndLogicImpl(Object metaObject, String context) {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateAssociationEnd#isManagesRelationalLink()
     */
    public boolean handleIsManagesRelationalLink() {
        return AssociationLinkManagerFinder.managesRelationalLink(this);
    }
}
