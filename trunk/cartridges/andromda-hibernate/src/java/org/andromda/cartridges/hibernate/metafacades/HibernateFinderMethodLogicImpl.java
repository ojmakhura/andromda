package org.andromda.cartridges.hibernate.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.lang.StringUtils;


/**
 * @author Chad Brandon
 * @author Carlos Cuenca
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodFacade
 *      Metaclass facade implementation.
 */
public class HibernateFinderMethodLogicImpl
    extends HibernateFinderMethodLogic
{
    // ---------------- constructor -------------------------------
    public HibernateFinderMethodLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethod#getQuery()
     */
    protected String handleGetQuery()
    {
        // first see if we can retrieve the query from the super class as an OCL
        // translation
        String queryString = this.getTranslatedQuery();

        // otherwise see if there is a query stored as a tagged value
        if (StringUtils.isEmpty(queryString))
        {
            Object value = this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_QUERY);
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
            queryString = "from " + this.getOwner().getFullyQualifiedName() + " as " + variableName;
            if (this.getArguments().size() > 0)
            {
                queryString = queryString + " where";
                Collection arguments = this.getArguments();
                if (arguments != null && !arguments.isEmpty())
                {
                    Iterator argumentIt = arguments.iterator();
                    for (int ctr = 0; argumentIt.hasNext(); ctr++)
                    {
                        ParameterFacade argument = (ParameterFacade)argumentIt.next();
                        String parameter = "?";
                        if (this.isUseNamedParameters())
                        {
                            parameter = ":" + argument.getName();
                        }
                        queryString = queryString + " " + variableName + "." + argument.getName() + " = " + parameter;
                        if (argumentIt.hasNext())
                        {
                            queryString = queryString + " and";
                        }
                    }
                }
            }
        }
        return queryString;
    }

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
     * Stores whether or not named parameters should be used in hibernate
     * queries.
     */
    private static final String USE_NAMED_PARAMETERS = "hibernateQueryUseNamedParameters";

    /**
     * @see org.andromda.cartridges.spring.metafacades.HibernateFinderMethod#isUseNamedParameters()
     */
    protected boolean handleIsUseNamedParameters()
    {
        boolean useNamedParameters = Boolean.valueOf(String.valueOf(this.getConfiguredProperty(USE_NAMED_PARAMETERS))).booleanValue()
                || StringUtils.isNotBlank(this.getTranslatedQuery());
        return HibernateMetafacadeUtils.getUseNamedParameters(this, useNamedParameters);
    }

    /**
     * Stores the value indicating whether or not to use hibernate query
     * caching.
     */
    private static final String HIBERNATE_USE_QUERY_CACHE = "hibernateUseQueryCache";

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethod#isUseQueryCache()
     */
    protected boolean handleIsUseQueryCache()
    {
        boolean useQueryCache =
            Boolean.valueOf(String.valueOf(this.getConfiguredProperty(HIBERNATE_USE_QUERY_CACHE))).booleanValue();

        if (useQueryCache)
        {
            useQueryCache =
                Boolean.valueOf(
                    String.valueOf(findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_USE_QUERY_CACHE)))
                       .booleanValue();
        }
        return useQueryCache;
    }
}