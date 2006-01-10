package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
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
        String rolesAllowedStr = null;
        final String tmpRoles = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_SECURITY_ROLES_ALLOWED);
        if (StringUtils.isNotBlank(tmpRoles))
        {
            StringBuffer rolesAllowed = new StringBuffer();
            final String[] roles = StringUtils.split(tmpRoles, ',');
            for (int i = 0; i < roles.length; i++)
            {
                if (i > 0)
                {
                    rolesAllowed.append(", ");
                }
                rolesAllowed.append('"');
                rolesAllowed.append(roles[i]);
                rolesAllowed.append('"');
            }
            rolesAllowedStr = rolesAllowed.toString();
        }
        return rolesAllowedStr;
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

}