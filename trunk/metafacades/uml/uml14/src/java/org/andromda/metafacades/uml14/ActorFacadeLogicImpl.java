package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.ActorFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.GeneralizationFacade;

/**
 * Metaclass facade implementation.
 */
public class ActorFacadeLogicImpl
    extends ActorFacadeLogic
    implements org.andromda.metafacades.uml.ActorFacade
{
    // ---------------- constructor -------------------------------

    public ActorFacadeLogicImpl(
        org.omg.uml.behavioralelements.usecases.Actor metaObject,
        String context)
    {
        super(metaObject, context);
    }

    protected java.util.Collection handleGetGeneralizedActors()
    {
        final Collection parentActors = new ArrayList();
        final Collection generalizations = getGeneralizations();
        for (Iterator iterator = generalizations.iterator(); iterator.hasNext();)
        {
            GeneralizationFacade generalization = (GeneralizationFacade)iterator
                .next();
            GeneralizableElementFacade parent = generalization.getParent();
            parentActors.add(parent);
        }
        return parentActors;
    }

    protected java.util.Collection handleGetGeneralizedByActors()
    {
        final Collection allActors = getModel().getAllActors();
        final Collection childActors = new ArrayList();
        for (Iterator iterator = allActors.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (object instanceof ActorFacade)
            {
                ActorFacade anyActor = (ActorFacade)object;
                Collection generalizedActors = anyActor.getGeneralizedActors();
                for (Iterator actorIterator = generalizedActors.iterator(); actorIterator
                    .hasNext();)
                {
                    Object actorObject = actorIterator.next();
                    if (this.equals(actorObject))
                    {
                        childActors.add(anyActor);
                        break;
                    }
                }
            }
        }
        return childActors;
    }

}
