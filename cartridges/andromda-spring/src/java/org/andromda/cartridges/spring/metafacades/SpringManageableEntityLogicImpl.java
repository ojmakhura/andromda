package org.andromda.cartridges.spring.metafacades;

import org.andromda.metafacades.uml.UMLMetafacadeProperties;


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
     * @return the configured property denoting the character sequence to use for the separation of namespaces
     */
    private String internalGetNamespaceProperty()
    {
        return (String)getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR);
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
        return getManageablePackageName() + internalGetNamespaceProperty() + getManageableDaoName();
    }

    protected String handleGetManageableDaoFullPath()
    {
        return getFullyQualifiedManageableDaoName().replace(internalGetNamespaceProperty(), "/");
    }

    protected String handleGetManageableDaoBaseName()
    {
        return getManageableDaoName() + "Base";
    }

    protected String handleGetFullyQualifiedManageableDaoBaseName()
    {
        return getManageablePackageName() + internalGetNamespaceProperty() + getManageableDaoBaseName();
    }

    protected String handleGetManageableDaoBaseFullPath()
    {
        return getFullyQualifiedManageableDaoBaseName().replace(internalGetNamespaceProperty(), "/");
    }

    protected String handleGetManageableServiceBaseName()
    {
        return getManageableServiceName() + "Base";
    }

    protected String handleGetFullyQualifiedManageableServiceBaseName()
    {
        return getManageablePackageName() + internalGetNamespaceProperty() + getManageableServiceBaseName();
    }

    protected String handleGetManageableServiceBaseFullPath()
    {
        return getFullyQualifiedManageableServiceBaseName().replace(internalGetNamespaceProperty(), "/");
    }

    protected String handleGetManageableValueObjectFullPath()
    {
        return getFullyQualifiedManageableValueObjectName().replace(internalGetNamespaceProperty(), "/");
    }

    protected String handleGetManageableValueObjectClassName()
    {
        return getName() + "ValueObject";
    }

    protected String handleGetFullyQualifiedManageableValueObjectName()
    {
        return getManageablePackageName() + internalGetNamespaceProperty() + getManageableValueObjectClassName();
    }
}