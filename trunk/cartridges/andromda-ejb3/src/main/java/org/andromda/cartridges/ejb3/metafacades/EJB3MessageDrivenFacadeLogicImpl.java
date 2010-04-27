package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Role;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade.
 *
 * @see EJB3MessageDrivenFacade
 */
public class EJB3MessageDrivenFacadeLogicImpl
    extends EJB3MessageDrivenFacadeLogic
{
    /**
     * The property which stores the default destination type
     */
    public static final String MESSAGE_DRIVEN_DESTINATION_TYPE = "messageDrivenDestinationType";

    /**
     * The property which stores the default subscription durability for a Topic
     */
    public static final String MESSAGE_DRIVEN_TOPIC_SUBSCRIPTION_DURABILITY =
        "messageDrivenTopicSubscriptionDurability";

    /**
     * The property which stores the pattern defining the JMS message driven bean name.
     */
    public static final String MESSAGE_DRIVEN_NAME_PATTERN = "messageDrivenNamePattern";

    /**
     * The property which stores the pattern defining the JMS message driven bean listener callback name.
     */
    private static final String MESSAGE_DRIVEN_LISTENER_NAME_PATTERN = "messageDrivenListenerNamePattern";

    /**
     * The property which stores the pattern defining the JMS message driven bean implementation name.
     */
    private static final String MESSAGE_DRIVEN_IMPLEMENTATION_NAME_PATTERN = "messageDrivenImplementationNamePattern";

    /**
     * The property which stores the pattern defining the JMS message driven bean test class name
     */
    private static final String MESSAGE_DRIVEN_TEST_NAME_PATTERN = "messageDrivenTestNamePattern";

    /**
     * The property which stores the pattern defining the JMS message driven bean test package
     */
    private static final String MESSAGE_DRIVEN_TEST_PACKAGE_NAME_PATTERN = "messageDrivenTestPackageNamePattern";

    /**
     * The property which stores the pattern defining the JMS durable subscription ID
     */
    private static final String MESSAGE_DRIVEN_DURABLE_SUBSCRIPTION_ID_PATTERN =
        "messageDrivenDurableSubscriptionIdPattern";

    //  ---------------- constructor -------------------------------

    public EJB3MessageDrivenFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    //  ---------------- methods -------------------------------

    /**
     * @see EJB3MessageDrivenFacade#getAcknowledgeMode()
     */
    @Override
    protected String handleGetAcknowledgeMode()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_MDB_ACKNOWLEDGE_MODE);
    }

    /**
     * @see EJB3MessageDrivenFacade#getDestination()
     */
    @Override
    protected String handleGetDestination()
    {
        String destination = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_MDB_DESTINATION);
        if (StringUtils.isBlank(destination))
        {
            destination = (getDestinationType().equalsIgnoreCase(EJB3Globals.MDB_DESTINATION_TYPE_TOPIC) ?
                    "topic/" : "queue/") + getMessageDrivenName();
        }
        return destination;
    }

    /**
     * @see EJB3MessageDrivenFacade#getDestinationType()
     */
    @Override
    protected String handleGetDestinationType()
    {
        String destinationType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_MDB_DESTINATION_TYPE);
        if (StringUtils.isBlank(destinationType))
        {
            destinationType = (String)this.getConfiguredProperty(MESSAGE_DRIVEN_DESTINATION_TYPE);
        }

        /**
         * Formatting required to replace '_' with '.' and return correct casing
         */
        destinationType = StringUtils.replaceChars(destinationType, '_', '.');
        if (destinationType.equalsIgnoreCase(EJB3Globals.MDB_DESTINATION_TYPE_QUEUE))
        {
            destinationType = EJB3Globals.MDB_DESTINATION_TYPE_QUEUE;
        }
        else if (destinationType.equalsIgnoreCase(EJB3Globals.MDB_DESTINATION_TYPE_TOPIC))
        {
            destinationType = EJB3Globals.MDB_DESTINATION_TYPE_TOPIC;
        }
        return destinationType;
    }

    /**
     * @see EJB3MessageDrivenFacadeLogic#handleGetTestPackageName()
     */
    @Override
    protected String handleGetTestPackageName()
    {
        String namespacePattern = String.valueOf(this.getConfiguredProperty(MESSAGE_DRIVEN_TEST_PACKAGE_NAME_PATTERN));
        return MessageFormat.format(
                namespacePattern,
                this.getPackageName());
    }

    /**
     * @see EJB3MessageDrivenFacade#getFullyQualifiedMessageDrivenImplementationName()
     */
    @Override
    protected String handleGetFullyQualifiedMessageDrivenImplementationName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getMessageDrivenImplementationName(),
                null);
    }

    /**
     * @see EJB3MessageDrivenFacade#getFullyQualifiedMessageDrivenListenerName()
     */
    @Override
    protected String handleGetFullyQualifiedMessageDrivenListenerName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getMessageDrivenListenerName(),
                null);
    }

    /**
     * @see EJB3MessageDrivenFacade#getFullyQualifiedMessageDrivenName()
     */
    @Override
    protected String handleGetFullyQualifiedMessageDrivenName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getMessageDrivenName(),
                null);
    }

    /**
     * @see EJB3MessageDrivenFacadeLogic#handleGetFullyQualifiedMessageDrivenTestName()
     */
    @Override
    protected String handleGetFullyQualifiedMessageDrivenTestName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getTestPackageName(),
                this.getMessageDrivenTestName(),
                null);
    }

    /**
     * @see EJB3MessageDrivenFacade#getMessageDrivenImplementationName()
     */
    @Override
    protected String handleGetMessageDrivenImplementationName()
    {
        String messageDrivenImplNamePattern =
            (String)this.getConfiguredProperty(MESSAGE_DRIVEN_IMPLEMENTATION_NAME_PATTERN);

        return MessageFormat.format(
                messageDrivenImplNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3MessageDrivenFacade#getMessageDrivenListenerName()
     */
    @Override
    protected String handleGetMessageDrivenListenerName()
    {
        String messageDrivenListenerNamePattern =
            (String)this.getConfiguredProperty(MESSAGE_DRIVEN_LISTENER_NAME_PATTERN);

        return MessageFormat.format(
                messageDrivenListenerNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3MessageDrivenFacade#getMessageDrivenName()
     */
    @Override
    protected String handleGetMessageDrivenName()
    {
        String messageDrivenNamePattern =
            (String)this.getConfiguredProperty(MESSAGE_DRIVEN_NAME_PATTERN);

        return MessageFormat.format(
                messageDrivenNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3MessageDrivenFacadeLogic#handleGetMessageDrivenTestName()
     */
    @Override
    protected String handleGetMessageDrivenTestName()
    {
        String messageDrivenTestNamePattern =
            (String)this.getConfiguredProperty(MESSAGE_DRIVEN_TEST_NAME_PATTERN);

        return MessageFormat.format(
                messageDrivenTestNamePattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3MessageDrivenFacade#getMessageSelector()
     */
    @Override
    protected String handleGetMessageSelector()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_MDB_SELECTOR);
    }

    /**
     * @see EJB3MessageDrivenFacade#getRunAs()
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
     * @see EJB3MessageDrivenFacade#getSubscriptionDurability()
     */
    @Override
    protected String handleGetSubscriptionDurability()
    {
        String subscriptionDurability = null;
        if (this.isDestinationTypeTopic())
        {
            subscriptionDurability = String.valueOf(
                    this.getConfiguredProperty(MESSAGE_DRIVEN_TOPIC_SUBSCRIPTION_DURABILITY));
            if (this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_MDB_DURABILITY) != null)
            {
                subscriptionDurability = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_MDB_DURABILITY);
            }
        }
        return subscriptionDurability;
    }

    /**
     * @see EJB3MessageDrivenFacadeLogic#handleIsSubscriptionDurable()
     */
    @Override
    protected boolean handleIsSubscriptionDurable()
    {
        return StringUtils.equalsIgnoreCase(this.getSubscriptionDurability(), EJB3Globals.MDB_SUBSCRIPTION_DURABLE) ?
                true : false;
    }

    /**
     * @see EJB3MessageDrivenFacadeLogic#handleIsSubscriptionNonDurable()
     */
    @Override
    protected boolean handleIsSubscriptionNonDurable()
    {
        return StringUtils.equalsIgnoreCase(this.getSubscriptionDurability(), EJB3Globals.MDB_SUBSCRIPTION_NONDURABLE) ?
                true : false;
    }

    /**
     * @see EJB3MessageDrivenFacadeLogic#handleGetDurableSubscriptionId()
     */
    @Override
    protected String handleGetDurableSubscriptionId()
    {
        String durableSubscriptionIdPattern =
            (String)this.getConfiguredProperty(MESSAGE_DRIVEN_DURABLE_SUBSCRIPTION_ID_PATTERN);

        return MessageFormat.format(
                durableSubscriptionIdPattern,
                StringUtils.trimToEmpty(this.getName()));
    }

    /**
     * @see EJB3MessageDrivenFacade#getTransactionManagement()
     */
    @Override
    protected String handleGetTransactionManagement()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_MANAGEMENT);
    }

    /**
     * @see EJB3MessageDrivenFacade#isTransactionManagementBean()
     */
    @Override
    protected boolean handleIsTransactionManagementBean()
    {
        return StringUtils.equalsIgnoreCase(getTransactionManagement(), EJB3Globals.TRANSACTION_MANAGEMENT_BEAN);
    }

    /**
     * @see EJB3MessageDrivenFacade#getTransactionType()
     */
    @Override
    protected String handleGetTransactionType()
    {
        return EJB3MetafacadeUtils.getTransactionType(this,
                String.valueOf(this.getConfiguredProperty(EJB3Globals.TRANSACTION_TYPE)));
    }

    /**
     * @see EJB3MessageDrivenFacade#getAttributesAsList(java.util.Collection, boolean, boolean)
     */
    @Override
    protected String handleGetAttributesAsList(
            java.util.Collection attributes,
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
            AttributeFacade attr = (AttributeFacade)it.next();
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
     * @see EJB3MessageDrivenFacade#getConstants(boolean)
     */
    protected java.util.Collection handleGetConstants(boolean follow)
    {
        return EJB3MetafacadeUtils.getConstants(this, follow);
    }

    /**
     * @see EJB3MessageDrivenFacadeLogic#getServiceReferences()
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
     * @see EJB3MessageDrivenFacadeLogic#handleIsDestinationTypeQueue()
     */
    @Override
    protected boolean handleIsDestinationTypeQueue()
    {
        boolean typeQueue = false;
        if (StringUtils.equalsIgnoreCase(this.getDestinationType(), EJB3Globals.MDB_DESTINATION_TYPE_QUEUE))
        {
            typeQueue = true;
        }
        return typeQueue;
    }

    /**
     * @see EJB3MessageDrivenFacadeLogic#handleIsDestinationTypeTopic()
     */
    @Override
    protected boolean handleIsDestinationTypeTopic()
    {
        boolean typeTopic = false;
        if (StringUtils.equalsIgnoreCase(this.getDestinationType(), EJB3Globals.MDB_DESTINATION_TYPE_TOPIC))
        {
            typeTopic = true;
        }
        return typeTopic;
    }

    /**
     * @see EJB3MessageDrivenFacadeLogic#handleGetEnvironmentEntries(boolean)
     */
    protected Collection handleGetEnvironmentEntries(boolean follow)
    {
        return EJB3MetafacadeUtils.getEnvironmentEntries(this, follow);
    }

    /**
     * @see EJB3MessageDrivenFacadeLogic#handleGetMinimumPoolSize()
     */
    protected int handleGetMinimumPoolSize()
    {
        int minPoolSize = 0;
        String minPoolSizeStr = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_MDB_MINIMUM_POOL_SIZE);
        if (StringUtils.isNotBlank(minPoolSizeStr) && NumberUtils.isDigits(minPoolSizeStr))
        {
            minPoolSize = Integer.parseInt(minPoolSizeStr);
        }
        return minPoolSize;
    }

    /**
     * @see EJB3MessageDrivenFacadeLogic#handleGetMaximumPoolSize()
     */
    protected int handleGetMaximumPoolSize()
    {
        int maxPoolSize = 0;
        String maxPoolSizeStr = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_MDB_MAXIMUM_POOL_SIZE);
        if (StringUtils.isNotBlank(maxPoolSizeStr) && NumberUtils.isDigits(maxPoolSizeStr))
        {
            maxPoolSize = Integer.parseInt(maxPoolSizeStr);
        }
        return maxPoolSize;
    }

    /**
     * @see EJB3MessageDrivenFacadeLogic#handleIsListenerEnabled()
     */
    @Override
    protected boolean handleIsListenerEnabled()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_LISTENER);
    }

    /**
     * @see EJB3MessageDrivenFacadeLogic#handleGetInterceptorReferences()
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
                    return (targetElement != null && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_INTERCEPTOR));
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
     * @see EJB3MessageDrivenFacadeLogic#handleIsExcludeDefaultInterceptors()
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
}
