package org.andromda.cartridges.ejb.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.metafacades.uml.ParameterFacade;
import org.apache.commons.lang.StringUtils;


/**
 * <p>
 *  Represents an EJB finder method.
 * </p>
 *
 * Metaclass facade implementation.
 *
 */
public class EJBFinderMethodFacadeLogicImpl
       extends EJBFinderMethodFacadeLogic
       implements org.andromda.cartridges.ejb.metafacades.EJBFinderMethodFacade
{
    // ---------------- constructor -------------------------------
    
    public EJBFinderMethodFacadeLogicImpl (java.lang.Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class EJBFinderMethodFacade ...

    public java.lang.String getQuery() {

        // first see if we can retrieve the query from the super class.
        String queryString = super.getQuery();
        
        // now see if there is a query stored as a tagged value
        if (StringUtils.isEmpty(queryString)) {
            queryString = this.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_QUERY);
        }

        //if there wasn't any stored query, create one by default.
        if (StringUtils.isEmpty(queryString)) {
            queryString =
                "SELECT DISTINCT OBJECT(c) FROM "
                    + this.getOwner().getName()
                    + " as c";
            if (this.getParameters().size() > 0) {
                queryString = queryString + " WHERE";
                Collection parameters = this.getParameters();
                if (parameters != null && !parameters.isEmpty()) {
                    Iterator parameterIt = parameters.iterator();
                    for (int ctr = 1; parameterIt.hasNext(); ctr++) {
                        Object test = parameterIt.next();
                        ParameterFacade param = (ParameterFacade) test;
                        queryString =
                            queryString
                                + " c."
                                + param.getName()
                                + " = ?"
                                + ctr;
                        if (parameterIt.hasNext()) {
                            queryString = queryString + " AND";
                        }
                    }
                }
            }
        }
        return queryString;
    }

    public java.lang.String getViewType() {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }

    // ------------- relations ------------------

}
