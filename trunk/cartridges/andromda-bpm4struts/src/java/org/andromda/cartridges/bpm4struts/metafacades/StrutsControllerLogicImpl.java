package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.metafacades.uml.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController
 */
public class StrutsControllerLogicImpl
        extends StrutsControllerLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsController
{
    // ---------------- constructor -------------------------------

    public StrutsControllerLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsController ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getFullPath()
     */
    protected java.lang.String handleGetFullPath()
    {
        return '/' + getUseCase().getPackageName().replace('.', '/') + '/' + getName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getDeferringActions()
     */
    protected Collection handleGetDeferringActions()
    {
        Collection deferringActions = new HashSet();

        Collection operations = getOperations();
        for (Iterator operationIterator = operations.iterator(); operationIterator.hasNext();)
        {
            StrutsControllerOperation operation = (StrutsControllerOperation) operationIterator.next();
            deferringActions.addAll(operation.getDeferringActions());
        }
        return deferringActions;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getSessionObjects()
     */
    protected Collection handleGetSessionObjects()
    {
        final Collection objectsList = new ArrayList();

        final Collection dependencies = getTargetDependencies();
        for (Iterator iterator = dependencies.iterator(); iterator.hasNext();)
        {
            DependencyFacade dependency = (DependencyFacade) iterator.next();
            ModelElementFacade modelElement = dependency.getTargetElement();
            if (modelElement instanceof StrutsSessionObject)
                objectsList.add(modelElement);
        }

        return objectsList;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getServiceReferences()
     */
    protected Collection handleGetServiceReferences()
    {
        return new FilteredCollection(this.getTargetDependencies())
        {
            public boolean evaluate(Object object)
            {
                ModelElementFacade targetElement = ((DependencyFacade)object)
                    .getTargetElement();
                return targetElement != null
                    && ServiceFacade.class.isAssignableFrom(targetElement
                        .getClass());
            }
        };
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getUseCase()
     */
    protected Object handleGetUseCase()
    {
        StrutsUseCase useCase = null;

        final Collection useCases = getModel().getAllUseCases();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext() && useCase==null;)
        {
            Object useCaseObject = iterator.next();
            if (useCaseObject instanceof StrutsUseCase)
            {
                StrutsUseCase strutsUseCase = (StrutsUseCase) useCaseObject;
                if (this.equals(strutsUseCase.getController()))
                {
                    useCase = strutsUseCase;
                }
            }
        }

        return useCase;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getAllArguments()
     */
    protected Collection handleGetAllArguments()
    {
        final Collection allArguments = new ArrayList();
        final Collection operations = this.getOperations();

        for (Iterator operationIterator = operations.iterator(); operationIterator.hasNext();)
        {
            final OperationFacade operationFacade = (OperationFacade) operationIterator.next();
            final Collection arguments = operationFacade.getArguments();
            for (Iterator argumentIterator = arguments.iterator(); argumentIterator.hasNext();)
            {
                ParameterFacade parameterFacade = (ParameterFacade) argumentIterator.next();
                allArguments.add(parameterFacade);
            }
        }

        return allArguments;
    }

/* @todo: delete or use
    protected boolean handleIsUniqueOperationNames()
    {
        boolean unique = true;

        Collection operations = getOperations();
        HashSet names = new HashSet();

        for (Iterator operationIterator = operations.iterator(); operationIterator.hasNext() && unique;)
        {
            StrutsControllerOperation operation = (StrutsControllerOperation) operationIterator.next();
            String name = operation.getName();

            if (names.contains(name))
            {
                unique = false;
            }
            else
            {
                names.add(name);
            }
        }
        return unique;
    }
*/
}
