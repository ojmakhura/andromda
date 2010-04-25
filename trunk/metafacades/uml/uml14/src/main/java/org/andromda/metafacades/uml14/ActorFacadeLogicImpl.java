package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.ActorFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.omg.uml.behavioralelements.usecases.Actor;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class ActorFacadeLogicImpl
    extends ActorFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ActorFacadeLogicImpl(
        Actor metaObject,
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
    @Override
    protected List<ActorFacade> handleGetGeneralizedActors()
    {
        final List<ActorFacade> generalizedActors = new ArrayList<ActorFacade>();

        final Collection<GeneralizableElementFacade> parentActors = this.getGeneralizations();
        for (final Iterator<GeneralizableElementFacade> iterator = parentActors.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof ActorFacade)
            {
                generalizedActors.add((ActorFacade) object);
            }
        }
        return generalizedActors;
    }

    /**
     *
     * @see org.andromda.metafacades.uml.ActorFacade#getGeneralizedByActors()
     */
    @Override
    protected List<ActorFacade> handleGetGeneralizedByActors()
    {
        final List<ActorFacade> generalizedByActors = new ArrayList<ActorFacade>();

        final Collection<GeneralizableElementFacade> specializedActors = this.getSpecializations();
        for (final Iterator<GeneralizableElementFacade> iterator = specializedActors.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof ActorFacade)
            {
                generalizedByActors.add((ActorFacade) object);
            }
        }
        return generalizedByActors;
    }
}