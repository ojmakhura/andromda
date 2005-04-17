package org.andromda.cartridges.spring.metafacades;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd
 */
public class SpringManageableEntityAssociationEndLogicImpl
    extends SpringManageableEntityAssociationEndLogic
{
    // ---------------- constructor -------------------------------

    public SpringManageableEntityAssociationEndLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd#getDaoGetterName()
     */
    protected java.lang.String handleGetDaoGetterName()
    {
        return getGetterName() + "Dao";
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd#getDaoSetterName()
     */
    protected java.lang.String handleGetDaoSetterName()
    {
        return getSetterName() + "Dao";
    }

}