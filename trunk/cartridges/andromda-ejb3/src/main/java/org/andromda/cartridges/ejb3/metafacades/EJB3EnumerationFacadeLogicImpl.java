package org.andromda.cartridges.ejb3.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.AttributeFacade;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3EnumerationFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3EnumerationFacade
 */
public class EJB3EnumerationFacadeLogicImpl
    extends EJB3EnumerationFacadeLogic
{

    public EJB3EnumerationFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EnumerationFacadeLogic#handleGetMemberVariablesAsList(java.util.Collection, boolean, boolean)
     */
    protected String handleGetMemberVariablesAsList(
            final Collection variables,
            final boolean includeTypes,
            final boolean includeNames)
    {
        if (!includeNames && !includeTypes || variables == null)
        {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        String separator = "";

        for (final Iterator it = variables.iterator(); it.hasNext();)
        {
            final AttributeFacade attr = (AttributeFacade)it.next();
            sb.append(separator);
            separator = ", ";
            if (includeTypes)
            {
                sb.append(attr.getType().getFullyQualifiedName());
                sb.append(" ");
            }
            if (includeNames)
            {
                sb.append(attr.getName());
            }
        }
        return sb.toString();
    }
}