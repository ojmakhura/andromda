package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndParameter;
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
 * @author Bob Fields
 */
public class FrontEndViewLogicImpl
    extends FrontEndViewLogic
{
    public FrontEndViewLogicImpl(
        final Object metaObject,
        final String context)
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

    /* Moved to StateVertexFacade
     * @see org.andromda.metafacades.uml.FrontEndView#getActions()
    protected List handleGetActions()
    {
        final List actions = new ArrayList();
        final Collection outgoing = this.getOutgoings();
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
     */

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
     * @see org.andromda.metafacades.emf.uml2.ModelElementFacadeLogic#handleGetPackageName()
     */
    public String handleGetPackageName()
    {
        String packageName = null;
        final StateMachineFacade graphContext = this.getStateMachine();

        // TODO: Why not use getUseCase ?
        if (graphContext instanceof ActivityGraphFacade)
        {
            final UseCaseFacade graphUseCase = ((ActivityGraphFacade)graphContext).getUseCase();
            if (graphUseCase instanceof FrontEndUseCase)
            {
                final FrontEndUseCase useCase = (FrontEndUseCase)graphUseCase;
                packageName = useCase.getPackageName();
            }
        }
        return packageName;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getVariables()
     */
    protected List handleGetVariables()
    {
        final Map variablesMap = new LinkedHashMap();
        final Collection incoming = this.getIncomings();
        for (final Iterator iterator = incoming.iterator(); iterator.hasNext();)
        {
            final TransitionFacade transition = (TransitionFacade)iterator.next();
            final EventFacade trigger = transition.getTrigger();
            if (trigger != null)
            {
                for (final Iterator parameterIterator = trigger.getParameters().iterator();
                    parameterIterator.hasNext();)
                {
                    final ModelElementFacade modelElement = (ModelElementFacade)parameterIterator.next();
                    variablesMap.put(
                        modelElement.getName(),
                        modelElement);
                }
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
     * @see org.andromda.metafacades.uml.FrontEndView#getAllFormFields()
     */
    protected List handleGetAllFormFields()
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
     * @see org.andromda.metafacades.uml14.FrontEndView#getTables()
     */
    protected List handleGetTables()
    {
        final List variables = new ArrayList(this.getVariables());
        for (final Iterator iterator = variables.iterator(); iterator.hasNext();)
        {
            final FrontEndParameter parameter = (FrontEndParameter)iterator.next();
            if (!parameter.isTable())
            {
                iterator.remove();
            }
        }
        return variables;
    }
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getActions()
     */
    @Override
    protected List handleGetActions()
    {
        final List actions = new ArrayList();
        final Collection outgoing = this.getOutgoings();
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
}