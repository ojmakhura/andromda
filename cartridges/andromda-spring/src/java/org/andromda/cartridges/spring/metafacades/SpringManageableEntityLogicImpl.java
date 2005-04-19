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

    protected java.lang.String handleGetManageableDaoName()
    {
        return getName() + "ManageableDao";
    }

    protected java.lang.String handleGetFullyQualifiedManageableDaoName()
    {
        return getManageablePackageName() + '.' + getManageableDaoName();
    }

    protected String handleGetManageableDaoFullPath()
    {
        return getFullyQualifiedManageableDaoName().replace('.', '/');
    }

    protected String handleGetManageableDaoBaseName()
    {
        return getManageableDaoName() + "Base";
    }

    protected String handleGetFullyQualifiedManageableDaoBaseName()
    {
        return getManageablePackageName() + '.' + getManageableDaoBaseName();
    }

    protected String handleGetManageableDaoBaseFullPath()
    {
        return getFullyQualifiedManageableDaoBaseName().replace('.', '/');
    }

    protected String handleGetManageableServiceBaseName()
    {
        return getManageableServiceName() + "Base";
    }

    protected String handleGetFullyQualifiedManageableServiceBaseName()
    {
        return getManageablePackageName() + '.' + getManageableServiceBaseName();
    }

    protected String handleGetManageableServiceBaseFullPath()
    {
        return getFullyQualifiedManageableServiceBaseName().replace('.', '/');
    }

    protected String handleGetManageableValueObjectFullPath()
    {
        return getFullyQualifiedManageableValueObjectName().replace('.', '/');
    }

    protected String handleGetManageableValueObjectClassName()
    {
        return getName() + "ValueObject";
    }

    protected String handleGetFullyQualifiedManageableValueObjectName()
    {
        return getManageablePackageName() + '.' + getManageableValueObjectClassName();
    }
}