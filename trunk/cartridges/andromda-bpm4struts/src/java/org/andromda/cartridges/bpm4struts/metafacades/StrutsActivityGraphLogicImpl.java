package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.PseudostateFacade;

import java.util.Collection;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActivityGraph
 */
public class StrutsActivityGraphLogicImpl
        extends StrutsActivityGraphLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsActivityGraph
{
    // ---------------- constructor -------------------------------

    public StrutsActivityGraphLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }
    // ------------- relations ------------------

    protected Object handleGetFirstAction()
    {
        Object firstAction = null;
        Collection initialStates = getInitialStates();
        if (initialStates.isEmpty() == false)
        {
            PseudostateFacade initialState = (PseudostateFacade)initialStates.iterator().next();
            Collection outgoing = initialState.getOutgoing();
            firstAction = (outgoing.isEmpty()) ? null : outgoing.iterator().next();
        }
        return firstAction;
    }

    protected Object handleGetUseCase()
    {
        /*
         * First check whether this activity graph resides in the namespace of a use-case
         */
        Collection useCases = getModel().getAllUseCases();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            if (obj instanceof StrutsUseCase)
            {
                StrutsUseCase strutsUseCase = (StrutsUseCase) obj;
                if (this.equals(strutsUseCase.getActivityGraph()))
                {
                    return strutsUseCase;
                }
            }
        }

        /*
         * If this was not the case then look for a use-case pointing to this activity graph using a tagged
         * value, if this activity graph has no name we do not try to search
         */
        final String name = getName();
        if (name != null)
        {
            for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
            {
                Object obj = iterator.next();
                if (obj instanceof StrutsUseCase)
                {
                    StrutsUseCase strutsUseCase = (StrutsUseCase) obj;
                    Object activity = strutsUseCase.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_USECASE_ACTIVITY);

                    if (activity != null)
                    {
                        String activityName = activity.toString();
                        if (name.equalsIgnoreCase(activityName))
                            return strutsUseCase;
                    }
                }
            }
        }

        /*
         * If still nothing can be found, return without any result
         */
        return null;
    }

    protected Object handleGetController()
    {
        final ModelElementFacade contextElement = getContextElement();
        if (contextElement instanceof StrutsController)
        {
            return contextElement;
        }

        /*
         * for those tools not supporting setting the context of an activity graph (such as Poseidon)
         * an alternative is implemented: a tagged value on the controller, specifying the name of the use-case
         *
         * It is also allowed to set a hyperlink from the controller to the usecase
         */

        UseCaseFacade useCase = this.getUseCase();
        if (useCase != null)
        {
            final String useCaseName = useCase.getName();
            final StrutsUseCase thisUseCase = getUseCase();

            // loop over the controllers, looking for the tagged value matching this activity graph's use-case name
            Collection allClasses = getModel().getRootPackage().getClasses();
            for (Iterator classIterator = allClasses.iterator(); classIterator.hasNext();)
            {
                final ModelElementFacade element = (ModelElementFacade) classIterator.next();

                if (element instanceof StrutsController)
                {
                    final Object hyperlinkModel = element.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HYPERLINK);
                    if (thisUseCase.equals(hyperlinkModel))
                    {
                        return element;
                    }

                    final Object value = element.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_CONTROLLER_USE_CASE);
                    String taggedValue = value == null ? null : value.toString();
                    if (useCaseName.equalsIgnoreCase(taggedValue))
                    {
                        return element;
                    }
                }
            }
        }
        return null;
    }
}
