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
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getFullPath()()
     */
    public java.lang.String handleGetFullPath()
    {
        return '/' + getFullyQualifiedName().replace('.', '/');
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

    protected Object handleGetUseCase()
    {
        final Collection useCases = getModel().getAllUseCases();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            StrutsUseCase strutsUseCase = (StrutsUseCase) iterator.next();
            if (this.equals(strutsUseCase.getController()))
                return strutsUseCase;
        }
        return null;
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
