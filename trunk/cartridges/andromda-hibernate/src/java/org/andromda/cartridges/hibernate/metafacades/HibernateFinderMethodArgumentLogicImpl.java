package org.andromda.cartridges.hibernate.metafacades;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodArgument.
 * 
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodArgument
 */
public class HibernateFinderMethodArgumentLogicImpl
    extends HibernateFinderMethodArgumentLogic
    implements
    org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodArgument
{
    // ---------------- constructor -------------------------------

    public HibernateFinderMethodArgumentLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodArgument#getQueryArgumentNameSetter()
     */
    protected java.lang.String handleGetQueryArgumentNameSetter()
    {
        StringBuffer setterName = new StringBuffer("setParameter");
        if (this.getType().isCollectionType())
        {
            setterName.append("List"); 
        }
        return setterName.toString();
    }

}