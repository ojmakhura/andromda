package org.andromda.cartridges.spring.metafacades;

import java.util.Arrays;
import java.util.Collection;
import org.andromda.cartridges.spring.CartridgeSpringProfile;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * Contains utilities specific to dealing with the Spring cartridge metafacades.
 *
 * @author Chad Brandon
 * @author Peter Friese
 */
class SpringMetafacadeUtils
{
    /**
     * Creates a fully qualified name from the given <code>packageName</code>, <code>name</code>, and
     * <code>suffix</code>.
     *
     * @param packageName the name of the model element package.
     * @param name        the name of the model element.
     * @param suffix      the suffix to append.
     * @return the new fully qualified name.
     */
    static String getFullyQualifiedName(String packageName, String name, String suffix)
    {
        StringBuilder fullyQualifiedName = new StringBuilder(StringUtils.trimToEmpty(packageName));
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append('.');
        }
        fullyQualifiedName.append(StringUtils.trimToEmpty(name));
        if (StringUtils.isNotBlank(suffix))
        {
            fullyQualifiedName.append(StringUtils.trimToEmpty(suffix));
        }
        return fullyQualifiedName.toString();
    }

    /**
     * Creates a fully qualified name from the given <code>packageName</code>, <code>name</code>, and
     * <code>suffix</code>.
     *
     * @param packageName the name of the model element package.
     * @param name        the name of the model element.
     * @return the new fully qualified name.
     */
    static String getFullyQualifiedName(String packageName, String name)
    {
        return getFullyQualifiedName(packageName, name, null);
    }

    /**
     * Gets the remoting type for the passed in <code>classifier</code>. If the remoting type can be retrieved from the
     * <code>classifier</code>, then that is used, otherwise the <code>defaultRemotingType</code> is returned.
     * @param classifier
     * @param defaultServiceRemotingType
     * @return String the remoting type name.
     */
    static String getServiceRemotingType(ClassifierFacade classifier, String defaultServiceRemotingType)
    {
        ExceptionUtils.checkNull("classifer", classifier);
        String remotingType = null;
        if (classifier.hasStereotype(UMLProfile.STEREOTYPE_SERVICE))
        {
            String remotingTypeValue = (String)classifier.findTaggedValue(
                    CartridgeSpringProfile.TAGGEDVALUE_SPRING_SERVICE_REMOTING_TYPE);
            // if the remoting type wasn't found, search all super classes
            if (StringUtils.isBlank(remotingTypeValue))
            {
                remotingType = (String)CollectionUtils.find(classifier.getAllGeneralizations(), new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        return ((ModelElementFacade)object).findTaggedValue(
                                CartridgeSpringProfile.TAGGEDVALUE_SPRING_SERVICE_REMOTING_TYPE) != null;
                    }
                });
            }
            if (StringUtils.isNotBlank(remotingTypeValue))
            {
                remotingType = remotingTypeValue;
            }
        }
        if (StringUtils.isBlank(remotingType) || remotingType == null)
        {
            remotingType = defaultServiceRemotingType;
        }
        return remotingType.toLowerCase().trim();
    }

    /**
     * Get the interceptors for the passed in <code>classifier</code>. If the interceptors can be retrieved from the
     * <code>classifier</code>, then these will be used, otherwise the <code>defaultInterceptors</code> are
     * returned.
     *
     * @param classifier the classifier whose interceptors we are looking for.
     * @param defaultInterceptors a list of interceptors to use if the classifier itself has no explicit interceptors.
     *
     * @return String[] the interceptors.
     */
    static Collection<String> getServiceInterceptors(ClassifierFacade classifier,
            Collection<String> defaultInterceptors)
    {
        ExceptionUtils.checkNull("classifier", classifier);
        Collection<String> interceptors = null;
        if (classifier.hasStereotype(UMLProfile.STEREOTYPE_SERVICE))
        {
            String interceptorsValue = (String)classifier.findTaggedValue(
                    CartridgeSpringProfile.TAGGEDVALUE_SPRING_SERVICE_INTERCEPTORS);
            // if the interceptors weren't found, search all super classes
            if (StringUtils.isBlank(interceptorsValue))
            {
                interceptorsValue = (String)CollectionUtils.find(classifier.getAllGeneralizations(), new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        return ((ModelElementFacade)object).findTaggedValue(
                                CartridgeSpringProfile.TAGGEDVALUE_SPRING_SERVICE_INTERCEPTORS) != null;
                    }
                });
            }
            // interceptors are a comma-separated list of strings, go and split the list
            if (StringUtils.isNotBlank(interceptorsValue))
            {
                interceptors = Arrays.asList(interceptorsValue.split(","));
            }
        }
        if (interceptors == null || interceptors.isEmpty())
        {
            interceptors = defaultInterceptors;
        }
        return interceptors;
    }

    /**
     * Gets the remote service port for the passed in <code>classifier</code>. If the remote service
     * port can be retrieved from the <code>classifier</code>, then that is used, otherwise the
     * <code>defaultRemoteServicePort</code> is returned.
     * @param classifier
     * @param defaultRemoteServicePort
     * @return String the remote service port.
     */
    static String getServiceRemotePort(ClassifierFacade classifier, String defaultRemoteServicePort)
    {
        ExceptionUtils.checkNull("classifer", classifier);
        String remoteServicePort = null;
        if (classifier.hasStereotype(UMLProfile.STEREOTYPE_SERVICE))
        {
            String remoteServicePortValue = (String)classifier.findTaggedValue(
                    CartridgeSpringProfile.TAGGEDVALUE_SPRING_SERVICE_REMOTE_PORT);
            // if the remote service port wasn't found, search all super classes
            if (StringUtils.isBlank(remoteServicePortValue))
            {
                remoteServicePort = (String)CollectionUtils.find(classifier.getAllGeneralizations(), new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        return ((ModelElementFacade)object).findTaggedValue(
                                CartridgeSpringProfile.TAGGEDVALUE_SPRING_SERVICE_REMOTE_PORT) != null;
                    }
                });
            }
            if (StringUtils.isNotBlank(remoteServicePortValue))
            {
                remoteServicePort = remoteServicePortValue;
            }
        }
        if (StringUtils.isBlank(remoteServicePort) || remoteServicePort == null)
        {
            remoteServicePort = defaultRemoteServicePort;
        }
        return remoteServicePort.toLowerCase().trim();
    }

    /**
     * Checks whether the passed in operation is a query and should be using named parameters.
     *
     * @param operation the operation.
     * @param defaultUseNamedParameters the default value.
     * @return whether named parameters should be used.
     */
    static boolean getUseNamedParameters(OperationFacade operation,
        boolean defaultUseNamedParameters)
    {
        ExceptionUtils.checkNull("operation", operation);
        boolean useNamedParameters = defaultUseNamedParameters;
        if (operation.isQuery())
        {
            String useNamedParametersValue = StringUtils.trimToEmpty((String)operation
                    .findTaggedValue(CartridgeSpringProfile.TAGGEDVALUE_HIBERNATE_USE_NAMED_PARAMETERS));
            if (StringUtils.isNotBlank(useNamedParametersValue))
            {
                useNamedParameters = Boolean.valueOf(useNamedParametersValue).booleanValue();
            }
        }
        return useNamedParameters;
    }
}