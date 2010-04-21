package org.andromda.cartridges.spring.metafacades;

import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringManageableEntity.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntity
 */
public class SpringManageableEntityLogicImpl
    extends SpringManageableEntityLogic
{

    /**
     * Public constructor for SpringManageableEntityLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntity
     */
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

    protected String handleGetDaoReferenceName()
    {
        return StringUtils.uncapitalize(getName()) + "Dao";
    }

    protected String handleGetManageableDaoName()
    {
        return getName() + "ManageableDao";
    }

    protected String handleGetFullyQualifiedManageableDaoName()
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
        return getName() + this.getConfiguredProperty(SpringGlobals.CRUD_VALUE_OBJECT_SUFFIX);
    }

    protected String handleGetFullyQualifiedManageableValueObjectName()
    {
        return getManageablePackageName() + getNamespaceProperty() + getManageableValueObjectClassName();
    }

    protected boolean handleIsRemotingTypeRmi()
    {
        return SpringGlobals.REMOTING_PROTOCOL_RMI.equalsIgnoreCase(this.getRemotingType());
    }

    protected boolean handleIsRemotingTypeNone()
    {
        return SpringGlobals.REMOTING_PROTOCOL_NONE.equalsIgnoreCase(this.getRemotingType());
    }

    protected boolean handleIsRemotingTypeHttpInvoker()
    {
        return SpringGlobals.REMOTING_PROTOCOL_HTTPINVOKER.equalsIgnoreCase(this.getRemotingType());
    }

    protected boolean handleIsRemotingTypeHessian()
    {
        return SpringGlobals.REMOTING_PROTOCOL_HESSIAN.equalsIgnoreCase(this.getRemotingType());
    }

    protected boolean handleIsRemotingTypeBurlap()
    {
        return SpringGlobals.REMOTING_PROTOCOL_BURLAP.equalsIgnoreCase(this.getRemotingType());
    }

    protected String handleGetRemoteUrl()
    {
        final StringBuilder result = new StringBuilder();

        String propertyPrefix = ObjectUtils.toString(this.getConfiguredProperty(SpringGlobals.CONFIG_PROPERTY_PREFIX));

        if (this.isRemotingTypeNone())
        {
            // nothing
        }
        else if (this.isRemotingTypeHttpInvoker() || this.isRemotingTypeHessian() || this.isRemotingTypeBurlap())
        {
            // server
            result.append("${").append(propertyPrefix).append("remoteHttpScheme}://${");
            result.append(propertyPrefix);
            result.append("remoteServer}");

            // port
            if (hasServiceRemotePort())
            {
                result.append(":${");
                result.append(propertyPrefix);
                result.append("remotePort}");
            }

            // context
            if (hasServiceRemoteContext())
            {
                result.append("/${");
                result.append(propertyPrefix);
                result.append("remoteContext}");
            }

            // service name
            result.append("/remote");
            result.append(this.getManageableServiceName());
        }
        else if (this.isRemotingTypeRmi())
        {
            // server
            result.append("${").append(propertyPrefix).append("remoteRmiScheme}://${");
            result.append(propertyPrefix);
            result.append("remoteServer}");

            // port
            if (hasServiceRemotePort())
            {
                result.append(":${");
                result.append(propertyPrefix);
                result.append("remotePort}");
            }

            // service name
            result.append("/remote");
            result.append(this.getManageableServiceName());
        }

        return result.toString();
    }

    protected String handleGetRemoteServer()
    {
        return StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(SpringGlobals.SERVICE_REMOTE_SERVER)));
    }

    protected String handleGetRemotePort()
    {
        final String serviceRemotePort =
            StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(SpringGlobals.SERVICE_REMOTE_PORT)));
        return SpringMetafacadeUtils.getServiceRemotePort(this, serviceRemotePort);
    }

    protected String handleGetRemoteContext()
    {
        return this.isConfiguredProperty(SpringGlobals.SERVICE_REMOTE_CONTEXT)
            ? ObjectUtils.toString(this.getConfiguredProperty(SpringGlobals.SERVICE_REMOTE_CONTEXT)) : "";
    }

    protected boolean handleIsRemotable()
    {
        return !this.isRemotingTypeNone();
    }

    /**
     * Gets the remoting type for this service.
     */
    private String getRemotingType()
    {
        final String serviceRemotingType =
            StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(SpringGlobals.SERVICE_REMOTING_TYPE)));
        return SpringMetafacadeUtils.getServiceRemotingType(this, serviceRemotingType);
    }

    /**
     * Checks whether this service has a remote port assigned.
     *
     * @return <code>true</code> if the service has a remote port, <code>false</code> otherwise.
     */
    private boolean hasServiceRemotePort()
    {
        return StringUtils.isNotEmpty(this.getRemotePort());
    }

    /**
     * Checks whether the service has a remote context assigned.
     *
     * @return <code>true</code> if the service has a remote context, <code>false</code> otherweise.
     */
    private boolean hasServiceRemoteContext()
    {
        return StringUtils.isNotEmpty(this.getRemoteContext());
    }

}