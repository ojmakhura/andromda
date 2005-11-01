package org.andromda.cartridges.nspring.metafacades;

import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.nspring.metafacades.SpringManageableEntity.
 *
 * @see org.andromda.cartridges.nspring.metafacades.SpringManageableEntity
 */
public class SpringManageableEntityLogicImpl
    extends SpringManageableEntityLogic
{

    public SpringManageableEntityLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @return the configured property denoting the character sequence to use for the separation of namespaces
     */
    private String getNamespaceProperty()
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
        return getManageablePackageName() + getNamespaceProperty() + getManageableDaoName();
    }

    protected String handleGetManageableDaoFullPath()
    {
        return StringUtils.replace(this.getFullyQualifiedManageableDaoName(), getNamespaceProperty(), "/");
    }

    protected String handleGetManageableDaoBaseName()
    {
        return getManageableDaoName() + "Base";
    }

    protected String handleGetFullyQualifiedManageableDaoBaseName()
    {
        return getManageablePackageName() + getNamespaceProperty() + getManageableDaoBaseName();
    }

    protected String handleGetManageableDaoBaseFullPath()
    {
        return StringUtils.replace(this.getFullyQualifiedManageableDaoBaseName(), this.getNamespaceProperty(), "/");
    }

    protected String handleGetManageableServiceBaseName()
    {
        return getManageableServiceName() + "Base";
    }

    protected String handleGetFullyQualifiedManageableServiceBaseName()
    {
        return getManageablePackageName() + getNamespaceProperty() + getManageableServiceBaseName();
    }

    protected String handleGetManageableServiceBaseFullPath()
    {
        return StringUtils.replace(this.getFullyQualifiedManageableServiceBaseName(), this.getNamespaceProperty(), "/");
    }

    protected String handleGetManageableValueObjectFullPath()
    {
        return StringUtils.replace(this.getFullyQualifiedManageableValueObjectName(), this.getNamespaceProperty(), "/");
    }

    protected String handleGetManageableValueObjectClassName()
    {
        return getName() + "ValueObject";
    }

    protected String handleGetFullyQualifiedManageableValueObjectName()
    {
        return getManageablePackageName() + getNamespaceProperty() + getManageableValueObjectClassName();
    }
}