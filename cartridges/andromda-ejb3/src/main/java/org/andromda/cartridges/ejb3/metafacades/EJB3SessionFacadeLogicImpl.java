package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3SessionFacade.
 *
 * @see EJB3SessionFacade
 */
public class EJB3SessionFacadeLogicImpl
extends EJB3SessionFacadeLogic
{
    /**
     * The property which stores the pattern defining the service bean name.
     */
    public static final String SERVICE_NAME_PATTERN = "serviceNamePattern";

    /**
     * The property which stores the pattern defining the service bean parent interface name.
     */
    public static final String SERVICE_INTERFACE_NAME_PATTERN = "serviceInterfaceNamePattern";

    /**
     * The property which stores the pattern defining the service bean local interface name.
     */
    public static final String SERVICE_LOCAL_INTERFACE_NAME_PATTERN = "serviceLocalInterfaceNamePattern";

    /**
     * The property which stores the pattern defining the service bean remote interface name.
     */
    private static final String SERVICE_REMOTE_INTERFACE_NAME_PATTERN = "serviceRemoteInterfaceNamePattern";

    /**
     * The property which stores the pattern defining the service bean listener callback name.
     */
    private static final String SERVICE_LISTENER_NAME_PATTERN = "serviceListenerNamePattern";

    /**
     * The property which stores the pattern defining the service bean implementation name.
     */
    private static final String SERVICE_IMPLEMENTATION_NAME_PATTERN = "serviceImplementationNamePattern";

    /**
     * The property which stores the pattern defining the service bean delegate class name.
     */
    private static final String SERVICE_DELEGATE_NAME_PATTERN = "serviceDelegateNamePattern";

    /**
     * The property which stores the pattern defining the service bean base class name.
     */
    private static final String SERVICE_BASE_NAME_PATTERN = "serviceBaseNamePattern";

    /**
     * The property which stores the pattern defining the service bean test class name
     */
    private static final String SERVICE_TEST_NAME_PATTERN = "serviceTestNamePattern";

    /**
     * The property which stores the pattern defining the service test package
     */
    private static final String SERVICE_TEST_PACKAGE_NAME_PATTERN = "serviceTestPackageNamePattern";

    /**
     * The property which stores the pattern defining the default service bean
     * exception class name.
     */
    private static final String SERVICE_DEFAULT_EXCEPTION_NAME_PATTERN = "defaultServiceExceptionNamePattern";

    /**
     * The property that stores the persistence container name.
     */
    public static final String PERSISTENCE_CONTAINER = "persistenceContainerName";

    /**
     * The property which stores the persistence context unit name associated with the default
     * Entity Manager.
     */
    private static final String PERSISTENCE_CONTEXT_UNIT_NAME = "persistenceContextUnitName";

    /**
     * The default view type accessibility for the session bean.
     */
    public static final String SERVICE_DEFAULT_VIEW_TYPE = "serviceViewType";

    /**
     * The property that stores whether default service exceptions are permitted.
     */
    public static final String ALLOW_DEFAULT_SERVICE_EXCEPTION = "allowDefaultServiceException";

    /**
     * The property that stores the JNDI name prefix.
     */
    public static final String SERVICE_JNDI_NAME_PREFIX = "jndiNamePrefix";

    /**
     * The property that determines application wide clustering
     */
    public static final String SERVICE_ENABLE_CLUSTERING = "enableClustering";

    /**
     * The property that sets whether EJB 3.0 JSR 181 webservices is enabled
     */
    private static final String PROPERTY_WEBSERVICE_ENABLED = "webServiceEnabled";

    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public EJB3SessionFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    // ---------------- methods -------------------------------

    /**
     * @see EJB3SessionFacade#isSyntheticCreateMethodAllowed()
     */
    @Override
    protected boolean handleIsSyntheticCreateMethodAllowed()
    {
        return EJB3MetafacadeUtils.allowSyntheticCreateMethod(this);
    }

    /**
     * @return operations
     * @see EJB3SessionFacade#getBusinessOperations()
     */
    protected Collection handleGetBusinessOperations()
    {
        Collection operations = super.getOperations();
        CollectionUtils.filter(
                operations,
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        boolean businessOperation = false;
                        if (EJB3SessionOperationFacade.class.isAssignableFrom(object.getClass()))
                        {
                            businessOperation = ((EJB3SessionOperationFacade)object).isBusinessOperation();
                        }
                        return businessOperation;
                    }
                });
        return operations;
    }

    /**
     * @return EJB3MetafacadeUtils.getAllInstanceAttributes(this)
     * @see EJB3SessionFacade#getAllInstanceAttributes()
     */
    protected List handleGetAllInstanceAttributes()
    {
         return EJB3MetafacadeUtils.getAllInstanceAttributes(this);

        // Don't use the Metafacade util method since we want to invoke the implementation of
        // getInstanceAttributes from EJB3SessionFacade

//        List attributes = this.getInheritedInstanceAttributes();
//        attributes.addAll(this.getInstanceAttributes());
//        return attributes;
    }

    /**
     * @return EJB3MetafacadeUtils.getInheritedInstanceAttributes(this)
     * @see EJB3SessionFacade#getInheritedInstanceAttributes()
     */
    protected List handleGetInheritedInstanceAttributes()
    {
        return EJB3MetafacadeUtils.getInheritedInstanceAttributes(this);

        // Don't use the Metafacade util method since we want to invoke the implementation of
        // getInstanceAttributes from EJB3SessionFacade

//        EJB3SessionFacade current = (EJB3SessionFacade)this.getSuperClass();
//        if (current == null)
//        {
//            return new ArrayList();
//        }
//        List attributes = current.getInheritedInstanceAttributes();
//
//        if (current.getInstanceAttributes() != null)
//        {
//            attributes.addAll(current.getInstanceAttributes());
//        }
//        return attributes;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleFilterSeamAttributes(Collection)
     */
    protected Collection handleFilterSeamAttributes(Collection attributes)
    {
        CollectionUtils.filter(
                attributes,
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        boolean isSeamAttribute = false;
                        if (((EJB3SessionAttributeFacade)object).isSeamAttribute())
                        {
                            isSeamAttribute = true;
                        }
                        return !isSeamAttribute;
                    }
                });
        return attributes;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetJndiNameRemote()
     */
    @Override
    protected String handleGetJndiNameRemote()
    {
        String jndiName = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SESSION_JNDI_NAME_REMOTE);
        if (StringUtils.isNotBlank(jndiName))
        {
            jndiName = this.getJndiNamePrefix() + "/" + jndiName;
        }
        return jndiName;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetJndiNameLocal()
     */
    @Override
    protected String handleGetJndiNameLocal()
    {
        String jndiName = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SESSION_JNDI_NAME_Local);
        if (StringUtils.isNotBlank(jndiName))
        {
            jndiName = this.getJndiNamePrefix() + "/" + jndiName;
        }
        return jndiName;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetJndiNamePrefix()
     */
    @Override
    protected String handleGetJndiNamePrefix()
    {
        return this.isConfiguredProperty(SERVICE_JNDI_NAME_PREFIX) ?
                ObjectUtils.toString(this.getConfiguredProperty(SERVICE_JNDI_NAME_PREFIX)) : null;
    }

    /**
     * @see EJB3SessionFacade#isStateful()
     */
    @Override
    protected boolean handleIsStateful()
    {
        boolean isStateful = false;
        String sessionType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SESSION_TYPE);
        if (StringUtils.isBlank(sessionType))
        {
            isStateful = !isStateless();
        }
        else
        {
            isStateful = sessionType.equalsIgnoreCase(EJB3Globals.SERVICE_TYPE_STATEFUL);
        }
        return isStateful;
    }

    /**
     * @see EJB3SessionFacade#isStateless()
     */
    @Override
    protected boolean handleIsStateless()
    {
        boolean isStateless = false;
        String sessionType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SESSION_TYPE);
        if (StringUtils.isBlank(sessionType))
        {
            isStateless =
                this.getAllInstanceAttributes() == null ||
                    this.filterSeamAttributes(this.getAllInstanceAttributes()).isEmpty();
        }
        else
        {
            isStateless = sessionType.equalsIgnoreCase(EJB3Globals.SERVICE_TYPE_STATELESS);
        }
        return isStateless;
    }

    /**
     * @see EJB3SessionFacade#getType()
     */
    @Override
    protected String handleGetType()
    {
        String sessionType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SESSION_TYPE);
        if (StringUtils.isBlank(sessionType))
        {
            sessionType = "Stateful";
            if (this.isStateless())
            {
                sessionType = "Stateless";
            }
        }
        return sessionType;
    }

    /**
     * @see EJB3SessionFacade#getViewType()
     */
    @Override
    protected String handleGetViewType()
    {
        String viewType = EJB3MetafacadeUtils.getViewType(this,
                String.valueOf(this.getConfiguredProperty(SERVICE_DEFAULT_VIEW_TYPE)));

        /**
         * Check all session bean operations.
         * If session view type is remote, check for operations with view type local and return both.
         * If session view type is local, check for operations with view type remote and return both.
         * Otherwise session view type is both, return both.
         *
         * NOTE: do not invoke viewType on EJB3SessionOperationFacade to avoid cyclic dependency and throwing
         * StackOverFlowError.
         */
        if (viewType.equalsIgnoreCase(EJB3Globals.VIEW_TYPE_LOCAL))
        {
            boolean operationWithRemoteViewExists =
                CollectionUtils.exists(
                        this.getBusinessOperations(),
                        new Predicate()
                        {
                            public boolean evaluate(Object object)
                            {
                                EJB3SessionOperationFacade operation = (EJB3SessionOperationFacade)object;
                                String operationViewType =
                                    String.valueOf(operation.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_VIEWTYPE));
                                if (operationViewType.equalsIgnoreCase(EJB3Globals.VIEW_TYPE_REMOTE) ||
                                        operationViewType.equalsIgnoreCase(EJB3Globals.VIEW_TYPE_BOTH))
                                {
                                    return true;
                                }
                                else
                                {
                                    return false;
                                }
                            }
                        });
            viewType = (operationWithRemoteViewExists ? EJB3Globals.VIEW_TYPE_BOTH : viewType);
        }
        else if (viewType.equalsIgnoreCase(EJB3Globals.VIEW_TYPE_REMOTE))
        {
            boolean operationWithLocalViewExists =
                CollectionUtils.exists(
                        this.getBusinessOperations(),
                        new Predicate()
                        {
                            public boolean evaluate(Object object)
                            {
                                EJB3SessionOperationFacade operation = (EJB3SessionOperationFacade)object;
                                String operationViewType =
                                    String.valueOf(operation.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_VIEWTYPE));
                                if (operationViewType.equalsIgnoreCase(EJB3Globals.VIEW_TYPE_LOCAL) ||
                                        operationViewType.equalsIgnoreCase(EJB3Globals.VIEW_TYPE_BOTH))
                                {
                                    return true;
                                }
                                else
                                {
                                    return false;
                                }
                            }
                        });
            viewType = (operationWithLocalViewExists ? EJB3Globals.VIEW_TYPE_BOTH : viewType);
        }
        return viewType;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsViewTypeLocal()
     */
    @Override
    protected boolean handleIsViewTypeLocal()
    {
        boolean isLocal = false;
        if (this.getViewType().equalsIgnoreCase(EJB3Globals.VIEW_TYPE_LOCAL) || this.isViewTypeBoth() ||
                this.isSeamComponent())
        {
            isLocal = true;
        }
        return isLocal;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsViewTypeRemote()
     */
    @Override
    protected boolean handleIsViewTypeRemote()
    {
        boolean isRemote = false;
        if (this.getViewType().equalsIgnoreCase(EJB3Globals.VIEW_TYPE_REMOTE) || this.isViewTypeBoth())
        {
            isRemote = true;
        }
        return isRemote;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsViewTypeBoth()
     */
    @Override
    protected boolean handleIsViewTypeBoth()
    {
        boolean isBoth = false;
        if (this.getViewType().equalsIgnoreCase(EJB3Globals.VIEW_TYPE_BOTH))
        {
            isBoth = true;
        }
        return isBoth;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsViewTypeStrictlyLocal()
     */
    @Override
    protected boolean handleIsViewTypeStrictlyLocal()
    {

        boolean isViewTypeStrictlyLocal = false;
        String viewType = EJB3MetafacadeUtils.getViewType(this,
                String.valueOf(this.getConfiguredProperty(SERVICE_DEFAULT_VIEW_TYPE)));
        if (StringUtils.equalsIgnoreCase(viewType, EJB3Globals.VIEW_TYPE_LOCAL) ||
                StringUtils.equalsIgnoreCase(viewType, EJB3Globals.VIEW_TYPE_BOTH))
        {
            isViewTypeStrictlyLocal = true;
        }
        return isViewTypeStrictlyLocal;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsViewTypeStrictlyRemote()
     */
    @Override
    protected boolean handleIsViewTypeStrictlyRemote()
    {
        boolean isViewTypeStrictlyRemote = false;
        String viewType = EJB3MetafacadeUtils.getViewType(this,
                String.valueOf(this.getConfiguredProperty(SERVICE_DEFAULT_VIEW_TYPE)));
        if (StringUtils.equalsIgnoreCase(viewType, EJB3Globals.VIEW_TYPE_REMOTE) ||
                StringUtils.equalsIgnoreCase(viewType, EJB3Globals.VIEW_TYPE_BOTH))
        {
            isViewTypeStrictlyRemote = true;
        }
        return isViewTypeStrictlyRemote;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsViewTypeStrictlyBoth()
     */
    @Override
    protected boolean handleIsViewTypeStrictlyBoth()
    {
        boolean isViewTypeStrictlyBoth = false;
        String viewType = EJB3MetafacadeUtils.getViewType(this,
                String.valueOf(this.getConfiguredProperty(SERVICE_DEFAULT_VIEW_TYPE)));
        if (StringUtils.equalsIgnoreCase(viewType, EJB3Globals.VIEW_TYPE_BOTH))
        {
            isViewTypeStrictlyBoth = true;
        }
        return isViewTypeStrictlyBoth;
    }

    /**
     * @see EJB3SessionFacade#getHomeInterfaceName()
     */
    @Override
    protected String handleGetHomeInterfaceName()
    {
        return EJB3MetafacadeUtils.getHomeInterfaceName(this);
    }

    /**
     * @see EJB3SessionFacade#getTransactionType()
     */
    @Override
    protected String handleGetTransactionType()
    {
        return EJB3MetafacadeUtils.getTransactionType(this, null);
    }

    /**
     * @param follow 
     * @return EJB3MetafacadeUtils.getCreateMethods(this, follow)
     * @see EJB3SessionFacade#getCreateMethods(boolean)
     */
    protected Collection handleGetCreateMethods(boolean follow)
    {
        return EJB3MetafacadeUtils.getCreateMethods(this, follow);
    }

    /**
     * @param follow 
     * @return EJB3MetafacadeUtils.getEnvironmentEntries(this, follow)
     * @see EJB3SessionFacade#getEnvironmentEntries(boolean)
     */
    protected Collection handleGetEnvironmentEntries(boolean follow)
    {
        return EJB3MetafacadeUtils.getEnvironmentEntries(this, follow);
    }

    /**
     * @param follow 
     * @return EJB3MetafacadeUtils.getConstants(this, follow)
     * @see EJB3SessionFacade#getConstants(boolean)
     */
    protected Collection handleGetConstants(boolean follow)
    {
        return EJB3MetafacadeUtils.getConstants(this, follow);
    }

    /**
     * @see EJB3SessionFacade#getTestPackageName
     */
    @Override
    protected String handleGetTestPackageName()
    {
        String namespacePattern = String.valueOf(this.getConfiguredProperty(SERVICE_TEST_PACKAGE_NAME_PATTERN));
        return MessageFormat.format(
                namespacePattern,
                this.getPackageName());
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetServiceName()
     */
    @Override
    protected String handleGetServiceName()
    {
        String serviceNamePattern = (String)this.getConfiguredProperty(SERVICE_NAME_PATTERN);

        return MessageFormat.format(
                serviceNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetServiceInterfaceName()
     */
    @Override
    protected String handleGetServiceInterfaceName()
    {
        String serviceInterfaceNamePattern =
            (String)this.getConfiguredProperty(SERVICE_INTERFACE_NAME_PATTERN);

        return MessageFormat.format(
                serviceInterfaceNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetServiceLocalInterfaceName()
     */
    @Override
    protected String handleGetServiceLocalInterfaceName()
    {
        String serviceLocalInterfaceNamePattern =
            (String)this.getConfiguredProperty(SERVICE_LOCAL_INTERFACE_NAME_PATTERN);

        return MessageFormat.format(
                serviceLocalInterfaceNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetServiceRemoteInterfaceName()
     */
    @Override
    protected String handleGetServiceRemoteInterfaceName()
    {
        String serviceRemoteInterfaceNamePattern =
            (String)this.getConfiguredProperty(SERVICE_REMOTE_INTERFACE_NAME_PATTERN);

        return MessageFormat.format(
                serviceRemoteInterfaceNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetServiceImplementationName()
     */
    @Override
    protected String handleGetServiceImplementationName()
    {
        String serviceImplementationNamePattern =
            (String)this.getConfiguredProperty(SERVICE_IMPLEMENTATION_NAME_PATTERN);

        return MessageFormat.format(
                serviceImplementationNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetServiceListenerName()
     */
    @Override
    protected String handleGetServiceListenerName()
    {
        String serviceListenerNamePattern =
            (String)this.getConfiguredProperty(SERVICE_LISTENER_NAME_PATTERN);

        return MessageFormat.format(
                serviceListenerNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }


    /**
     * @see EJB3SessionFacadeLogic#handleGetServiceDelegateName()
     */
    @Override
    protected String handleGetServiceDelegateName()
    {
        String serviceDelegateNamePattern =
            (String)this.getConfiguredProperty(SERVICE_DELEGATE_NAME_PATTERN);

        return MessageFormat.format(
                serviceDelegateNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetServiceBaseName()
     */
    @Override
    protected String handleGetServiceBaseName()
    {
        String serviceBaseNamePattern =
            (String)this.getConfiguredProperty(SERVICE_BASE_NAME_PATTERN);

        return MessageFormat.format(
                serviceBaseNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetServiceTestName()
     */
    @Override
    protected String handleGetServiceTestName()
    {
        String serviceTestNamePattern =
            (String)this.getConfiguredProperty(SERVICE_TEST_NAME_PATTERN);

        return MessageFormat.format(
                serviceTestNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceName()
     */
    @Override
    protected String handleGetFullyQualifiedServiceName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceName(),
                null);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceImplementationName()
     */
    @Override
    protected String handleGetFullyQualifiedServiceImplementationName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceImplementationName(),
                null);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceListenerName()
     */
    @Override
    protected String handleGetFullyQualifiedServiceListenerName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceListenerName(),
                null);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceInterfaceName()
     */
    @Override
    protected String handleGetFullyQualifiedServiceInterfaceName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceInterfaceName(),
                null);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceLocalInterfaceName()
     */
    @Override
    protected String handleGetFullyQualifiedServiceLocalInterfaceName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceLocalInterfaceName(),
                null);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceRemoteInterfaceName()
     */
    @Override
    protected String handleGetFullyQualifiedServiceRemoteInterfaceName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceRemoteInterfaceName(),
                null);
    }


    /**
     * @see EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceDelegateName()
     */
    @Override
    protected String handleGetFullyQualifiedServiceDelegateName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceDelegateName(),
                null);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceBaseName()
     */
    @Override
    protected String handleGetFullyQualifiedServiceBaseName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getServiceBaseName(),
                null);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetFullyQualifiedServiceTestName()
     */
    @Override
    protected String handleGetFullyQualifiedServiceTestName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getTestPackageName(),
                this.getServiceTestName(),
                null);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetPersistenceContextUnitName()
     */
    @Override
    protected String handleGetPersistenceContextUnitName()
    {
        String unitName = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_UNIT_NAME);
        if (StringUtils.isBlank(unitName))
        {
            unitName = StringUtils.trimToEmpty(
                    ObjectUtils.toString(this.getConfiguredProperty(PERSISTENCE_CONTEXT_UNIT_NAME)));
        }
        return unitName;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetPersistenceContextType()
     */
    @Override
    protected String handleGetPersistenceContextType()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_TYPE);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetPersistenceContextReferences()
     */
    protected Collection handleGetPersistenceContextReferences()
    {
        Collection references = this.getSourceDependencies();
        CollectionUtils.filter(
                references,
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                        return (targetElement != null
                                && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_PERSISTENCE_CONTEXT));
                    }
                });
        CollectionUtils.transform(
                references,
                new Transformer()
                {
                    public Object transform(final Object object)
                    {
                        return ((DependencyFacade)object).getTargetElement();
                    }
                });
        return references;
    }

    /**
     * @see EJB3SessionFacadeLogic#getServiceReferences()
     *
     * Returns the Collection of DependencyFacades where the target is a Service ONLY.
     */
    public Collection getServiceReferences()
    {
        Collection references = super.getServiceReferences();
        CollectionUtils.filter(
                references,
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                        return targetElement.hasStereotype(EJB3Profile.STEREOTYPE_SERVICE);
                    }
                });
        return references;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetAttributesAsList(Collection, boolean, boolean)
     */
    @Override
    protected String handleGetAttributesAsList(
            Collection attributes,
            boolean includeTypes,
            boolean includeNames)
    {
        if (!includeNames && !includeTypes || attributes == null)
        {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        String separator = "";

        for (final Iterator it = attributes.iterator(); it.hasNext();)
        {
            EJB3SessionAttributeFacade attr = (EJB3SessionAttributeFacade)it.next();
            sb.append(separator);
            separator = ", ";
            if (includeTypes)
            {
                sb.append(attr.getType().getFullyQualifiedName());
                sb.append(" ");
            }
            if (includeNames)
            {
                sb.append(attr.getName());
            }
        }
        return sb.toString();
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetPersistenceContainer()
     */
    @Override
    protected String handleGetPersistenceContainer()
    {
        return StringUtils.trimToEmpty(
                ObjectUtils.toString(this.getConfiguredProperty(PERSISTENCE_CONTAINER)));
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsPersistenceContainerJboss()
     */
    @Override
    protected boolean handleIsPersistenceContainerJboss()
    {
        return getPersistenceContainer().equalsIgnoreCase(EJB3Globals.PERSISTENCE_CONTAINER_JBOSS);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsPersistenceContainerWeblogic()
     */
    @Override
    protected boolean handleIsPersistenceContainerWeblogic()
    {
        return getPersistenceContainer().equalsIgnoreCase(EJB3Globals.PERSISTENCE_CONTAINER_WEBLOGIC);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetRolesAllowed()
     */
    @Override
    protected String handleGetRolesAllowed()
    {
        StringBuilder rolesAllowed = null;
        String separator = "";

        for (final Iterator iter = this.getNonRunAsRoles().iterator(); iter.hasNext(); )
        {
            if (rolesAllowed == null)
            {
                rolesAllowed = new StringBuilder();
            }
            rolesAllowed.append(separator);
            Role role = (Role)iter.next();
            rolesAllowed.append('"');
            rolesAllowed.append(role.getName());
            rolesAllowed.append('"');
            separator = ", ";
        }
        return rolesAllowed != null ? rolesAllowed.toString() : null;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsPermitAll()
     */
    @Override
    protected boolean handleIsPermitAll()
    {
        boolean permitAll = false;
        String permitAllStr = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SECURITY_PERMIT_ALL);
        if (StringUtils.isNotBlank(permitAllStr))
        {
            permitAll = BooleanUtils.toBoolean(permitAllStr);
        }
        return permitAll;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsDenyAll()
     */
    @Override
    protected boolean handleIsDenyAll()
    {
        boolean denyAll = false;
        String denyAllStr = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SECURITY_DENY_ALL);
        if (StringUtils.isNotBlank(denyAllStr))
        {
            denyAll = BooleanUtils.toBoolean(denyAllStr);
        }
        return denyAll;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetSecurityRealm()
     */
    @Override
    protected String handleGetSecurityRealm()
    {
        String securityRealm = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SECURITY_REALM);
        if (StringUtils.isBlank(securityRealm))
        {
            securityRealm = StringUtils.trimToEmpty(
                    ObjectUtils.toString(this.getConfiguredProperty(EJB3Globals.SECURITY_REALM)));
        }
        return securityRealm;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsSecurityEnabled()
     */
    @Override
    protected boolean handleIsSecurityEnabled()
    {
        return StringUtils.isNotBlank(this.getSecurityRealm());
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetRunAs()
     */
    @Override
    protected String handleGetRunAs()
    {
        String runAsRole = null;
        DependencyFacade dependency = (DependencyFacade)CollectionUtils.find(
                this.getTargetDependencies(),
                new Predicate()
                {
                    public boolean evaluate(final Object object)
                    {
                        DependencyFacade dependency = (DependencyFacade)object;
                        return dependency != null
                        && dependency.getSourceElement() != null
                        && dependency.getSourceElement() instanceof Role
                        && dependency.hasStereotype(EJB3Profile.STEREOTYPE_SECURITY_RUNAS);
                    }
                });
        if (dependency != null)
        {
            Role role = (Role)dependency.getSourceElement();
            runAsRole = role.getName();
        }
        return runAsRole;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetTransactionManagement()
     */
    @Override
    protected String handleGetTransactionManagement()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_MANAGEMENT);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsTransactionManagementBean()
     */
    @Override
    protected boolean handleIsTransactionManagementBean()
    {
        return StringUtils.equalsIgnoreCase(getTransactionManagement(), EJB3Globals.TRANSACTION_MANAGEMENT_BEAN);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetResourceUserTransactionReferences()
     */
    protected Collection handleGetResourceUserTransactionReferences()
    {
        Collection references = this.getSourceDependencies();
        CollectionUtils.filter(
                references,
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        DependencyFacade dependency = (DependencyFacade)object;
                        ModelElementFacade targetElement = dependency.getTargetElement();
                        return (targetElement != null
                                && EJB3SessionFacade.class.isAssignableFrom(targetElement.getClass())
                                && dependency.hasStereotype(EJB3Profile.STEREOTYPE_RESOURCE_REF)
                                && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_USER_TRANSACTION));
                    }
                });
        return references;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetResourceDataSourceReferences()
     */
    protected Collection handleGetResourceDataSourceReferences()
    {
        Collection references = this.getSourceDependencies();
        CollectionUtils.filter(
                references,
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        DependencyFacade dependency = (DependencyFacade)object;
                        ModelElementFacade targetElement = dependency.getTargetElement();
                        return (targetElement != null
                                && EJB3SessionFacade.class.isAssignableFrom(targetElement.getClass())
                                && dependency.hasStereotype(EJB3Profile.STEREOTYPE_RESOURCE_REF)
                                && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_DATA_SOURCE));
                    }
                });
        return references;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetMessageDrivenReferences()
     */
    protected Collection handleGetMessageDrivenReferences()
    {
        Collection references = this.getSourceDependencies();
        CollectionUtils.filter(references, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                DependencyFacade dependency = (DependencyFacade)object;
                ModelElementFacade targetElement = dependency.getTargetElement();
                return (targetElement != null
                        && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_MESSAGE_DRIVEN));
            }
        });
        return references;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetInterceptorReferences()
     */
    protected Collection handleGetInterceptorReferences()
    {
        Collection references = this.getSourceDependencies();
        CollectionUtils.filter(
                references,
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        DependencyFacade dependency = (DependencyFacade)object;
                        ModelElementFacade targetElement = dependency.getTargetElement();
                        return (targetElement != null &&
                                targetElement.hasStereotype(EJB3Profile.STEREOTYPE_INTERCEPTOR));
                    }
                });
        CollectionUtils.transform(
                references,
                new Transformer()
                {
                    public Object transform(final Object object)
                    {
                        return ((DependencyFacade)object).getTargetElement();
                    }
                });
        final Collection interceptors = new LinkedHashSet(references);
        CollectionUtils.forAllDo(
                references,
                new Closure()
                {
                    public void execute(Object object)
                    {
                        if (object instanceof EJB3InterceptorFacade)
                        {
                            interceptors.addAll(((EJB3InterceptorFacade)object).getInterceptorReferences());
                        }
                    }
                });
        return interceptors;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetNonRunAsRoles()
     */
    protected Collection handleGetNonRunAsRoles()
    {
        Collection roles = this.getTargetDependencies();
        CollectionUtils.filter(
                roles,
                new Predicate()
                {
                    public boolean evaluate(final Object object)
                    {
                        DependencyFacade dependency = (DependencyFacade)object;
                        return dependency != null
                        && dependency.getSourceElement() != null
                        && dependency.getSourceElement() instanceof Role
                        && !dependency.hasStereotype(EJB3Profile.STEREOTYPE_SECURITY_RUNAS);
                    }
                });
        CollectionUtils.transform(
                roles,
                new Transformer()
                {
                    public Object transform(final Object object)
                    {
                        return ((DependencyFacade)object).getSourceElement();
                    }
                });
        final Collection allRoles = new LinkedHashSet(roles);
        // add all roles which are generalizations of this one
        CollectionUtils.forAllDo(
                roles,
                new Closure()
                {
                    public void execute(final Object object)
                    {
                        allRoles.addAll(((Role)object).getAllSpecializations());
                    }
                });
        return allRoles;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetDefaultExceptionName()
     */
    @Override
    protected String handleGetDefaultExceptionName()
    {
        String defaultExceptionNamePattern =
            (String)this.getConfiguredProperty(SERVICE_DEFAULT_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                defaultExceptionNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetFullyQualifiedDefaultExceptionName()
     */
    @Override
    protected String handleGetFullyQualifiedDefaultExceptionName()
    {
        StringBuilder fullyQualifiedName = new StringBuilder("RuntimeException");
        if (this.isAllowDefaultServiceException())
        {
            fullyQualifiedName = new StringBuilder();
            if (StringUtils.isNotBlank(this.getPackageName()))
            {
                fullyQualifiedName.append(this.getPackageName());
                fullyQualifiedName.append('.');
            }
            fullyQualifiedName.append(this.getDefaultExceptionName());
        }
        return fullyQualifiedName.toString();
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsAllowDefaultServiceException()
     */
    @Override
    protected boolean handleIsAllowDefaultServiceException()
    {
        return Boolean.valueOf(
                String.valueOf(this.getConfiguredProperty(ALLOW_DEFAULT_SERVICE_EXCEPTION))).booleanValue();
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsListenerEnabled()
     */
    @Override
    protected boolean handleIsListenerEnabled()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_LISTENER);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsExcludeDefaultInterceptors()
     */
    @Override
    protected boolean handleIsExcludeDefaultInterceptors()
    {
        boolean excludeDefault = false;
        String excludeDefaultStr =
            (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SERVICE_INTERCEPTOR_EXCLUDE_DEFAULT);
        if (excludeDefaultStr != null)
        {
            excludeDefault = BooleanUtils.toBoolean(excludeDefaultStr);
        }
        return excludeDefault;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsClusteringEnabled()
     */
    @Override
    protected boolean handleIsClusteringEnabled()
    {
        return BooleanUtils.toBoolean(String.valueOf(this.getConfiguredProperty(SERVICE_ENABLE_CLUSTERING)));
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsWebServiceEnabled()
     */
    @Override
    protected boolean handleIsWebServiceEnabled()
    {
        return (this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE) || this.isWebServiceOperationExists()) &&
        BooleanUtils.toBoolean(String.valueOf(this.getConfiguredProperty(PROPERTY_WEBSERVICE_ENABLED)));
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsWebServiceOperationExists()
     */
    @Override
    protected boolean handleIsWebServiceOperationExists()
    {
        return CollectionUtils.find(
                this.getOperations(),
                new Predicate()
                {
                    public boolean evaluate(final Object object)
                    {
                        boolean isWebService = false;
                        final OperationFacade operation = (OperationFacade)object;
                        if (operation.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION))
                        {
                            isWebService = true;
                        }
                        return isWebService;
                    }
                }) != null;
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsSeamComponent()
     */
    @Override
    protected boolean handleIsSeamComponent()
    {
        return EJB3MetafacadeUtils.isSeamComponent(this);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetSeamComponentScopeType()
     */
    @Override
    protected String handleGetSeamComponentScopeType()
    {
        return EJB3MetafacadeUtils.getSeamComponentScopeType(this, this.isStateless());
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetSeamComponentName()
     */
    @Override
    protected String handleGetSeamComponentName()
    {
        return EJB3MetafacadeUtils.getSeamComponentName(this);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetSeamComponentConversionalIfNotBegunOutcome()
     */
    @Override
    protected String handleGetSeamComponentConversionalIfNotBegunOutcome()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_COMPONENT_CONVERSIONAL_IFNOTBEGUNOUTCOME);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetSeamComponentIntercept()
     */
    @Override
    protected String handleGetSeamComponentIntercept()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_COMPONENT_INTERCEPT);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetSeamComponentJndiName()
     */
    @Override
    protected String handleGetSeamComponentJndiName()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_COMPONENT_JNDI_NAME);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetSeamComponentRoleNames()
     */
    protected Collection handleGetSeamComponentRoleNames()
    {
        return this.findTaggedValues(EJB3Profile.TAGGEDVALUE_SEAM_COMPONENT_ROLE_NAME);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetSeamComponentRoleScopeTypes()
     */
    protected Collection handleGetSeamComponentRoleScopeTypes()
    {
        return this.findTaggedValues(EJB3Profile.TAGGEDVALUE_SEAM_COMPONENT_ROLE_SCOPE_TYPE);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetSeamComponentStartupParameters()
     */
    @Override
    protected String handleGetSeamComponentStartupParameters()
    {
        Collection depends = this.findTaggedValues(EJB3Profile.TAGGEDVALUE_SEAM_COMPONENT_STARTUP_DEPENDS);
        if(depends.isEmpty())
        {
            return null;
        }
        else
        {
            StringBuilder buf = new StringBuilder();
            buf.append("(depends = {");
            Iterator it = depends.iterator();
            while(it.hasNext())
            {
                String dependency = (String) it.next();
                buf.append("\"");
                buf.append(dependency);
                buf.append("\"");
                if(it.hasNext())
                {
                    buf.append(", ");
                }
            }
            buf.append("})");
            return buf.toString();
        }
    }

    /**
     * @see EJB3SessionFacadeLogic#handleGetSeamComponentSynchronizedTimeout()
     */
    @Override
    protected String handleGetSeamComponentSynchronizedTimeout()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_COMPONENT_SYNCHRONIZED_TIMEOUT);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsSeamComponentReadonly()
     */
    @Override
    protected boolean handleIsSeamComponentReadonly()
    {
        return BooleanUtils.toBoolean((String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_COMPONENT_READONLY));
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsSeamComponentStartup()
     */
    @Override
    protected boolean handleIsSeamComponentStartup()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_COMPONENT_STARTUP);
    }

    /**
     * @see EJB3SessionFacadeLogic#handleIsSeamComponentTransactional()
     */
    @Override
    protected boolean handleIsSeamComponentTransactional()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_TRANSACTION_TRANSACTIONAL);
    }
}
