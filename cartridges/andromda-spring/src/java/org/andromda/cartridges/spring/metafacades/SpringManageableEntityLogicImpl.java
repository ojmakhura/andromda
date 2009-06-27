package org.andromda.cartridges.spring.metafacades;

import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.ObjectUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringManageableEntity.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntity
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
        return getName() + this.getConfiguredProperty(SpringGlobals.CRUD_VALUE_OBJECT_SUFFIX);
    }

    protected String handleGetFullyQualifiedManageableValueObjectName()
    {
        return getManageablePackageName() + getNamespaceProperty() + getManageableValueObjectClassName();
    }

    protected boolean handleIsRemotingTypeRmi()
    {
        return this.getRemotingType().equalsIgnoreCase(SpringGlobals.REMOTING_PROTOCOL_RMI);
    }

    protected boolean handleIsRemotingTypeNone()
    {
        return this.getRemotingType().equalsIgnoreCase(SpringGlobals.REMOTING_PROTOCOL_NONE);
    }

    protected boolean handleIsRemotingTypeHttpInvoker()
    {
        return this.getRemotingType().equalsIgnoreCase(SpringGlobals.REMOTING_PROTOCOL_HTTPINVOKER);
    }

    protected boolean handleIsRemotingTypeHessian()
    {
        return this.getRemotingType().equalsIgnoreCase(SpringGlobals.REMOTING_PROTOCOL_HESSIAN);
    }

    protected boolean handleIsRemotingTypeBurlap()
    {
        return this.getRemotingType().equalsIgnoreCase(SpringGlobals.REMOTING_PROTOCOL_BURLAP);
    }

    protected String handleGetRemoteUrl()
    {
        final StringBuffer result = new StringBuffer();

        String propertyPrefix = ObjectUtils.toString(this.getConfiguredProperty(SpringGlobals.CONFIG_PROPERTY_PREFIX));

        if (this.isRemotingTypeNone())
        {
            // nothing
        }
        else if (this.isRemotingTypeHttpInvoker() || this.isRemotingTypeHessian() || this.isRemotingTypeBurlap())
        {
            // server
            result.append("${" + propertyPrefix + "remoteHttpScheme}://${");
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
            result.append("${" + propertyPrefix + "remoteRmiScheme}://${");
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