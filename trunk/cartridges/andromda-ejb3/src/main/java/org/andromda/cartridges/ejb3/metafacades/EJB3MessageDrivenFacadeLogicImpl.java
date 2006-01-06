package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade
 */
public class EJB3MessageDrivenFacadeLogicImpl
    extends EJB3MessageDrivenFacadeLogic
{
    /**
     * The property which stores the default destination type
     */
    public static final String MDB_DESTINATION_TYPE = "messageDrivenDestinationType";
    
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
    
    //  ---------------- constructor -------------------------------
    
    public EJB3MessageDrivenFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    //  ---------------- methods -------------------------------
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getAcknowledgeMode()
     */
    protected java.lang.String handleGetAcknowledgeMode()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_MDB_ACKNOWLEDGE_MODE);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getDestination()
     */
    protected java.lang.String handleGetDestination()
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getDestinationType()
     */
    protected java.lang.String handleGetDestinationType()
    {
        String destinationType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_MDB_DESTINATION_TYPE);
        if (StringUtils.isBlank(destinationType))
        {
            destinationType = (String)this.getConfiguredProperty(MDB_DESTINATION_TYPE);
        }
        return destinationType;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getFullyQualifiedMessageDrivenImplementationName()
     */
    protected java.lang.String handleGetFullyQualifiedMessageDrivenImplementationName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getMessageDrivenImplementationName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getFullyQualifiedMessageDrivenListenerName()
     */
    protected java.lang.String handleGetFullyQualifiedMessageDrivenListenerName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getMessageDrivenListenerName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getFullyQualifiedMessageDrivenName()
     */
    protected java.lang.String handleGetFullyQualifiedMessageDrivenName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getMessageDrivenName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getMessageDrivenImplementationName()
     */
    protected java.lang.String handleGetMessageDrivenImplementationName()
    {
        String messageDrivenImplNamePattern = 
            (String)this.getConfiguredProperty(MESSAGE_DRIVEN_IMPLEMENTATION_NAME_PATTERN);

        return MessageFormat.format(
                messageDrivenImplNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getMessageDrivenListenerName()
     */
    protected java.lang.String handleGetMessageDrivenListenerName()
    {
        String messageDrivenListenerNamePattern = 
            (String)this.getConfiguredProperty(MESSAGE_DRIVEN_LISTENER_NAME_PATTERN);

        return MessageFormat.format(
                messageDrivenListenerNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getMessageDrivenName()
     */
    protected java.lang.String handleGetMessageDrivenName()
    {
        String messageDrivenNamePattern = 
            (String)this.getConfiguredProperty(MESSAGE_DRIVEN_NAME_PATTERN);

        return MessageFormat.format(
                messageDrivenNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getMessageSelector()
     */
    protected java.lang.String handleGetMessageSelector()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_MDB_SELECTOR);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getRunAs()
     */
    protected java.lang.String handleGetRunAs()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SECURITY_RUN_AS);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getSubscriptionDurability()
     */
    protected java.lang.String handleGetSubscriptionDurability()
    {
        String durability = null;
        if (StringUtils.equalsIgnoreCase(getDestinationType(), EJB3Globals.MDB_DESTINATION_TYPE_TOPIC))
        {
            durability = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_MDB_DURABILITY);
        }
        return durability;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getTransactionManagement()
     */
    protected java.lang.String handleGetTransactionManagement()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_MANAGEMENT);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#isTransactionManagementBean()
     */
    protected boolean handleIsTransactionManagementBean()
    {
        return StringUtils.equalsIgnoreCase(getTransactionManagement(), EJB3Globals.TRANSACTION_MANAGEMENT_BEAN);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getTransactionType()
     */
    protected java.lang.String handleGetTransactionType()
    {
        return EJB3MetafacadeUtils.getTransactionType(this, 
                String.valueOf(this.getConfiguredProperty(EJB3Globals.TRANSACTION_TYPE)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#
     *      getAttributesAsList(java.util.Collection, boolean, boolean)
     */
    protected java.lang.String handleGetAttributesAsList(
            java.util.Collection attributes, 
            boolean includeTypes, 
            boolean includeNames)
    {
        if (!includeNames && !includeTypes || attributes == null)
        {
            return "";
        }

        StringBuffer sb = new StringBuffer();
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacade#getConstants(boolean)
     */
    protected java.util.Collection handleGetConstants(boolean follow)
    {
        return EJB3MetafacadeUtils.getConstants(this, follow);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacadeLogic#getServiceReferences()
     */
    public Collection getServiceReferences()
    {
        Collection references = super.getServiceReferences();
        CollectionUtils.filter(references, new Predicate()
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacadeLogic#handleIsDestinationTypeQueue()
     */
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3MessageDrivenFacadeLogic#handleIsDestinationTypeTopic()
     */
    protected boolean handleIsDestinationTypeTopic()
    {
        boolean typeTopic = false;
        if (StringUtils.equalsIgnoreCase(this.getDestinationType(), EJB3Globals.MDB_DESTINATION_TYPE_TOPIC))
        {
            typeTopic = true;
        }
        return typeTopic;
    }

}