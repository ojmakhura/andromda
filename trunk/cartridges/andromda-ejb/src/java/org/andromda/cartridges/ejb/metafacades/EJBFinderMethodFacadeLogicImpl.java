package org.andromda.cartridges.ejb.metafacades;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Iterator;

/**
 * <p>
 * Represents an EJB finder method. </p> Metaclass facade implementation.
 */
public class EJBFinderMethodFacadeLogicImpl
        extends EJBFinderMethodFacadeLogic
{
    // ---------------- constructor -------------------------------

    public EJBFinderMethodFacadeLogicImpl(java.lang.Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected java.lang.String handleGetQuery()
    {

        // first see if there is a query stored as a constraint
        String queryString = super.getQuery("query.EJB-QL");

        // otherwise see if there is a query stored as a tagged value
        if (StringUtils.isEmpty(queryString))
        {
            Object value = this.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_QUERY);
            queryString = (String)value;
            if (queryString != null)
            {
                // remove any excess whitespace
                queryString = queryString.replaceAll("[$\\s]+", " ");
            }
        }

        // if there wasn't any stored query, create one by default.
        if (StringUtils.isEmpty(queryString))
        {
            String variableName = StringUtils.uncapitalize(this.getOwner().getName());
            queryString = "SELECT DISTINCT OBJECT(" + variableName + ") FROM " + this.getOwner().getName() + " as " +
                    variableName;
            if (!this.getArguments().isEmpty())
            {
                queryString = queryString + " WHERE";
                Collection parameters = this.getArguments();
                if (parameters != null && !parameters.isEmpty())
                {
                    Iterator parameterIt = parameters.iterator();
                    for (int ctr = 1; parameterIt.hasNext(); ctr++)
                    {
                        Object test = parameterIt.next();
                        ParameterFacade param = (ParameterFacade)test;
                        queryString = queryString + ' ' + variableName + '.' + param.getName() + " = ?" + ctr;
                        if (parameterIt.hasNext())
                        {
                            queryString = queryString + " AND";
                        }
                    }
                }
            }
        }
        return queryString;
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.EJBFinderMethodFacade#getTransactionType()
     */
    protected java.lang.String handleGetTransactionType()
    {
        return (String)this.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE, true);
    }

}