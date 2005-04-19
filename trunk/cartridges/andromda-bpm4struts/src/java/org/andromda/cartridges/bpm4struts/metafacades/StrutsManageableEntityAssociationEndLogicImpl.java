package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.AttributeFacade;

import java.util.Collection;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAssociationEnd.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAssociationEnd
 */
public class StrutsManageableEntityAssociationEndLogicImpl
    extends StrutsManageableEntityAssociationEndLogic
{
    // ---------------- constructor -------------------------------

    public StrutsManageableEntityAssociationEndLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAssociationEnd#getMessageKey()
     */
    protected java.lang.String handleGetMessageKey()
    {
        String messageKey = getName();

        final ClassifierFacade type = getType();
        if (type instanceof Entity)
        {
            final Collection identifiers = ((Entity)type).getIdentifiers();
            if (!identifiers.isEmpty())
            {
                messageKey = '.' + ((AttributeFacade)identifiers.iterator().next()).getName();
            }
        }
        return StringUtilsHelper.toResourceMessageKey(messageKey);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAssociationEnd#getMessageValue()
     */
    protected java.lang.String handleGetMessageValue()
    {
        String messageValue = null;

        final ClassifierFacade type = getType();
        if (type instanceof Entity)
        {
            messageValue = getName();
        }
        
        return StringUtilsHelper.toPhrase(messageValue);
    }

}