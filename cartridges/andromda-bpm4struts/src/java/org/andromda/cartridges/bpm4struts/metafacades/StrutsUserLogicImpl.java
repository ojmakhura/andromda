package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.GeneralizationFacade;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


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

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsUser ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUser#getRole()()
     */
    public java.lang.String handleGetRole()
    {
        return getName().toLowerCase();
    }

    // ------------- relations ------------------

    public java.util.Collection handleGetGeneralizedUsers()
    {
        final Collection parentActors = new LinkedList();
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

    public java.util.Collection handleGetGeneralizedByUsers()
    {
        final Collection allActors = getModel().getAllActors();
        final Collection childUsers = new LinkedList();
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
