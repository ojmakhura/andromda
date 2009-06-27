package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.ActorFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.eclipse.uml2.uml.Actor;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ActorFacade.
 *
 * @see org.andromda.metafacades.uml.ActorFacade
 */
public class ActorFacadeLogicImpl
    extends ActorFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ActorFacadeLogicImpl(
        final Actor metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
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
                generalizedActors.add((ActorFacade)object);
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
        final List<ActorFacade> generalizedByActors = new ArrayList();

        final Collection<GeneralizableElementFacade> specializedActors = this.getSpecializations();
        for (final Iterator<GeneralizableElementFacade> iterator = specializedActors.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof ActorFacade)
            {
                generalizedByActors.add((ActorFacade)object);
            }
        }
        return generalizedByActors;
    }
}
