package org.andromda.cartridges.spring.metafacades;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringManageableEntity.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntity
 */
public class SpringManageableEntityLogicImpl
    extends SpringManageableEntityLogic
{
    // ---------------- constructor -------------------------------

    public SpringManageableEntityLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntity#getFullyQualifiedCrudDaoName()
     */
    protected java.lang.String handleGetFullyQualifiedCrudDaoName()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntity#getCrudDaoName()
     */
    protected java.lang.String handleGetCrudDaoName()
    {
        // TODO: put your implementation here.
        return null;
    }

}