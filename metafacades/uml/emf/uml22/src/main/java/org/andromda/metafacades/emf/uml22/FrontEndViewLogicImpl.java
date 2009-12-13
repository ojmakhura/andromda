package org.andromda.metafacades.emf.uml22;

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
import org.andromda.metafacades.uml.ParameterFacade;
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
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndViewLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#isFrontEndView()
     */
    @Override
    protected boolean handleIsFrontEndView()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_VIEW);
    }

    /* Moved to StateVertexFacade
     * @see org.andromda.metafacades.uml.FrontEndView#getActions()
    @Override
    protected List<FrontEndAction> handleGetActions()
    {
        final List<FrontEndAction> actions = new ArrayList<FrontEndAction>();
        final Collection<TransitionFacade> outgoing = this.getOutgoings();
        for (final Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof FrontEndAction)
            {
                actions.add((FrontEndAction)object);
            }
        }
        return actions;
    }
     */

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getUseCase()
     */
    @Override
    protected UseCaseFacade handleGetUseCase()
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
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleGetPackageName()
     */
    @Override
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
    @Override
    protected List<ModelElementFacade> handleGetVariables()
    {
        final Map<String, ModelElementFacade> variablesMap = new LinkedHashMap<String, ModelElementFacade>();
        final Collection<TransitionFacade> incoming = this.getIncomings();
        for (final Iterator<TransitionFacade> iterator = incoming.iterator(); iterator.hasNext();)
        {
            final TransitionFacade transition = iterator.next();
            final EventFacade trigger = transition.getTrigger();
            if (trigger != null)
            {
                for (final Iterator<ParameterFacade> parameterIterator = trigger.getParameters().iterator();
                    parameterIterator.hasNext();)
                {
                    final ModelElementFacade modelElement = (ModelElementFacade)parameterIterator.next();
                    variablesMap.put(
                        modelElement.getName(),
                        modelElement);
                }
            }
        }
        return new ArrayList<ModelElementFacade>(variablesMap.values());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getAllActionParameters()
     */
    @Override
    protected List<FrontEndParameter> handleGetAllActionParameters()
    {
        final List<FrontEndParameter> actionParameters = new ArrayList();
        final Collection<FrontEndAction> actions = this.getActions();
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
    @Override
    protected List<FrontEndParameter> handleGetAllFormFields()
    {
        final List<FrontEndParameter> actionParameters = new ArrayList();
        final Collection<FrontEndAction> actions = this.getActions();
        for (final Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
            final FrontEndAction action = (FrontEndAction)iterator.next();
            actionParameters.addAll(action.getParameters());
        }
        return actionParameters;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.FrontEndViewLogic#getTables()
     */
    @Override
    protected List<FrontEndParameter> handleGetTables()
    {
        final List<FrontEndParameter> variables = new ArrayList<FrontEndParameter>(this.getVariables());
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
    protected List<FrontEndAction> handleGetActions()
    {
        final List<FrontEndAction> actions = new ArrayList<FrontEndAction>();
        final Collection<TransitionFacade> outgoing = this.getOutgoings();
        for (final Iterator<TransitionFacade> iterator = outgoing.iterator(); iterator.hasNext();)
        {
            final TransitionFacade object = iterator.next();
            if (object instanceof FrontEndAction)
            {
                actions.add((FrontEndAction)object);
            }
        }
        return actions;
    }
}
