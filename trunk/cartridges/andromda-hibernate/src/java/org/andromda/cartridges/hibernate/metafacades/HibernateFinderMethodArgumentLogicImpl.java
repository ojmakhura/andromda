package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.core.common.StringUtilsHelper;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodArgument.
 * 
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodArgument
 */
public class HibernateFinderMethodArgumentLogicImpl
    extends HibernateFinderMethodArgumentLogic
{
    // ---------------- constructor -------------------------------

    public HibernateFinderMethodArgumentLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }
    
    /**
     * Defines w
     */
    private static final String USE_SPECIALIZED_SETTERS="hibernateQueryUseSpecializedSetters";
    
    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodArgument#getQueryArgumentNameSetter()
     */
    protected java.lang.String handleGetQueryArgumentNameSetter()
    {
        StringBuffer setterName = new StringBuffer();
        boolean specializedSetters=Boolean.valueOf(
                String.valueOf(this.getConfiguredProperty(USE_SPECIALIZED_SETTERS)))
                .booleanValue();
        if (specializedSetters)
        {
            if (this.getType().isPrimitive())
            {
                setterName.append("set" + this.getType().getWrapperName().replaceAll("(.)*\\.", ""));
            }
            else if (this.getType().isDateType()
                    || this.getType().isStringType())
            {
                setterName.append("set" + this.getType().getName());
            }
            else
            {
                setterName.append("setParameter");
            }
        }
        else
        {
                setterName.append("setParameter");
        }
        if (this.getType().isCollectionType())
        {
            setterName.append("List");
        }
        return setterName.toString();
    }
}