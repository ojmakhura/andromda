package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.GeneralizationFacade;
import org.andromda.core.common.StringUtilsHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUser
 */
public class StrutsUserLogicImpl
        extends StrutsUserLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsUser
{
    // ---------------- constructor -------------------------------

    public StrutsUserLogicImpl(java.lang.Object metaObject, String context)
    {
        super(metaObject, context);
    }
    
    protected java.util.Collection handleGetGeneralizedUsers()
    {
        final Collection parentActors = new ArrayList();
        final Collection generalizations = getGeneralizations();
        for (Iterator iterator = generalizations.iterator(); iterator.hasNext();)
        {
            GeneralizationFacade generalization = (GeneralizationFacade) iterator.next();
            GeneralizableElementFacade parent = generalization.getParent();
            if (parent instanceof StrutsUser)
                parentActors.add(parent);
        }
        return parentActors;
    }

    protected java.util.Collection handleGetGeneralizedByUsers()
    {
        final Collection allActors = getModel().getAllActors();
        final Collection childUsers = new ArrayList();
        for (Iterator iterator = allActors.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (object instanceof StrutsUser)
            {
                StrutsUser anyUser = (StrutsUser) object;
                Collection generalizedUsers = anyUser.getGeneralizedUsers();
                for (Iterator userIterator = generalizedUsers.iterator(); userIterator.hasNext();)
                {
                    Object strutsUserObject = userIterator.next();
                    if (this.equals(strutsUserObject))
                    {
                        childUsers.add(anyUser);
                        break;
                    }
                }
            }
        }
        return childUsers;
    }
}
