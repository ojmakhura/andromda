package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.ServiceFacade;


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
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getFullPath()()
     */
    protected java.lang.String handleGetFullPath()
    {
        return '/' + getUseCase().getPackageName().replace('.', '/') + '/' + getName();
    }

    // ------------- relations ------------------

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

    protected Collection handleGetSessionObjects()
    {
        final Collection objectsList = new ArrayList();

        final Collection associationEnds = getAssociationEnds();
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEndFacade associationEnd = (AssociationEndFacade) iterator.next();
            ClassifierFacade classifier = associationEnd.getOtherEnd().getType();
            if (classifier instanceof StrutsSessionObject)
                objectsList.add(classifier);
        }

        return objectsList;
    }

    protected Collection handleGetServices()
    {
        final Collection servicesList = new ArrayList();
        final Collection dependencies = getDependencies();
        for (Iterator iterator = dependencies.iterator(); iterator.hasNext();)
        {
            DependencyFacade dependency = (DependencyFacade) iterator.next();
            ModelElementFacade target = dependency.getTargetElement();
            if (target instanceof ServiceFacade)
                servicesList.add(target);
        }
        return servicesList;
    }

    private Object useCase = null;
    private boolean useCaseFound = false;

    protected Object handleGetUseCase()
    {
        if (useCaseFound == false)
        {
            final Collection useCases = getModel().getAllUseCases();
            for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
            {
                StrutsUseCase strutsUseCase = (StrutsUseCase) iterator.next();
                if (this.equals(strutsUseCase.getController()))
                {
                    useCase = strutsUseCase;
                    useCaseFound = true;
                }
            }
        }
        return useCase;
    }

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
}
