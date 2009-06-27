package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController
 */
public class StrutsControllerLogicImpl
    extends StrutsControllerLogic
{
    public StrutsControllerLogicImpl(
        java.lang.Object metaObject,
        java.lang.String context)
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
    public List getDeferringActions()
    {
        final Collection deferringActions = new LinkedHashSet();

        final Collection operations = getOperations();
        for (final Iterator operationIterator = operations.iterator(); operationIterator.hasNext();)
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
        for (final Iterator iterator = dependencies.iterator(); iterator.hasNext();)
        {
            final DependencyFacade dependency = (DependencyFacade)iterator.next();
            final ModelElementFacade modelElement = dependency.getTargetElement();
            if (modelElement instanceof StrutsSessionObject)
                objectsList.add(modelElement);
        }

        return objectsList;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getAllArguments()
     */
    protected List handleGetAllArguments()
    {
        final List allArguments = new ArrayList();
        final Collection operations = this.getOperations();

        for (final Iterator operationIterator = operations.iterator(); operationIterator.hasNext();)
        {
            final OperationFacade operationFacade = (OperationFacade)operationIterator.next();
            final Collection arguments = operationFacade.getArguments();
            for (final Iterator argumentIterator = arguments.iterator(); argumentIterator.hasNext();)
            {
                final ParameterFacade parameterFacade = (ParameterFacade)argumentIterator.next();
                allArguments.add(parameterFacade);
            }
        }

        return allArguments;
    }
}
