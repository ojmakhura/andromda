package org.andromda.cartridges.spring.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.lang.StringUtils;

/**
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodFacade
 *      Metaclass facade implementation.
 */
public class HibernateFinderMethodLogicImpl
    extends HibernateFinderMethodLogic
    implements org.andromda.cartridges.spring.metafacades.HibernateFinderMethod
{
    // ---------------- constructor -------------------------------

    public HibernateFinderMethodLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.HibernateFinderMethod#getQuery()
     */
    public String handleGetQuery()
    {

        // first see if we can retrieve the query from the super class as an OCL
        // translation
        StringBuffer queryString = new StringBuffer(StringUtils.trimToEmpty(super.getQuery("query.Hibernate-QL")));

        // otherwise see if there is a query stored as a tagged value
        if (StringUtils.isEmpty(queryString.toString()))
        {
            String value = (String)this
                .findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_QUERY);
            queryString = new StringBuffer(StringUtils.trimToEmpty(value));
        }

        //if there wasn't any stored query, create one by default.
        if (StringUtils.isEmpty(queryString.toString()))
        {
            if (SpringEntity.class.isAssignableFrom(this.getOwner().getClass()))
            {
                SpringEntity entity = (SpringEntity)this.getOwner();
                String variableName = StringUtils.uncapitalize(entity.getEntityName());
                queryString.append("from ");
                queryString.append(entity.getFullyQualifiedEntityName());
                queryString.append(" as ");
                queryString.append(variableName);
                if (this.getArguments().size() > 0)
                {
                    queryString.append(" where");
                    Collection parameters = this.getArguments();
                    if (parameters != null && !parameters.isEmpty())
                    {
                        Iterator parameterIt = parameters.iterator();
                        for (int ctr = 0; parameterIt.hasNext(); ctr++)
                        {
                            Object test = parameterIt.next();
                            ParameterFacade param = (ParameterFacade)test;
                            queryString.append(" ");
                            queryString.append(variableName);
                            queryString.append(".");
                            queryString.append(param.getName());
                            queryString.append(" = ?");
                            if (parameterIt.hasNext())
                            {
                                queryString.append(" and");
                            }
                        }
                    }
                }
            }
        }
        return queryString.toString();
    }

}