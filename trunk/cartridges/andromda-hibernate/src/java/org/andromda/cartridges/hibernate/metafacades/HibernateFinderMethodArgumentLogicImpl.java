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
    public java.lang.String handleGetQueryArgumentNameSetter()
    {
        String suffix = this.getType().getFullyQualifiedName();   
        if (this.getType().isPrimitiveType())
        {
            suffix = this.getType().getWrapperName();
        }
        suffix = suffix.replaceAll(".*\\.", "");
        return "set" + suffix; 
    }

}