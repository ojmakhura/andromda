package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsUser
 */
public class StrutsUserLogicImpl
    extends StrutsUserLogic
{
    // ---------------- constructor -------------------------------

    public StrutsUserLogicImpl(java.lang.Object metaObject, String context)
    {
        super(metaObject, context);
    }
    
    protected java.util.Collection handleGetGeneralizedUsers()
    {
        List generalizedUsers = new ArrayList();

        final Collection parentActors = getGeneralizedActors();
        for (Iterator iterator = parentActors.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (object instanceof StrutsUser)
            {
                generalizedUsers.add(object);
            }
        }
        return parentActors;
    }

    protected java.util.Collection handleGetGeneralizedByUsers()
    {
        List generalizedByUsers = new ArrayList();

        final Collection parentActors = getGeneralizedByActors();
        for (Iterator iterator = parentActors.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (object instanceof StrutsUser)
            {
                generalizedByUsers.add(object);
            }
        }
        return parentActors;
    }

    protected boolean handleIsAssociatedWithStrutsUseCase()
    {
        boolean associated = false;

        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext() && !associated;)
        {
            AssociationEndFacade associationEnd = (AssociationEndFacade) iterator.next();
            ClassifierFacade classifier = associationEnd.getOtherEnd().getType();
            associated = classifier instanceof StrutsUseCase;
        }

        // a generalized user is a StrutsUser, and therefore is associated with the StrutsUseCase
        if (associated == false)
        {
            associated = !getGeneralizedUsers().isEmpty();
        }

        return associated;
    }
}
