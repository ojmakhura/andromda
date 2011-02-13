package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;



/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsPseudostate
 */
public class StrutsPseudostateLogicImpl
    extends StrutsPseudostateLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public StrutsPseudostateLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getStateMachine() instanceof StrutsActivityGraph
     */
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getStateMachine() instanceof StrutsActivityGraph;
    }

    /**
     * @return actionMethodName
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsPseudostate#getActionMethodName()
     */
    protected String handleGetActionMethodName()
    {
        final String methodName = getName();
        return (methodName == null) ?
            "a" + System.currentTimeMillis() : StringUtilsHelper.lowerCamelCaseName(methodName);
    }

    /**
     * Overridden and not typesafe since StrutsAction does not extend FrontEndAction
     *
     * @see org.andromda.metafacades.uml.FrontEndPseudostate#getContainerActions()
     */
    public List getContainerActions()
    {
        final Set<StrutsAction> actionSet = new LinkedHashSet<StrutsAction>();
        final StateMachineFacade graphContext = getStateMachine();

        if (graphContext instanceof ActivityGraphFacade)
        {
            final UseCaseFacade useCase = ((ActivityGraphFacade)graphContext).getUseCase();

            if (useCase instanceof StrutsUseCase)
            {
                // StrutsUseCase.getActions returns StrutsAction which cannot be cast to FrontEndAction
                for (final Object action : ((StrutsUseCaseLogicImpl)useCase).getActions())
                {
                    for (final StrutsForward transition : ((StrutsAction)action).getTransitions())
                    {
                        if (this.equals(transition.getTarget()))
                        {
                            actionSet.add((StrutsAction)action);
                        }
                    }
                }
            }
        }
        return new ArrayList<StrutsAction>(actionSet);
    }
}
