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

    public java.lang.String handleGetQuery() {

         // first see if there is a query stored as a constraint
        String queryString = super.getQuery("query.EJB-QL");

        // otherwise see if there is a query stored as a tagged value
        if (StringUtils.isEmpty(queryString)) {
            Object value = this.findTaggedValue(EJBProfile.TAGGEDVALUE_EJB_QUERY);
            queryString = value==null?null:value.toString();
        }

        //if there wasn't any stored query, create one by default.
        if (StringUtils.isEmpty(queryString)) {
            queryString =
                "SELECT DISTINCT OBJECT(c) FROM "
                    + this.getOwner().getName()
                    + " as c";
            if (this.getArguments().size() > 0) {
                queryString = queryString + " WHERE";
                Collection parameters = this.getArguments();
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

    public java.lang.String handleGetViewType() {
        // TODO: put your implementation here.

        // Dummy return value, just that the file compiles
        return null;
    }

    // ------------- relations ------------------

}
