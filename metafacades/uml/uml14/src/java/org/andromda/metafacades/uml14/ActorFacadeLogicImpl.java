package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.ActorFacade;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;


/**
 * Metaclass facade implementation.
 */
public class ActorFacadeLogicImpl
        extends ActorFacadeLogic
{
    // ---------------- constructor -------------------------------

    public ActorFacadeLogicImpl(org.omg.uml.behavioralelements.usecases.Actor metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getPackage();
    }

    protected java.util.Collection handleGetGeneralizedActors()
    {
        final List generalizedActors = new ArrayList();

        final Collection parentActors = this.getGeneralizations();
        for (Iterator iterator = parentActors.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof ActorFacade)
            {
                generalizedActors.add(object);
            }
        }
        return parentActors;
    }

    protected java.util.Collection handleGetGeneralizedByActors()
    {
        final List generalizedByActors = new ArrayList();

        final Collection parentActors = this.getSpecializations();
        for (Iterator iterator = parentActors.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof ActorFacade)
            {
                generalizedByActors.add(object);
            }
        }
        return parentActors;
    }

}
