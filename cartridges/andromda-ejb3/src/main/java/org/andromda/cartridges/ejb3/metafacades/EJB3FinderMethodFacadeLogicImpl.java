package org.andromda.cartridges.ejb3.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.lang.StringUtils;


/**
 * <p/>
 * Represents an EJB finder method. </p>
 * 
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodFacade
 */
public class EJB3FinderMethodFacadeLogicImpl
    extends EJB3FinderMethodFacadeLogic
{
    
    /**
     * Stores whether or not named parameters should be used in EJB queries.
     */
    public static final String QUERY_USE_NAMED_PARAMETERS = "queryUseNamedParameters";
    
    // ---------------- constructor -------------------------------
	
    public EJB3FinderMethodFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // --------------- methods ---------------------
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodFacade#getQuery()
     */
    protected java.lang.String handleGetQuery()
    {
        // first see if there is a query stored as a constraint
        String queryString = super.getQuery("query.EJB-QL");

        // otherwise see if there is a query stored as a tagged value
        if (StringUtils.isEmpty(queryString))
        {
            Object value = this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_QUERY);
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
            queryString = "FROM " + this.getOwner().getName() + " AS " + variableName;
            if (this.getArguments().size() > 0)
            {
                final Collection parameters = this.getArguments();
                if (parameters != null && !parameters.isEmpty())
                {
                    Iterator parameterIt = parameters.iterator();
                    for (int ctr = 0; parameterIt.hasNext(); ctr++)
                    {
                        EJB3FinderMethodArgumentFacade param = (EJB3FinderMethodArgumentFacade)parameterIt.next();
                        if (!param.isFirstResult() && !param.isMaxResults()) 
                        {
                            if (ctr == 0)
                            {
                                queryString = queryString + " WHERE";
                            }
                            String parameter = "?";
                            if (this.isUseNamedParameters()) 
                            {
                                parameter = ":" + param.getName();
                            }
                            else
                            {
                                parameter = parameter + ctr;
                            }
                            queryString = queryString + " " + variableName + "." + param.getName() + " = " + parameter;
                            if (parameterIt.hasNext())
                            {
                                queryString = queryString + " AND";
                            }
                        }
                    }
                }
            }
        }
        return queryString;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodFacade#getTransactionType()
     */
    protected java.lang.String handleGetTransactionType()
    {
    	return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_TYPE, true);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodFacadeLogic#handleIsUseNamedParameters()
     */
    protected boolean handleIsUseNamedParameters()
    {
        return Boolean.valueOf(String.valueOf(
                this.getConfiguredProperty(QUERY_USE_NAMED_PARAMETERS))).booleanValue();
    }

}