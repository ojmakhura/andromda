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

    protected java.lang.String handleGetDaoReferenceName()
    {
        final char[] name = getName().toCharArray();
        if (name.length > 0)
        {
            name[0] = Character.toLowerCase(name[0]);
        }

        return new String(name) + "Dao";
    }

    protected java.lang.String handleGetCrudDaoName()
    {
        return getName() + "CrudDao";
    }

    protected java.lang.String handleGetFullyQualifiedCrudDaoName()
    {
        return getCrudPackageName() + '.' + getCrudDaoName();
    }

    protected String handleGetCrudDaoFullPath()
    {
        return getFullyQualifiedCrudDaoName().replace('.', '/');
    }

    protected String handleGetCrudDaoBaseName()
    {
        return getCrudDaoName() + "Base";
    }

    protected String handleGetFullyQualifiedCrudDaoBaseName()
    {
        return getCrudPackageName() + '.' + getCrudDaoBaseName();
    }

    protected String handleGetCrudDaoBaseFullPath()
    {
        return getFullyQualifiedCrudDaoBaseName().replace('.', '/');
    }

    protected String handleGetCrudServiceBaseName()
    {
        return getCrudServiceName() + "Base";
    }

    protected String handleGetFullyQualifiedCrudServiceBaseName()
    {
        return getCrudPackageName() + '.' + getCrudServiceBaseName();
    }

    protected String handleGetCrudServiceBaseFullPath()
    {
        return getFullyQualifiedCrudServiceBaseName().replace('.', '/');
    }

    protected String handleGetCrudValueObjectFullPath()
    {
        return getFullyQualifiedCrudValueObjectName().replace('.', '/');
    }

    protected String handleGetCrudValueObjectClassName()
    {
        return getName() + "ValueObject";
    }

    protected String handleGetFullyQualifiedCrudValueObjectName()
    {
        return getCrudPackageName() + '.' + getCrudValueObjectClassName();
    }
}