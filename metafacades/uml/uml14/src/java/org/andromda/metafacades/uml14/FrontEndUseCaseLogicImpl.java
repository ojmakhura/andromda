package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndUseCase.
 *
 * @see org.andromda.metafacades.uml.FrontEndUseCase
 */
public class FrontEndUseCaseLogicImpl
    extends FrontEndUseCaseLogic
{
    public FrontEndUseCaseLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#isEntryUseCase()
     */
    protected boolean handleIsEntryUseCase()
    {
        return hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_APPLICATION);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getController()
     */
    protected java.lang.Object handleGetController()
    {
        final FrontEndActivityGraph graph = this.getActivityGraph();
        return graph == null ? null : graph.getController();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getActivityGraph()
     */
    protected java.lang.Object handleGetActivityGraph()
    {
        ActivityGraphFacade activityGraph = null;

        // - in case there is a tagged value pointing to an activity graph, and this graph is found,
        //   return it.
        final Object activity = findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_USECASE_ACTIVITY);
        if (activity != null)
        {
            final String activityName = String.valueOf(activity.toString());
            activityGraph = getModel().findActivityGraphByName(activityName);
        }

        // - otherwise just take the first one in this use-case's namespace.
        if (activityGraph == null)
        {
            final Collection ownedElements = getOwnedElements();
            for (final Iterator iterator = ownedElements.iterator(); iterator.hasNext();)
            {
                final Object object = iterator.next();
                if (object instanceof FrontEndActivityGraph)
                {
                    return object;
                }
            }
        }

        return activityGraph;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getReferencingFinalStates()
     */
    protected List handleGetReferencingFinalStates()
    {
        return new ArrayList(this.getModel().findFinalStatesWithNameOrHyperlink(this));
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getAllUseCases()
     */
    protected List handleGetAllUseCases()
    {
        final List useCases = new ArrayList();
        for (final Iterator useCaseIterator = getModel().getAllUseCases().iterator(); useCaseIterator.hasNext();)
        {
            final Object object = useCaseIterator.next();
            if (object instanceof FrontEndUseCase)
                useCases.add(object);
        }
        return useCases;
    }
}