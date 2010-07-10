package org.andromda.cartridges.ejb3.metafacades;

import java.util.Collection;
import java.util.Iterator;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <p/>
 * Represents an EJB finder method. </p>
 *
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodFacade.
 *
 * @see EJB3FinderMethodFacade
 */
public class EJB3FinderMethodFacadeLogicImpl
    extends EJB3FinderMethodFacadeLogic
{
    /**
     * Stores whether or not named parameters should be used in EJB queries.
     */
    private static final String QUERY_USE_NAMED_PARAMETERS = "queryUseNamedParameters";

    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public EJB3FinderMethodFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    // --------------- methods ---------------------

    /**
     * Stores the translated query so that its only translated once.
     */
    private String translatedQuery = null;

    /**
     * Retrieves the translated query.
     */
    private String getTranslatedQuery()
    {
        if (this.translatedQuery == null)
        {
            this.translatedQuery = super.getQuery("query.Hibernate-QL");
        }
        return this.translatedQuery;
    }

    /**
     * @see EJB3FinderMethodFacade#getQuery()
     */
    @Override
    protected String handleGetQuery()
    {
        return this.getQuery((EJB3EntityFacade)null);
    }

    /**
     * @see EJB3FinderMethodFacade#getTransactionType()
     */
    @Override
    protected String handleGetTransactionType()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_TYPE, true);
    }

    /**
     * @see EJB3FinderMethodFacadeLogic#handleIsUseNamedParameters()
     */
    @Override
    protected boolean handleIsUseNamedParameters()
    {
        return BooleanUtils.toBoolean(String.valueOf(
                this.getConfiguredProperty(QUERY_USE_NAMED_PARAMETERS)));
    }

    /**
     * @see EJB3FinderMethodFacadeLogic#handleIsUseQueryCache()
     */
    @Override
    protected boolean handleIsUseQueryCache()
    {
        boolean queryCacheEnabled = ((EJB3EntityFacade)this.getOwner()).isUseQueryCache();
        String queryCacheEnabledStr = (String)super.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_USE_QUERY_CACHE);
        if (StringUtils.isNotBlank(queryCacheEnabledStr))
        {
            queryCacheEnabled = BooleanUtils.toBoolean(queryCacheEnabledStr);
        }
        return queryCacheEnabled;
    }

    /**
     * @see EJB3FinderMethodFacadeLogic#handleGetQuery(EJB3EntityFacade)
     */
    @Override
    protected String handleGetQuery(final EJB3EntityFacade entity)
    {
        // first see if we can retrieve the query from the super class as an OCL
        // translation
        String queryString = this.getTranslatedQuery();

        boolean whereClauseExists = false;

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
            ModelElementFacade owner;
            if (entity == null)
            {
                owner = this.getOwner();
            }
            else
            {
                owner = entity;
            }
            String variableName = StringUtils.uncapitalize(owner.getName());
            queryString = "from " + owner.getName() + " as " + variableName;
            if (this.getArguments().size() > 0)
            {
                Collection<ParameterFacade> arguments = this.getArguments();
                if (arguments != null && !arguments.isEmpty())
                {
                    Iterator argumentIt = arguments.iterator();
                    for (int ctr = 0; argumentIt.hasNext(); ctr++)
                    {
                        EJB3FinderMethodArgumentFacade argument = (EJB3FinderMethodArgumentFacade)argumentIt.next();
                        if (!argument.isFirstResult() && !argument.isMaxResults())
                        {
                            if (!whereClauseExists)
                            {
                                queryString += " where";
                                whereClauseExists = true;
                            }
                            String parameter = "?";
                            if (this.isUseNamedParameters())
                            {
                                parameter = ':' + argument.getName();
                            }
                            queryString += ' ' + variableName + '.' + argument.getName() + " = " + parameter;
                            if (argumentIt.hasNext())
                            {
                                queryString += " and";
                            }
                        }
                    }
                }
            }
        }
        return queryString;
    }

    /**
     * @see EJB3FinderMethodFacadeLogic#handleIsNamedQuery()
     */
    @Override
    protected boolean handleIsNamedQuery()
    {
        boolean isNamedQuery = true;
        final String query = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_QUERY);
        if (StringUtils.isNotBlank(query))
        {
            isNamedQuery = false;
        }
        return isNamedQuery;
    }
}
