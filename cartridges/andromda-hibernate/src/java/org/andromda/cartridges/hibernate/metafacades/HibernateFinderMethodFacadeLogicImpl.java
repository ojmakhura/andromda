package org.andromda.cartridges.hibernate.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.lang.StringUtils;


/**
 * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodFacade
 *
 * Metaclass facade implementation.
 *
 */
public class HibernateFinderMethodFacadeLogicImpl
       extends HibernateFinderMethodFacadeLogic
       implements org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethodFacade
{
    // ---------------- constructor -------------------------------
    
    public HibernateFinderMethodFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @see org.andromda.metafacades.uml.EntityFinderMethodFacade#getQuery()
     */
    public String getQuery() {
        
        // first see if we can retrieve the query from the super class.
        String queryString = super.getQuery();
        
        // now see if there is a query stored as a tagged value
        if (StringUtils.isEmpty(queryString)) {
            queryString = this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_QUERY);
        }

        //if there wasn't any stored query, create one by default.
        if (StringUtils.isEmpty(queryString)) {
            queryString =
                "from c in class  "
                    + this.getOwner().getFullyQualifiedName()
                    + " as c";
            if (this.getParameters().size() > 0) {
                queryString = queryString + " where";
                Collection parameters = this.getParameters();
                if (parameters != null && !parameters.isEmpty()) {
                    Iterator parameterIt = parameters.iterator();
                    for (int ctr = 0; parameterIt.hasNext(); ctr++) {
                        Object test = parameterIt.next();
                        ParameterFacade param = (ParameterFacade) test;
                        queryString =
                            queryString
                                + " c."
                                + param.getName()
                                + " = ?"
                                + ctr;
                        if (parameterIt.hasNext()) {
                            queryString = queryString + " and";
                        }
                    }
                }
            }
        }
        return queryString;
    }
    
}
