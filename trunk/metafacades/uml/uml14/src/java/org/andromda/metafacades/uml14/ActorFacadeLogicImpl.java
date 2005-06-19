package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.andromda.metafacades.uml.ActorFacade;


/**
 * Metaclass facade implementation.
 */
public class ActorFacadeLogicImpl
    extends ActorFacadeLogic
{
    public ActorFacadeLogicImpl(
        org.omg.uml.behavioralelements.usecases.Actor metaObject,
        String context)
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

    /**
     * @see org.andromda.metafacades.uml.ActorFacade#getGeneralizedActors()
     */
    protected java.util.List handleGetGeneralizedActors()
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
        return generalizedActors;
    }

    /**
     *
     * @see org.andromda.metafacades.uml.ActorFacade#getGeneralizedByActors()
     */
    protected java.util.List handleGetGeneralizedByActors()
    {
        final List generalizedByActors = new ArrayList();

        final Collection specializedActors = this.getSpecializations();
        for (Iterator iterator = specializedActors.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof ActorFacade)
            {
                generalizedByActors.add(object);
            }
        }
        return generalizedByActors;
    }
}