package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsFinalState
 * @author Bob Fields
 */
public class StrutsFinalStateLogicImpl
    extends StrutsFinalStateLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public StrutsFinalStateLogicImpl(
        java.lang.Object metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String getName()
    {
        String name = super.getName();

        if (name == null)
        {
            final UseCaseFacade useCase = this.getTargetUseCase();
            if (useCase != null)
            {
                name = useCase.getName();
            }
        }

        return name;
    }

    protected String handleGetFullPath()
    {
        String fullPath = null;

        final StrutsUseCase useCase = (StrutsUseCase)this.getTargetUseCase();
        if (useCase == null)
        {
            // perhaps this final state links outside of the UML model
            final Object taggedValue = this.findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK);
            if (taggedValue == null)
            {
                String name = getName();
                if (name != null && (name.startsWith("/") || name.startsWith("http://")))
                {
                    fullPath = name;
                }
            }
            else
            {
                fullPath = String.valueOf(taggedValue);
            }
        }
        else
        {
            fullPath = useCase.getActionPath() + ".do";
        }
        return fullPath;
    }

    /**
     * Overridden for now (@todo need to figure out why it doesn't work correctly when using
     * the one from the FrontEndFinalState).
     *
     * @see org.andromda.metafacades.uml.FrontEndFinalState#getTargetUseCase()
     */
    public FrontEndUseCase getTargetUseCase()
    {
        FrontEndUseCase targetUseCase = null;
        // first check if there is a hyperlink from this final state to a use-case
        // this works at least in MagicDraw
        final Object taggedValue = this.findTaggedValue(UMLProfile.TAGGEDVALUE_MODEL_HYPERLINK);
        if (taggedValue != null)
        {
            if (taggedValue instanceof StrutsActivityGraph)
            {
                targetUseCase = (FrontEndUseCase)((StrutsActivityGraph)taggedValue).getUseCase();
            }
            else if (taggedValue instanceof StrutsUseCase)
            {
                targetUseCase = (FrontEndUseCase)taggedValue;
            }
        }
        
        // maybe the name points to a use-case ?
        if (targetUseCase == null)
        {
            final String name = super.getName();
            if (StringUtils.isNotBlank(name))
            {
                UseCaseFacade useCase = getModel().findUseCaseByName(name);
                if (useCase instanceof FrontEndUseCase)
                {
                    targetUseCase = (FrontEndUseCase)useCase;
                }
            }
        }
        return targetUseCase;
    }

    /**
     * Need to override default handling in StateVertexFacade.handleGetActions()
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsFinalStateLogic#getActions()
     */
    public List getActions()
    {
        return handleGetActions();
    }
    
    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsFinalStateLogic#getActions()
     */
    // TODO StateVertexFacade.handleGetActions calls getOutgoings. Why?
    // Changed from previous handleGetActions because that could never be called by FacadeLogic which delegates to StateVertexFacade.
    //@Override
    protected List handleGetActions()
    {
        Set<FrontEndAction> actions = new LinkedHashSet<FrontEndAction>();
        Collection<TransitionFacade> incomings = this.getIncomings();

        for (final Iterator<TransitionFacade> incomingIterator = incomings.iterator(); incomingIterator.hasNext();)
        {
            StrutsForward forward = (StrutsForward)incomingIterator.next();
            actions.addAll(forward.getActions());
        }
        return new ArrayList<FrontEndAction>(actions);
    }
}
