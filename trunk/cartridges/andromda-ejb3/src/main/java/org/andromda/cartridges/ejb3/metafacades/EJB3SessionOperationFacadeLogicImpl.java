package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.Service;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacade
 */
public class EJB3SessionOperationFacadeLogicImpl
    extends EJB3SessionOperationFacadeLogic
{
    /**
     * The property used to specify the implementation operation name pattern 
     * on service beans.
     */
    public static final String SERVICE_IMPLEMENTATION_OPERATION_NAME_PATTERN = 
        "serviceImplementationOperationNamePattern";
    
    public EJB3SessionOperationFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacade#getViewType()
     */
    protected java.lang.String handleGetViewType()
    {
        String viewType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_VIEWTYPE);
        if (StringUtils.isEmpty(viewType))
        {
            EJB3SessionFacade sessionFacade = (EJB3SessionFacade)this.getOwner();
            if (StringUtils.isNotEmpty(sessionFacade.getViewType()))
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleIsViewTypeRemote()
     */
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleIsViewTypeLocal()
     */
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleIsViewTypeBoth()
     */
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleGetTransactionType()
     */
    protected String handleGetTransactionType()
    {
        String transType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        if (StringUtils.isNotBlank(transType))
        {
            transType = EJB3MetafacadeUtils.convertTransactionType(transType);
        }
        else
        {
            transType = StringUtils.trimToEmpty(
                    ObjectUtils.toString(this.getConfiguredProperty(EJB3Globals.TRANSACTION_TYPE)));
        }
        return transType;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleIsBusinessOperation()
     */
    protected boolean handleIsBusinessOperation()
    {
        return !this.hasStereotype(EJB3Profile.STEREOTYPE_CREATE_METHOD);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleGetRolesAllowed()
     */
    protected String handleGetRolesAllowed()
    {
        StringBuffer rolesAllowed = null;
        String separator = "";
        
        for (final Iterator iter = this.getNonRunAsRoles().iterator(); iter.hasNext(); )
        {
            if (rolesAllowed == null)
            {
                rolesAllowed = new StringBuffer();
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleIsPermitAll()
     */
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleIsDenyAll()
     */
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleGetFlushMode()
     */
    protected String handleGetFlushMode()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_FLUSH_MODE);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleGetThrowsClause()
     */
    protected String handleGetThrowsClause()
    {
        StringBuffer throwsClause = null;
        if (this.isExceptionsPresent())
        {
            throwsClause = new StringBuffer(this.getExceptionList());
        }
        if (throwsClause != null)
        {
            throwsClause.insert(0, "throws ");
        }
        return throwsClause != null ? throwsClause.toString() : null;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleGetThrowsClause(java.lang.String)
     */
    protected String handleGetThrowsClause(String initialExceptions)
    {
        final StringBuffer throwsClause = new StringBuffer(initialExceptions);
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleGetNonRunAsRoles()
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleIsTimeoutCallback()
     */
    protected boolean handleIsTimeoutCallback()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SERVICE_TIMER_TIMEOUT);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleGetImplementationName()
     */
    protected String handleGetImplementationName()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleGetImplementationCall()
     */
    protected String handleGetImplementationCall()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getCall()));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleGetImplementationSignature()
     */
    protected String handleGetImplementationSignature()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getSignature()));
    }

    /**
     * Retrieves the implementationOperatName by replacing the <code>replacement</code> in the {@link
     * SERVICE_IMPLEMENTATION_OPERATION_NAME_PATTERN}
     *
     * @param replacement the replacement string for the pattern.
     * @return the operation name
     */
    private String getImplementationOperationName(String replacement)
    {
        String implementationNamePattern = 
            (String)this.getConfiguredProperty(SERVICE_IMPLEMENTATION_OPERATION_NAME_PATTERN);

        return MessageFormat.format(
                implementationNamePattern,
                new Object[] {StringUtils.trimToEmpty(replacement)});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleGetInterceptorReferences()
     */
    protected Collection handleGetInterceptorReferences()
    {
        Collection references = this.getSourceDependencies();
        CollectionUtils.filter(references, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                DependencyFacade dependency = (DependencyFacade)object;
                ModelElementFacade targetElement = dependency.getTargetElement();
                return (targetElement != null && targetElement.hasStereotype(EJB3Profile.STEREOTYPE_INTERCEPTOR));
            }
        });
        CollectionUtils.transform(references, new Transformer()
        {
            public Object transform(final Object object)
            {
                return ((DependencyFacade)object).getTargetElement();
            }
        });
        return references;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#
     *      handleIsExcludeDefaultInterceptors()
     */
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleIsExcludeClassInterceptors()
     */
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
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleIsPostConstruct()
     */
    protected boolean handleIsPostConstruct()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_POST_CONSTRUCT);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleIsPreDestroy()
     */
    protected boolean handleIsPreDestroy()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_PRE_DESTROY);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleIsPostActivate()
     */
    protected boolean handleIsPostActivate()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_POST_ACTIVATE);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleIsPrePassivate()
     */
    protected boolean handleIsPrePassivate()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_PRE_PASSIVATE);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionOperationFacadeLogic#handleIsLifecycleCallback()
     */
    protected boolean handleIsLifecycleCallback()
    {
        return this.isPostConstruct() || this.isPreDestroy() || this.isPostActivate() || this.isPrePassivate();
    }
}