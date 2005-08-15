package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndView.
 *
 * @see org.andromda.metafacades.uml.FrontEndView
 */
public class FrontEndViewLogicImpl
    extends FrontEndViewLogic
{
    public FrontEndViewLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#isFrontEndView()
     */
    protected boolean handleIsFrontEndView()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_VIEW);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getActions()
     */
    protected List handleGetActions()
    {
        final List actions = new ArrayList();
        final Collection outgoing = getOutgoing();
        for (final Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof FrontEndAction)
            {
                actions.add(object);
            }
        }
        return actions;
    }
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getUseCase()
     */
    protected Object handleGetUseCase()
    {
        UseCaseFacade useCase = null;
        final StateMachineFacade graphContext = this.getStateMachine();
        if (graphContext instanceof ActivityGraphFacade)
        {
            useCase = ((ActivityGraphFacade)graphContext).getUseCase();
            if (!(useCase instanceof FrontEndUseCase))
            {
                useCase = null;
            }
        }
        return useCase;
    }
    
    /**
     * Override to create the package of the view.
     * 
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetPackageName()
     */
    public String handleGetPackageName()
    {
        String packageName = null;
        final StateMachineFacade graphContext = this.getStateMachine();
        if (graphContext instanceof ActivityGraphFacade)
        {
            final UseCaseFacade graphUseCase = ((ActivityGraphFacade)graphContext).getUseCase();
            if (graphUseCase instanceof FrontEndUseCase)
            {
                final FrontEndUseCase useCase = (FrontEndUseCase)graphUseCase;
                if (useCase != null)
                {
                    packageName = useCase.getPackageName();
                }
            }
        }
        return packageName;
    }
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getVariables()
     */
    protected List handleGetVariables()
    {
        final Map variablesMap = new HashMap();
        final Collection incoming = getIncoming();
        for (final Iterator iterator = incoming.iterator(); iterator.hasNext();)
        {
            final TransitionFacade transition = (TransitionFacade)iterator.next();
            final EventFacade trigger = transition.getTrigger();
            if (trigger != null)
            {
                this.collectByName(trigger.getParameters(), variablesMap);
            }
        }
        return new ArrayList(variablesMap.values());
    }
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getAllActionParameters()
     */
    protected List handleGetAllActionParameters()
    {
        final List actionParameters = new ArrayList();
        final Collection actions = this.getActions();
        for (final Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            final FrontEndAction action = (FrontEndAction)iterator.next();
            actionParameters.addAll(action.getParameters());
        }
        return actionParameters;
    }
    
    /**
     * Iterates over the model elements and maps their name on their instance in the argument map.
     */
    private final void collectByName(final Collection modelElements, final Map elementMap)
    {
        for (final Iterator iterator = modelElements.iterator(); iterator.hasNext();)
        {
            final ModelElementFacade modelElement = (ModelElementFacade)iterator.next();
            elementMap.put(modelElement.getName(), modelElement);
        }
    }
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getAllFormFields()
     */
    protected List handleGetAllFormFields()
    {
        final List actionParameters = new ArrayList();
        final Collection actions = getActions();
        for (final Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            final FrontEndAction action = (FrontEndAction)iterator.next();
            actionParameters.addAll(action.getParameters());
        }
        return actionParameters;
    }
}