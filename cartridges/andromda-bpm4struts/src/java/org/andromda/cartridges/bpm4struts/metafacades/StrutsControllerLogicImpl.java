package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.StateMachineFacade;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController
 */
public class StrutsControllerLogicImpl
        extends StrutsControllerLogic
{
    public StrutsControllerLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getFullPath()
     */
    protected java.lang.String handleGetFullPath()
    {
        return '/' + getPackageName().replace('.', '/') + '/' + getName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getDeferringActions()
     */
    protected List handleGetDeferringActions()
    {
        final Collection deferringActions = new HashSet();

        final Collection operations = getOperations();
        for (Iterator operationIterator = operations.iterator(); operationIterator.hasNext();)
        {
            final StrutsControllerOperation operation = (StrutsControllerOperation)operationIterator.next();
            deferringActions.addAll(operation.getDeferringActions());
        }
        return new ArrayList(deferringActions);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getSessionObjects()
     */
    protected List handleGetSessionObjects()
    {
        final List objectsList = new ArrayList();

        final Collection dependencies = this.getSourceDependencies();
        for (Iterator iterator = dependencies.iterator(); iterator.hasNext();)
        {
            final DependencyFacade dependency = (DependencyFacade)iterator.next();
            final ModelElementFacade modelElement = dependency.getTargetElement();
            if (modelElement instanceof StrutsSessionObject)
                objectsList.add(modelElement);
        }

        return objectsList;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getServiceReferences()
     */
    protected List handleGetServiceReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
        {
            public boolean evaluate(Object object)
            {
                final ModelElementFacade targetElement = ((DependencyFacade)object).getTargetElement();
                return targetElement != null && Service.class.isAssignableFrom(targetElement.getClass());
            }
        };
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getUseCase()
     */
    protected Object handleGetUseCase()
    {
        UseCaseFacade useCase = null;

        final StateMachineFacade graphContext = getStateMachineContext();
        if (graphContext instanceof ActivityGraphFacade)
        {
            useCase = ((ActivityGraphFacade)graphContext).getUseCase();
            if (useCase != null && !StrutsUseCase.class.isAssignableFrom(useCase.getClass()))
            {
                useCase = null;
            }
        }
        else
        {
            final Object useCaseTaggedValue = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_CONTROLLER_USE_CASE);
            if (useCaseTaggedValue != null)
            {
                final String tag = useCaseTaggedValue.toString();
                // return the first use-case with this name
                useCase = getModel().findUseCaseWithNameAndStereotype(tag, Bpm4StrutsProfile.STEREOTYPE_USECASE);
            }
        }

        return useCase;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getAllArguments()
     */
    protected List handleGetAllArguments()
    {
        final List allArguments = new ArrayList();
        final Collection operations = this.getOperations();

        for (Iterator operationIterator = operations.iterator(); operationIterator.hasNext();)
        {
            final OperationFacade operationFacade = (OperationFacade)operationIterator.next();
            final Collection arguments = operationFacade.getArguments();
            for (Iterator argumentIterator = arguments.iterator(); argumentIterator.hasNext();)
            {
                ParameterFacade parameterFacade = (ParameterFacade)argumentIterator.next();
                allArguments.add(parameterFacade);
            }
        }

        return allArguments;
    }
}
