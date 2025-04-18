package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
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
    private static final long serialVersionUID = 3878476296052353951L;

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndViewLogicImpl(
        Object metaObject,
        String context)
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

    // TODO StrutsFinalStateLogicImpl uses getIncomings instead of getOutgoings - why?
    /* Moved to StateVertexFacade
     * @see org.andromda.metafacades.uml.FrontEndView#getActions()
    @Override
    protected List<FrontEndAction> handleGetActions()
    {
        final List<FrontEndAction> actions = new ArrayList<FrontEndAction>();
        final Collection<TransitionFacade> outgoing = getOutgoings();
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
    protected FrontEndUseCase handleGetUseCase()
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
        return (FrontEndUseCase)useCase;
    }

    /**
     * Override to create the package of the view.
     *
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetPackageName()
     */
    @Override
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
        final Collection<TransitionFacade> incoming = getIncomings();
        for (final Iterator iterator = incoming.iterator(); iterator.hasNext();)
        {
            final TransitionFacade transition = (TransitionFacade)iterator.next();
            final EventFacade trigger = transition.getTrigger();
            if (trigger != null)
            {
                for (final Iterator parameterIterator = trigger.getParameters().iterator(); parameterIterator.hasNext();)
                {
                    final ModelElementFacade modelElement = (ModelElementFacade)parameterIterator.next();
                    variablesMap.put(modelElement.getName(), modelElement);
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
        for (final Iterator<FrontEndAction> iterator = actions.iterator(); iterator.hasNext();)
        {
            final FrontEndAction action = iterator.next();
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
        final List<FrontEndParameter> actionParameters = new ArrayList<FrontEndParameter>();
        final Collection<FrontEndAction> actions = getActions();
        for (final Iterator<FrontEndAction> iterator = actions.iterator(); iterator.hasNext();)
        {
            final FrontEndAction action = iterator.next();
            actionParameters.addAll(action.getParameters());
        }
        return actionParameters;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndView#getTables()
     */
    @Override
    protected List<FrontEndParameter> handleGetTables()
    {
        final List<FrontEndParameter> variables = new ArrayList<FrontEndParameter>(this.getVariables());
        for (final Iterator<FrontEndParameter> iterator = variables.iterator(); iterator.hasNext();)
        {
            final FrontEndParameter parameter = iterator.next();
            if (!parameter.isTable())
            {
                iterator.remove();
            }
        }
        return variables;
    }

    /**
     * @return Outgoings FrontEndActions
     * @see org.andromda.metafacades.uml.FrontEndView#getActions()
     */
    @Override
    protected List<FrontEndAction> handleGetActions()
    {
        final List<FrontEndAction> actions = new ArrayList<FrontEndAction>();
        final Collection<TransitionFacade> outgoings = getOutgoings();
        for (final Iterator<TransitionFacade> iterator = outgoings.iterator(); iterator.hasNext();)
        {
            final TransitionFacade object = iterator.next();
            if (object instanceof FrontEndAction)
            {
                actions.add((FrontEndAction)object);
            }
        }
        // TODO StrutsFinalStateLogicImpl uses getIncomings INSTEAD OF getOutgoings - why?
        /*final Collection<TransitionFacade> incomings = getIncomings();
        for (final Iterator<TransitionFacade> iterator = incomings.iterator(); iterator.hasNext();)
        {
            final TransitionFacade object = iterator.next();
            if (object instanceof FrontEndAction)
            {
                actions.add((FrontEndAction)object);
            }
        }*/
        return actions;
    }

    @Override
    protected String handleGetPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetTitleKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetTitleValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetMessageKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetDocumentationKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetDocumentationValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetMessageValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetFullyQualifiedPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetPopulatorPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetPopulator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean handleIsPopulatorRequired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsValidationRequired() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsPopup() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsNonTableVariablesPresent() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean handleIsHasNameOfUseCase() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String handleGetFormKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetFromOutcome() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean handleIsNeedsFileUpload() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String handleGetFullyQualifiedPageObjectClassPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetFullyQualifiedPageObjectClassName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetPageObjectClassName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<FrontEndAction> handleGetFormActions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<FrontEndForward> handleGetActionForwards() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected List<FrontEndForward> handleGetForwards() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetPageObjectBeanName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection handleGetBackingValueVariables() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetFilename() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection handleGetAllowedRoles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetRestPath() {
                
        return UMLMetafacadeUtils.getRestPath(this, this.getName());
    }

    @Override
    protected Collection<String> handleGetFrontEndClasses() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetIcon() {
        return UMLMetafacadeUtils.getIcon(this);
    }

    @Override
    protected String handleGetDisplayCondition() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection handleGetAttributes() {
        // TODO Auto-generated method stub
        return null;
    }
}