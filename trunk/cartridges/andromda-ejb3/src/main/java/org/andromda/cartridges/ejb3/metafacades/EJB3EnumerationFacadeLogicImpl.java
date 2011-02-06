package org.andromda.cartridges.ejb3.metafacades;

import java.util.Collection;
import org.andromda.metafacades.uml.AttributeFacade;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3EnumerationFacade.
 *
 * @see EJB3EnumerationFacade
 */
public class EJB3EnumerationFacadeLogicImpl
    extends EJB3EnumerationFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public EJB3EnumerationFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    /**
     * @see EJB3EnumerationFacadeLogic#handleGetMemberVariablesAsList(java.util.Collection, boolean, boolean)
     */
    @Override
    protected String handleGetMemberVariablesAsList(
            final Collection variables,
            final boolean includeTypes,
            final boolean includeNames)
    {
        if (!includeNames && !includeTypes || (variables == null))
        {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        String separator = "";

        for (Object obj : variables)
        {
            final AttributeFacade attr = (AttributeFacade)obj;
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
