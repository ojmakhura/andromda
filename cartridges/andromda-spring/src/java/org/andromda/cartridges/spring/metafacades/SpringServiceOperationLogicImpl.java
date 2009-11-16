package org.andromda.cartridges.spring.metafacades;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringServiceOperation.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation
 */
public class SpringServiceOperationLogicImpl
        extends SpringServiceOperationLogic
{

    /**
     * Public constructor for SpringServiceOperationLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation
     */
    public SpringServiceOperationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION)
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#isWebserviceExposed()
     */
    protected boolean handleIsWebserviceExposed()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION);
    }

    /**
     * @return getImplementationOperationName(StringUtils.capitalize(this.getName()))
     * or getImplementationOperationName(StringUtils.capitalize(this.getSignature())
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getImplementationName()
     */
    protected String handleGetImplementationName()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getName()));
    }

    /**
     * @return getOutgoingMessageImplementationSignature() or getOutgoingMessageImplementationSignature()
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getImplementationSignature()
     */
    protected String handleGetImplementationSignature()
    {
        String signature = null;
        if (this.isIncomingMessageOperation())
        {
            signature = this.getIncomingMessageImplementationSignature();
        }
        else if (this.isOutgoingMessageOperation())
        {
            signature = this.getOutgoingMessageImplementationSignature();
        }
        else
        {
            signature = this.getImplementationOperationName(StringUtils.capitalize(this.getSignature()));
        }
        return signature;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getCall()
     *
     * Overridden to provide the message argument (when necessary)
     */
    public String getCall()
    {
        String call = null;
        if (this.isIncomingMessageOperation() && this.getArguments().isEmpty())
        {
            call = this.getName() + "(message)";
        }
        else
        {
            call = super.getCall();
        }
        return call;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getSignature(String)
     *
     * Overridden to provide the appropriate incoming message (if needed).
     */
    public String getSignature(String modifier)
    {
        String signature = null;
        if (this.isIncomingMessageOperation() && this.getArguments().isEmpty())
        {
            signature = this.getIncomingMessageSignature(modifier);
        }
        else
        {
            signature = super.getSignature(modifier);
        }
        return signature;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperationLogic#getSignature(boolean)
     *
     * Overridden to provide the appropriate incoming message (if needed).
     */
    public String getSignature(final boolean withArgumentNames)
    {
        String signature = null;
        if (this.isIncomingMessageOperation() && this.getArguments().isEmpty())
        {
            signature = this.getIncomingMessageSignature(null);
        }
        else
        {
            signature = super.getSignature(withArgumentNames);
        }
        return signature;
    }

    /**
     *
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperationLogic#getSignature()
     *
     * Overridden to provide the appropriate incoming message (if needed).
     */
    public String getSignature()
    {
        return this.getSignature(true);
    }

    /**
     * @return getImplementationOperationName(StringUtils.capitalize(this.getCall()))
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperationLogic#getImplementationCall()
     */
    protected String handleGetImplementationCall()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getCall()));
    }

    /**
     * Retrieves the implementationOperatName by replacing the <code>replacement</code> in the {@link
     * SpringGlobals#IMPLEMENTATION_OPERATION_NAME_PATTERN}
     *
     * @param replacement the replacement string for the pattern.
     * @return the operation name
     */
    private String getImplementationOperationName(String replacement)
    {
        return StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(
                SpringGlobals.IMPLEMENTATION_OPERATION_NAME_PATTERN))).replaceAll("\\{0\\}", replacement);
    }

    /**
     * The transaction type for Spring service operations.
     */
    private static final String SERVICE_OPERATION_TRANSACTION_TYPE = "serviceOperationTransactionType";

    /**
     * @return getOwner().findTaggedValue(SpringProfile.TAGGEDVALUE_TRANSACTION_TYPE)
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperationLogic#getTransactionType()
     */
    public String handleGetTransactionType()
    {
        String transactionType = (String)this.findTaggedValue(SpringProfile.TAGGEDVALUE_TRANSACTION_TYPE);
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = (String)this.getOwner().findTaggedValue(SpringProfile.TAGGEDVALUE_TRANSACTION_TYPE);
        }
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = String.valueOf(this.getConfiguredProperty(SERVICE_OPERATION_TRANSACTION_TYPE));
        }
        return transactionType;
    }

    /**
     * The transaction type for EJB wrapped service operations..
     */
    private static final String EJB_SERVICE_OPERATION_TRANSACTION_TYPE = "ejbServiceOperationTransactionType";

    /**
     * @return EjbTransactionType
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperationLogic#getEjbTransactionType()
     */
    protected String handleGetEjbTransactionType()
    {
        String transactionType = (String)this.findTaggedValue(SpringProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = (String)this.getOwner().findTaggedValue(SpringProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        }
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = String.valueOf(this.getConfiguredProperty(EJB_SERVICE_OPERATION_TRANSACTION_TYPE));
        }
        return transactionType;
    }

    /**
     * @return ThrowsClause
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getThrowsClause()
     */
    protected String handleGetThrowsClause()
    {
        StringBuilder throwsClause = null;
        if (this.isExceptionsPresent())
        {
            throwsClause = new StringBuilder(this.getExceptionList());
        }
        if (throwsClause != null)
        {
            throwsClause.insert(0, "throws ");
        }
        return throwsClause != null ? throwsClause.toString() : null;
    }

    /**
     * @param initialExceptions 
     * @return ThrowsClause
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getThrowsClause(String)
     */
    protected String handleGetThrowsClause(String initialExceptions)
    {
        final StringBuilder throwsClause = new StringBuilder(initialExceptions);
        if (this.getThrowsClause() != null)
        {
            throwsClause.insert(0, ", ");
            throwsClause.insert(0, this.getThrowsClause());
        }
        else
        {
            throwsClause.insert(0, "throws ");
        }
        return throwsClause.toString();
    }

    /**
     * @return getMessageImplementationCall("session")
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getOutgoingMessageImplementationCall()
     */
    protected String handleGetOutgoingMessageImplementationCall()
    {
        return this.getMessageImplementationCall("session");
    }

    private String getMessageImplementationCall(String firstArgument)
    {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(StringUtils.capitalize(this.getName()));
        buffer.append("(");
        final boolean outgoingMessageOperation = this.isOutgoingMessageOperation();
        if (outgoingMessageOperation || (this.isIncomingMessageOperation() && this.getArguments().isEmpty()))
        {
            buffer.append(firstArgument);
        }
        final String argumentNames = this.getArgumentNames();
        if (outgoingMessageOperation && StringUtils.isNotBlank(argumentNames))
        {
            buffer.append(", ");
        }
        if (StringUtils.isNotBlank(argumentNames))
        {
            buffer.append(argumentNames);
        }
        buffer.append(")");
        return this.getImplementationOperationName(buffer.toString());
    }

    /**
     * @return getMessagingImplementationSignature("javax.jms.Session session")
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getOutgoingMessageImplementationSignature()
     */
    protected String handleGetOutgoingMessageImplementationSignature()
    {
        return this.getMessagingImplementationSignature("javax.jms.Session session");
    }

    private String getMessagingImplementationSignature(final String firstArgument)
    {
        return this.getMessagingOperationSignature(this.getImplementationName(), firstArgument, null);
    }

    /**
     * Gets the signature for an incoming message operation.
     *
     * @return the signature
     */
    private String getIncomingMessageSignature(String modifier)
    {
        return this.getMessagingOperationSignature(this.getName(), "javax.jms.Message message", modifier);
    }

    /**
     * Constructs the incoming or outgoing messaging operation signature given the <code>operationName</code>
     * and the <code>firstArgument</code>.
     *
     * @param operationName the name of the operation.
     * @param firstArgument the argument that will be the first argument in the operation signature.
     * @param modifier the modifier to add to each argument (if null or empty, it isn't added).
     * @return the signature of the operation.
     */
    private String getMessagingOperationSignature(final String operationName, final String firstArgument, final String modifier)
    {
        final StringBuilder signature = new StringBuilder(operationName);
        signature.append("(");
        if (StringUtils.isNotBlank(modifier))
        {
            signature.append(modifier).append(" ");
        }
        final Collection<ParameterFacade> arguments = this.getArguments();
        final boolean outgoingMessageOperation = this.isOutgoingMessageOperation();
        if (outgoingMessageOperation || (this.isIncomingMessageOperation() && arguments.isEmpty()))
        {
            signature.append(firstArgument);
        }
        final String argumentList = MetafacadeUtils.getTypedArgumentList(
            this.getArguments(),
            true,
            modifier);
        if (outgoingMessageOperation && StringUtils.isNotBlank(argumentList))
        {
            signature.append(", ");
        }
        if (StringUtils.isNotBlank(argumentList))
        {
            signature.append(argumentList);
        }
        signature.append(")");
        return signature.toString();
    }

    /**
     * @return getMessageImplementationCall("message")
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getIncomingMessageImplementationCall()
     */
    protected String handleGetIncomingMessageImplementationCall()
    {
        return this.getMessageImplementationCall("message");
    }

    /**
     * @return getMessagingImplementationSignature("javax.jms.Message message")
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getIncomingMessageImplementationSignature()
     */
    protected String handleGetIncomingMessageImplementationSignature()
    {
        return this.getMessagingImplementationSignature("javax.jms.Message message");
    }

    /**
     * @return "javax.jms.Message" or getGetterSetterReturnTypeName()
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getImplementationReturnTypeName()
     */
    protected String handleGetImplementationReturnTypeName()
    {
        String returnTypeName = null;
        if (this.isOutgoingMessageOperation())
        {
            returnTypeName = "javax.jms.Message";
        }
        else
        {
            returnTypeName = getGetterSetterReturnTypeName();
        }
        return returnTypeName;
    }

    /**
     * @return FullyQualifiedMessageListenerName
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getFullyQualifiedMessageListenerName()
     */
    protected String handleGetFullyQualifiedMessageListenerName()
    {
        StringBuilder name = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            name.append(packageName).append('.');
        }
        name.append(this.getMessageListenerName());
        return name.toString();
    }

    /**
     * @return MessageListenerName
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getMessageListenerName()
     */
    protected String handleGetMessageListenerName()
    {
        return this.getOwner().getName() +
            StringUtilsHelper.upperCamelCaseName(this.getName());
    }

    /**
     * @return StringUtils.uncapitalize(this.getMessageListenerName())
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getMessageListenerBeanName()
     */
    protected String handleGetMessageListenerBeanName()
    {
        return StringUtils.uncapitalize(this.getMessageListenerName());
    }

    /**
     * @return getName() + MESSAGE_LISTENER_CONTAINER_SUFFIX
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getMessageListenerContainerReferenceName()
     */
    protected String handleGetMessageListenerContainerReferenceName()
    {
        return this.getName() + MESSAGE_LISTENER_CONTAINER_SUFFIX;
    }

    /**
     * The suffix for the listener container. "ListenerContainer"
     */
    private static final String MESSAGE_LISTENER_CONTAINER_SUFFIX = "ListenerContainer";

    /**
     * @return getMessageListenerBeanName() + MESSAGE_LISTENER_CONTAINER_SUFFIX
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getMessageListenerContainerBeanName()
     */
    protected String handleGetMessageListenerContainerBeanName()
    {
        return this.getMessageListenerBeanName() + MESSAGE_LISTENER_CONTAINER_SUFFIX;
    }

    /**
     * @return findTaggedValue(SpringProfile.TAGGEDVALUEVALUE_MESSAGING_SESSION_ACKNOWLEDGE_MODE)
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getSessionAcknowledgeMode()
     */
    protected String handleGetSessionAcknowledgeMode()
    {
        // use the attribute name by default
        String mode = null;

        // if there is a tagged value, use it instead
        Object value = findTaggedValue(SpringProfile.TAGGEDVALUEVALUE_MESSAGING_SESSION_ACKNOWLEDGE_MODE);
        if (value != null)
        {
            mode = ObjectUtils.toString(value);
        }

        return mode;
    }

    /**
     * @return findTaggedValue(SpringProfile.TAGGEDVALUEVALUE_ACTIVEMQ_OPTIMIZE_ACKNOWLEDGE)
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#isOptimizeAcknowledge()
     */
    protected boolean handleIsOptimizeAcknowledge()
    {
        return BooleanUtils.toBoolean(ObjectUtils.toString(this.findTaggedValue(SpringProfile.TAGGEDVALUEVALUE_ACTIVEMQ_OPTIMIZE_ACKNOWLEDGE)));
    }

    /**
     * @return IsNullMessageConverterRequired
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#isNullMessageConverterRequired()
     */
    protected boolean handleIsNullMessageConverterRequired()
    {
        boolean result = false;
        
        Collection<ParameterFacade> arguments = getArguments();
        if (arguments != null && arguments.size() == 1)
        {
            ParameterFacade parameter = (ParameterFacade)arguments.iterator().next();
            String parameterType = parameter.getType().getFullyQualifiedName();
            
            Set jmsMessageTypes = new HashSet();
            Collections.addAll(jmsMessageTypes, SpringGlobals.jmsMessageTypes);
            
            result = jmsMessageTypes.contains(parameterType);
        }
        
        return result;
    }
}