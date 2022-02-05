package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndManageableEntity;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ManageableEntity;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.lang3.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndFinalState.
 *
 * @see org.andromda.metafacades.uml.FrontEndFinalState
 */
public class FrontEndFinalStateLogicImpl
    extends FrontEndFinalStateLogic
{
    private static final long serialVersionUID = 8189139931299534798L;

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndFinalStateLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndFinalState#isContainedInFrontEndUseCase()
     */
    @Override
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getStateMachine() instanceof FrontEndActivityGraph;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogicImpl#handleGetName()
     */
    @Override
    public String handleGetName()
    {
        String name = super.handleGetName();
        if (name == null)
        {
            final FrontEndUseCase useCase = this.getTargetUseCase();
            if (useCase != null)
            {
                name = useCase.getName();
            }
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndFinalState#getTargetUseCase()
     */
    @Override
    protected Object handleGetTargetUseCase()
    {
        Object targetUseCase = null;

        // first check if there is a hyperlink from this final state to a
        // use-case this works at least in MagicDraw
        final Object taggedValue = this.findTaggedValue(UMLProfile.TAGGEDVALUE_MODEL_HYPERLINK);
        if (taggedValue != null)
        {
            if (taggedValue instanceof FrontEndActivityGraph)
            {
                targetUseCase = ((FrontEndActivityGraph)taggedValue).getUseCase();
            }
            else if (taggedValue instanceof FrontEndUseCase)
            {
                targetUseCase = taggedValue;
            }
        }

        // maybe the name points to a use-case ?
        if (targetUseCase == null)
        {
            final String name = super.handleGetName();
            if (StringUtils.isNotBlank(name))
            {
                final UseCaseFacade useCase = this.getModel().findUseCaseByName(name);
                if (useCase instanceof FrontEndUseCase)
                {
                    targetUseCase = useCase;
                }
            }
        }
        return targetUseCase;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndFinalState#getInterUseCaseParameters()
     */
    @Override
    protected List<FrontEndParameter> handleGetInterUseCaseParameters()
    {
        // we don't want to list parameters with the same name so we use a hash map
        final Map<String, FrontEndParameter> parameterMap = new LinkedHashMap<String, FrontEndParameter>();

        final Collection<TransitionFacade> transitions = this.getIncomings();
        for (final Iterator<TransitionFacade> transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
        {
            final FrontEndForward forward = (FrontEndForward)transitionIterator.next();
            final List<FrontEndParameter> forwardParameters = forward.getForwardParameters();
            for (int i = 0; i < forwardParameters.size(); i++)
            {
                final FrontEndParameter parameter = forwardParameters.get(i);
                parameterMap.put(
                    parameter.getName(),
                    parameter);
            }
        }

        return new ArrayList<FrontEndParameter>(parameterMap.values());
    }

    @Override
    protected String handleGetPath() {
        String fullPath = null;

        FrontEndUseCase useCase = this.getTargetUseCase();
        if (useCase == null)
        {
            // - perhaps this final state links outside of the UML model
            final Object taggedValue = this.findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK);
            if (taggedValue == null)
            {
                String name = getName();
                if (name != null && (name.startsWith("/") || name.startsWith("http://") || name.startsWith("file:")))
                {
                    fullPath = name;
                }
            }
            else
            {
                fullPath = String.valueOf(taggedValue);
            }
            
            if(fullPath == null && getName() != null)
            {
                //fullPath = ((ManageableEntity)getTargetElement()).getActionFullPath();
            }
            
        }
        else if (useCase instanceof FrontEndUseCase)
        {
            fullPath = useCase.getPath();
        }

        return fullPath;
    }

    @Override
    protected String handleGetTargetControllerFullyQualifiedName() {
        String result=null;
        
        if(getTargetElement() instanceof FrontEndUseCase)
        {
            result=((FrontEndUseCase)getTargetElement()).getController().getFullyQualifiedName();
        }
        else if(getTargetElement() instanceof FrontEndManageableEntity)
        {
            result=((FrontEndManageableEntity)getTargetElement()).getControllerType();
        }
        
        return result;
    }

    @Override
    protected String handleGetTargetControllerBeanName() {
        String result=null;
        
        if(getTargetElement() instanceof FrontEndUseCase)
        {
            result=((FrontEndController)((FrontEndUseCase)getTargetElement()).getController()).getName();
        }
        else if(getTargetElement() instanceof FrontEndManageableEntity)
        {
            result=((FrontEndManageableEntity)getTargetElement()).getControllerBeanName();
        }
        
        return result;
    }

    private ManageableEntity findManageableByName(String name)
    {
        for(ClassifierFacade clazz: getModel().getAllClasses())
        {
            if(clazz instanceof ManageableEntity && 
               (clazz.getName().equals(name) || clazz.getFullyQualifiedName().equals(name)))
            {
                return (ManageableEntity)clazz;
            }
        }
        return null;
    }

    @Override
    protected ModelElementFacade handleGetTargetElement() {
        ModelElementFacade targetElement=getTargetUseCase();
        
        if(targetElement == null)
        {
            String nameParts[] = getName().split(" ");
            if(nameParts.length >= 2 && nameParts[0].equalsIgnoreCase("Manage"))
            {
                ManageableEntity manageable=findManageableByName(nameParts[1]);
                if(manageable != null)
                {
                    return targetElement=manageable;
                }
            }
        }
        
        return targetElement;
    }

    @Override
    protected FrontEndController handleGetTargetController() {
        FrontEndController controller = null;
        
        if(getTargetElement() instanceof FrontEndUseCase)
        {
            controller = ((FrontEndUseCase)getTargetElement()).getController();
        }
        else if(getTargetElement() instanceof FrontEndManageableEntity)
        {
            //controller = ((FrontEndManageableEntity)getTargetElement()).get;
        }

        return controller;
    }
}
