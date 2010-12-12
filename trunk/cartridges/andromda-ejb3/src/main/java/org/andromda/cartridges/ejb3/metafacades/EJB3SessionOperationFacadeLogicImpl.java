package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Role;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacade.
 *
 * @see EJB3SessionOperationFacade
 */
public class EJB3SessionOperationFacadeLogicImpl
    extends EJB3SessionOperationFacadeLogic
{
    /**
     * The property which stores the pattern used to generate the service operation test name
     */
    private static final String SERVICE_OPERATION_TEST_NAME_PATTERN = "serviceOperationTestNamePattern";

    /**
     * @param metaObject
     * @param context
     */
    public EJB3SessionOperationFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    /**
     * @see EJB3SessionOperationFacade#getViewType()
     */
    @Override
    protected String handleGetViewType()
    {
        String viewType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_VIEWTYPE);
        if (StringUtils.isEmpty(viewType))
        {
            EJB3SessionFacade sessionFacade = (EJB3SessionFacade)this.getOwner();
            if (StringUtils.isNotBlank(sessionFacade.getViewType()))
            {
                viewType = sessionFacade.getViewType();
            }
            else
            {
                viewType = EJB3Globals.VIEW_TYPE_BOTH;
            }
        }
        return viewType;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsViewTypeRemote()
     */
    @Override
    protected boolean handleIsViewTypeRemote()
    {
        boolean isRemote = false;
        if (this.getViewType().equalsIgnoreCase(EJB3Globals.VIEW_TYPE_REMOTE))
        {
            isRemote = true;
        }
        return isRemote;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsViewTypeLocal()
     */
    @Override
    protected boolean handleIsViewTypeLocal()
    {
        boolean isLocal = false;
        if (this.getViewType().equalsIgnoreCase(EJB3Globals.VIEW_TYPE_LOCAL))
        {
            isLocal = true;
        }
        return isLocal;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsViewTypeBoth()
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
     * @see EJB3SessionOperationFacadeLogic#handleIsViewTypeStrictlyLocal()
     */
    @Override
    protected boolean handleIsViewTypeStrictlyLocal()
    {
        boolean isViewTypeStrictlyLocal = false;
        String viewType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_VIEWTYPE);
        if (StringUtils.equalsIgnoreCase(viewType, EJB3Globals.VIEW_TYPE_LOCAL) || this.isViewTypeStrictlyBoth())
        {
            isViewTypeStrictlyLocal = true;
        }
        return isViewTypeStrictlyLocal;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsViewTypeStrictlyRemote()
     */
    @Override
    protected boolean handleIsViewTypeStrictlyRemote()
    {
        boolean isViewTypeStrictlyRemote = false;
        String viewType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_VIEWTYPE);
        if (StringUtils.equalsIgnoreCase(viewType, EJB3Globals.VIEW_TYPE_REMOTE) || this.isViewTypeStrictlyBoth())
        {
            isViewTypeStrictlyRemote = true;
        }
        return isViewTypeStrictlyRemote;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsViewTypeStrictlyBoth()
     */
    @Override
    protected boolean handleIsViewTypeStrictlyBoth()
    {
        boolean isViewTypeStrictlyBoth = false;
        String viewType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_VIEWTYPE);
        if (StringUtils.equalsIgnoreCase(viewType, EJB3Globals.VIEW_TYPE_BOTH))
        {
            isViewTypeStrictlyBoth = true;
        }
        return isViewTypeStrictlyBoth;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsViewTypeAbsoluteLocal()
     */
    @Override
    protected boolean handleIsViewTypeAbsoluteLocal()
    {
        boolean isViewTypeAsbolutelyLocal = false;
        EJB3SessionFacade session = (EJB3SessionFacade)this.getOwner();
        if (!this.isLifecycleCallback() &&
            StringUtils.equalsIgnoreCase(this.getVisibility(), "public") &&
            ((session.isViewTypeBoth() &&
                ((session.isViewTypeStrictlyRemote() && this.isViewTypeStrictlyLocal()) ||
                ((session.isViewTypeStrictlyLocal() || session.isViewTypeStrictlyBoth()) &&
                        !this.isViewTypeStrictlyRemote()))) ||
            (session.isViewTypeStrictlyLocal() && !this.isViewTypeStrictlyRemote()) ||
            this.isViewTypeStrictlyBoth()))
        {
            isViewTypeAsbolutelyLocal = true;
        }
        return isViewTypeAsbolutelyLocal;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsViewTypeAbsoluteRemote()
     */
    @Override
    protected boolean handleIsViewTypeAbsoluteRemote()
    {
        boolean isViewTypeAsbolutelyRemote = false;
        EJB3SessionFacade session = (EJB3SessionFacade)this.getOwner();
        if (!this.isLifecycleCallback() &&
            StringUtils.equalsIgnoreCase(this.getVisibility(), "public") &&
            ((session.isViewTypeBoth() &&
                ((session.isViewTypeStrictlyLocal() && this.isViewTypeStrictlyRemote()) ||
                ((session.isViewTypeStrictlyRemote() || session.isViewTypeStrictlyBoth()) &&
                        !this.isViewTypeStrictlyLocal()))) ||
            (session.isViewTypeStrictlyRemote() && !this.isViewTypeStrictlyLocal()) ||
            this.isViewTypeStrictlyBoth()))
        {
            isViewTypeAsbolutelyRemote = true;
        }
        return isViewTypeAsbolutelyRemote;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsViewTypeAbsoluteBoth()
     */
    @Override
    protected boolean handleIsViewTypeAbsoluteBoth()
    {
        boolean isViewTypeAbsolutelyBoth = false;
        if (this.isViewTypeAbsoluteLocal() && this.isViewTypeAbsoluteRemote())
        {
            isViewTypeAbsolutelyBoth = true;
        }
        return isViewTypeAbsolutelyBoth;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#getSignature()
     *
     * Override the default implementation to check for timer service and
     * replace all attributes with javax.ejb.Timer attribute.
     */
    public String getSignature()
    {
        String signature = super.getSignature();
        if (this.isTimeoutCallback())
        {
            final StringBuilder timeoutSignature = new StringBuilder(this.getName());
            timeoutSignature.append("(");
            timeoutSignature.append("javax.ejb.Timer timer");
            timeoutSignature.append(")");
            signature = timeoutSignature.toString();
        }
        return signature;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetTestSignature()
     */
    @Override
    protected String handleGetTestSignature()
    {
        return this.getTestName() + "()";
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetTestName()
     */
    @Override
    protected String handleGetTestName()
    {
        String serviceOperationTestNamePattern =
            (String)this.getConfiguredProperty(SERVICE_OPERATION_TEST_NAME_PATTERN);

        return MessageFormat.format(
                serviceOperationTestNamePattern,
                StringUtils.trimToEmpty(StringUtils.capitalize(this.getName())));
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#getCall()
     *
     * Override the default implementation to check for timer service and
     * add the javax.ejb.Timer attribute to the call.
     */
    public String getCall()
    {
        String call = super.getCall();
        if (this.isTimeoutCallback())
        {
            final StringBuilder buffer = new StringBuilder(this.getName());
            buffer.append("(");
            buffer.append("timer");
            buffer.append(")");
            call =  buffer.toString();
        }
        return call;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetTransactionType()
     *
     * If no method level transaction type specified, take it from the class level if it exists there.
     */
    @Override
    protected String handleGetTransactionType()
    {
        String transType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        if (StringUtils.isNotBlank(transType))
        {
            transType = EJB3MetafacadeUtils.convertTransactionType(transType);
        }
        else
        {
            EJB3SessionFacade session = (EJB3SessionFacade)this.getOwner();
            transType = session.getTransactionType();
        }
        return transType;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsBusinessOperation()
     */
    @Override
    protected boolean handleIsBusinessOperation()
    {
        return !this.hasStereotype(EJB3Profile.STEREOTYPE_CREATE_METHOD);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetRolesAllowed()
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
     * @see EJB3SessionOperationFacadeLogic#handleIsPermitAll()
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
     * @see EJB3SessionOperationFacadeLogic#handleIsDenyAll()
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
     * @see EJB3SessionOperationFacadeLogic#handleGetFlushMode()
     */
    @Override
    protected String handleGetFlushMode()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_FLUSH_MODE);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetThrowsClause()
     */
    @Override
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
     * @see EJB3SessionOperationFacadeLogic#handleGetThrowsClause(String)
     */
    @Override
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
     * @see EJB3SessionOperationFacadeLogic#handleGetNonRunAsRoles()
     */
    protected Collection handleGetNonRunAsRoles()
    {
        final Collection roles = new LinkedHashSet();
        if (this.getOwner() instanceof EJB3SessionFacade)
        {
            roles.addAll(((EJB3SessionFacade)this.getOwner()).getNonRunAsRoles());
        }
        Collection operationRoles = this.getTargetDependencies();
        CollectionUtils.filter(
            operationRoles,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    DependencyFacade dependency = (DependencyFacade)object;
                    return dependency != null
                        && dependency.getSourceElement() != null
                        && Role.class.isAssignableFrom(dependency.getSourceElement().getClass())
                        && !dependency.hasStereotype(EJB3Profile.STEREOTYPE_SECURITY_RUNAS);
                }
            });
        CollectionUtils.transform(
            operationRoles,
            new Transformer()
            {
                public Object transform(Object object)
                {
                    return ((DependencyFacade)object).getSourceElement();
                }
            });
        roles.addAll(operationRoles);
        final Collection allRoles = new LinkedHashSet(roles);

        // add all roles which are specializations of this one
        CollectionUtils.forAllDo(
            roles,
            new Closure()
            {
                public void execute(Object object)
                {
                    if (object instanceof Role)
                    {
                        allRoles.addAll(((Role)object).getAllSpecializations());
                    }
                }
            });
        return allRoles;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsTimeoutCallback()
     */
    @Override
    protected boolean handleIsTimeoutCallback()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SERVICE_TIMER_TIMEOUT);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetImplementationName()
     */
    @Override
    protected String handleGetImplementationName()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getName()));
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetImplementationCall()
     */
    @Override
    protected String handleGetImplementationCall()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getCall()));
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetImplementationSignature()
     */
    @Override
    protected String handleGetImplementationSignature()
    {
        return (this.getVisibility().equalsIgnoreCase("public") ?
                this.getImplementationOperationName(StringUtils.capitalize(this.getSignature())) :
                    this.getSignature());
    }

    /**
     * Retrieves the implementationOperatName by replacing the <code>replacement</code> in the {@link
     * EJB3Globals#IMPLEMENTATION_OPERATION_NAME_PATTERN}
     *
     * @param replacement the replacement string for the pattern.
     * @return the operation name
     */
    private String getImplementationOperationName(String replacement)
    {
        String implementationNamePattern =
            (String)this.getConfiguredProperty(EJB3Globals.IMPLEMENTATION_OPERATION_NAME_PATTERN);

        return MessageFormat.format(
                implementationNamePattern,
                StringUtils.trimToEmpty(replacement));
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetInterceptorReferences()
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
     * @see EJB3SessionOperationFacadeLogic#handleIsExcludeDefaultInterceptors()
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
     * @see EJB3SessionOperationFacadeLogic#handleIsExcludeClassInterceptors()
     */
    @Override
    protected boolean handleIsExcludeClassInterceptors()
    {
        boolean excludeClass = false;
        String excludeClassStr =
            (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SERVICE_INTERCEPTOR_EXCLUDE_CLASS);
        if (excludeClassStr != null)
        {
            excludeClass = BooleanUtils.toBoolean(excludeClassStr);
        }
        return excludeClass;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsPostConstruct()
     */
    @Override
    protected boolean handleIsPostConstruct()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_POST_CONSTRUCT);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsPreDestroy()
     */
    @Override
    protected boolean handleIsPreDestroy()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_PRE_DESTROY);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsPostActivate()
     */
    @Override
    protected boolean handleIsPostActivate()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_POST_ACTIVATE);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsPrePassivate()
     */
    @Override
    protected boolean handleIsPrePassivate()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_PRE_PASSIVATE);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsLifecycleCallback()
     */
    @Override
    protected boolean handleIsLifecycleCallback()
    {
        return this.isPostConstruct() || this.isPreDestroy() || this.isPostActivate() || this.isPrePassivate();
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamValidationValidator()
     */
    @Override
    protected boolean handleIsSeamValidationValidator()
    {
        boolean isSeamValidorMethod = false;
        if (this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_VALIDATION_VALIDATOR))
        {
            isSeamValidorMethod = true;
        }
        return isSeamValidorMethod;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetSeamValidationOutcome()
     */
    @Override
    protected String handleGetSeamValidationOutcome()
    {
        String validationOutcome = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_VALIDATION_OUTCOME);
        if (StringUtils.isNotBlank(validationOutcome) &&
                !StringUtils.equals(validationOutcome, "org.jboss.seam.annotations.Outcome.REDISPLAY"))
        {
            validationOutcome = "\"" + validationOutcome + "\"";
        }
        return validationOutcome;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamValidationRefreshEntities()
     */
    @Override
    protected boolean handleIsSeamValidationRefreshEntities()
    {
        return BooleanUtils.toBoolean(
                (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_VALIDATION_REFRESH_ENTITIES));
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamLifecycleCreate()
     */
    @Override
    protected boolean handleIsSeamLifecycleCreate()
    {
        boolean isSeamLifecycleCreate = false;
        if (this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_LIFECYCLE_CREATE))
        {
            isSeamLifecycleCreate = true;
        }
        return isSeamLifecycleCreate;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamLifecycleDestroy()
     */
    @Override
    protected boolean handleIsSeamLifecycleDestroy()
    {
        boolean isSeamLifecycleCreate = false;
        if (this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_LIFECYCLE_DESTROY))
        {
            isSeamLifecycleCreate = true;
        }
        return isSeamLifecycleCreate;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamObserver()
     */
    @Override
    protected boolean handleIsSeamObserver()
    {
        boolean isSeamObserver = false;
        if (this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_LIFECYCLE_OBSERVER_EVENT) != null)
        {
            isSeamObserver = true;
        }
        return isSeamObserver;
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetSeamObserverEventName()
     */
    @Override
    protected String handleGetSeamObserverEventName()
    {
        return "(\"" +  this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_LIFECYCLE_OBSERVER_EVENT) + "\")";
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamAsynchronous()
     */
    @Override
    protected boolean handleIsSeamAsynchronous()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_ASYNCHRONOUS);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamBijectionFactory()
     */
    @Override
    protected boolean handleIsSeamBijectionFactory()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_BIJECTION_FACTORY);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetSeamBijectionFactoryParameters()
     */
    @Override
    protected String handleGetSeamBijectionFactoryParameters()
    {
        ArrayList parameters = new ArrayList();
        String value = (String) this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_BIJECTION_FACTORY_VALUE);
        if(StringUtils.isNotBlank(value))
        {
            parameters.add("value = \"" + value + "\"");
        }

        String scope = (String) this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_BIJECTION_FACTORY_SCOPE_TYPE);
        if(StringUtils.isNotBlank(scope))
        {
            parameters.add("scope = org.jboss.seam.ScopeType." + scope);
        }

        return EJB3MetafacadeUtils.buildAnnotationParameters(parameters);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamConversationBegin()
     */
    @Override
    protected boolean handleIsSeamConversationBegin()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_CONVERSATION_BEGIN);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetSeamConversationBeginParameters()
     */
    @Override
    protected String handleGetSeamConversationBeginParameters()
    {
        if(!this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_CONVERSATION_BEGIN))
        {
            return null;
        }
        else
        {
            ArrayList parameters = new ArrayList();
            String flushMode = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_FLUSH_MODE);
            if(StringUtils.isNotBlank(flushMode))
            {
                parameters.add("flushMode = org.jboss.seam.annotations.FlushModeType." + flushMode);
            }

            String pageflow = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_PAGEFLOW);
            if(StringUtils.isNotBlank(pageflow))
            {
                parameters.add("pageflow = \"" + pageflow + "\"");
            }

            String join = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_JOIN);
            if(StringUtils.isNotBlank(join))
            {
                parameters.add("join = " + join.toLowerCase());
            }

            String nested = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_NESTED);
            if(StringUtils.isNotBlank(nested))
            {
                parameters.add("nested = " + nested.toLowerCase());
            }

            Collection ifOutcome = this.findTaggedValues(EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_IF_OUTCOME);
            if(ifOutcome != null && !ifOutcome.isEmpty())
            {
                parameters.add(EJB3MetafacadeUtils.buildAnnotationMultivalueParameter("ifOutcome", ifOutcome));
            }
            return EJB3MetafacadeUtils.buildAnnotationParameters(parameters);
        }
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamConversationBeginTask()
     */
    @Override
    protected boolean handleIsSeamConversationBeginTask()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_CONVERSATION_BEGIN_TASK);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetSeamConversationBeginTaskParameters()
     */
    @Override
    protected String handleGetSeamConversationBeginTaskParameters()
    {
        if(!this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_CONVERSATION_BEGIN_TASK))
        {
            return null;
        }
        else
        {
            ArrayList parameters = new ArrayList();
            String flushMode = (String)this.findTaggedValue(
                    EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_TASK_FLUSH_MODE);
            if(StringUtils.isNotBlank(flushMode))
            {
                parameters.add("flushMode = org.jboss.seam.annotations.FlushModeType." + flushMode + "\"");
            }

            String taskIdParameter = (String)this.findTaggedValue(
                    EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_BEGIN_TASK_ID_PARAMETER);
            if(StringUtils.isNotBlank(taskIdParameter))
            {
                parameters.add("taskIdParameter = \"" + taskIdParameter + "\"");
            }
            return EJB3MetafacadeUtils.buildAnnotationParameters(parameters);
        }
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamConversationCreateProcess()
     */
    @Override
    protected boolean handleIsSeamConversationCreateProcess()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_CONVERSATION_CREATE_PROCESS);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetSeamConversationCreateProcessParameters()
     */
    @Override
    protected String handleGetSeamConversationCreateProcessParameters()
    {
        return "(definition = \"" +
            this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_CREATE_PROCESS_DEFINITION) + "\")";
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamConversationEnd()
     */
    @Override
    protected boolean handleIsSeamConversationEnd()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_CONVERSATION_END);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetSeamConversationEndParameters()
     */
    @Override
    protected String handleGetSeamConversationEndParameters()
    {
        if(!this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_CONVERSATION_END))
        {
            return null;
        }
        else
        {
            ArrayList parameters = new ArrayList();
            String beforeRedirect = (String)this.findTaggedValue(
                    EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_END_TASK_BEFORE_REDIRECT);
            if(StringUtils.isNotBlank(beforeRedirect))
            {
                parameters.add("beforeRedirect = " + beforeRedirect.toLowerCase());
            }

            Collection ifOutcome = this.findTaggedValues(EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_END_IF_OUTCOME);
            if(ifOutcome != null && !ifOutcome.isEmpty())
            {
                parameters.add(EJB3MetafacadeUtils.buildAnnotationMultivalueParameter("ifOutcome", ifOutcome));
            }

            Collection exceptions = this.findTaggedValues(
                    EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_END_EVEN_IF_EXCEPTION);
            if(exceptions != null && !exceptions.isEmpty())
            {
                parameters.add(
                        EJB3MetafacadeUtils.buildAnnotationMultivalueParameter(
                                "ifOutcome",
                                ifOutcome,
                                false,
                                ".class"));
            }
            return EJB3MetafacadeUtils.buildAnnotationParameters(parameters);
        }
    }
    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamConversationEndTask()
     */
    @Override
    protected boolean handleIsSeamConversationEndTask()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_CONVERSATION_END_TASK);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetSeamConversationEndTaskParameters()
     */
    @Override
    protected String handleGetSeamConversationEndTaskParameters()
    {
        if(!this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_CONVERSATION_END_TASK))
        {
            return null;
        }
        else
        {
            ArrayList parameters = new ArrayList();
            String transition = (String)this.findTaggedValue(
                    EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_END_TASK_TRANSITION_NAME);
            if(StringUtils.isNotBlank(transition))
            {
                parameters.add("transition = \"" + transition + "\"");
            }

            String beforeRedirect = (String)this.findTaggedValue(
                    EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_END_TASK_BEFORE_REDIRECT);
            if(StringUtils.isNotBlank(beforeRedirect))
            {
                parameters.add("beforeRedirect = " + beforeRedirect.toLowerCase());
            }
            Collection ifOutcome = this.findTaggedValues(EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_END_TASK_IF_OUTCOME);
            if(ifOutcome != null && !ifOutcome.isEmpty())
            {
                parameters.add(EJB3MetafacadeUtils.buildAnnotationMultivalueParameter("ifOutcome", ifOutcome));
            }
            return EJB3MetafacadeUtils.buildAnnotationParameters(parameters);
        }
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamConversationResumeProcess()
     */
    @Override
    protected boolean handleIsSeamConversationResumeProcess()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_CONVERSATION_RESUME_PROCESS);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetSeamConversationResumeProcessParameters()
     */
    @Override
    protected String handleGetSeamConversationResumeProcessParameters()
    {
        return "(processIdParameter = \"" +
            this.findTaggedValue(
                    EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_RESUME_PROCESS_PROCESS_ID_PARAMETER) + "\")";
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamConversationStartTask()
     */
    @Override
    protected boolean handleIsSeamConversationStartTask()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_CONVERSATION_START_TASK);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetSeamConversationStartTaskParameters()
     */
    @Override
    protected String handleGetSeamConversationStartTaskParameters()
    {
        if (!this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_CONVERSATION_START_TASK))
        {
            return null;
        }
        else
        {
            ArrayList parameters = new ArrayList();
            String flushMode = (String)this.findTaggedValue(
                    EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_START_TASK_FLUSH_MODE);
            if (StringUtils.isNotBlank(flushMode))
            {
                parameters.add("flushMode = org.jboss.seam.annotations.FlushModeType." + flushMode);
            }

            String taskIdParameter = (String)this.findTaggedValue(
                    EJB3Profile.TAGGEDVALUE_SEAM_CONVERSATION_START_TASK_ID_PARAMETER);
            if (StringUtils.isNotBlank(taskIdParameter))
            {
                parameters.add("taskIdParameter = \"" + taskIdParameter + "\"");
            }

            return EJB3MetafacadeUtils.buildAnnotationParameters(parameters);
        }
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamTransactional()
     */
    @Override
    protected boolean handleIsSeamTransactional()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_TRANSACTION_TRANSACTIONAL);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamTransactionRollback()
     */
    @Override
    protected boolean handleIsSeamTransactionRollback()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_TRANSACTION_ROLLBACK);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetSeamTransactionRollbackParameters()
     */
    @Override
    protected String handleGetSeamTransactionRollbackParameters()
    {
        Collection outcomes =  this.findTaggedValues(EJB3Profile.TAGGEDVALUE_SEAM_TRANSACTION_ROLLBACK_IF_OUTCOME);
        if (outcomes == null || outcomes.isEmpty())
        {
            return null;
        }
        else
        {
            return "(" + EJB3MetafacadeUtils.buildAnnotationMultivalueParameter("ifOutcome", outcomes) + ")";
        }
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleIsSeamWebRemote()
     */
    @Override
    protected boolean handleIsSeamWebRemote()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_WEBREMOTE);
    }

    /**
     * @see EJB3SessionOperationFacadeLogic#handleGetSeamWebRemoteParameters()
     */
    @Override
    protected String handleGetSeamWebRemoteParameters()
    {
        Collection excludes = this.findTaggedValues(EJB3Profile.TAGGEDVALUE_SEAM_WEBREMOTE_EXCLUDE);
        if(excludes == null || excludes.isEmpty())
        {
            return null;
        }
        else
        {
            return "(" + EJB3MetafacadeUtils.buildAnnotationMultivalueParameter("exclude", excludes) + ")";
        }
    }
}
