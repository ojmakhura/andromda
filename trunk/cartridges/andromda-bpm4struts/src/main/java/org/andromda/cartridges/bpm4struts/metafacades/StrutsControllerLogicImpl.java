package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FrontEndAction;
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
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public StrutsControllerLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @return '/' + getPackageName().replace('.', '/') + '/' + getName()
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getFullPath()
     */
    protected String handleGetFullPath()
    {
        return '/' + getPackageName().replace('.', '/') + '/' + getName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getDeferringActions()
     */
    public List<FrontEndAction> getDeferringActions()
    {
        final Collection<FrontEndAction> deferringActions = new LinkedHashSet<FrontEndAction>();

        final Collection<OperationFacade> operations = getOperations();
        for (final Iterator<OperationFacade> operationIterator = operations.iterator(); operationIterator.hasNext();)
        {
            final StrutsControllerOperation operation = (StrutsControllerOperation)operationIterator.next();
            deferringActions.addAll(operation.getDeferringActions());
        }
        return new ArrayList<FrontEndAction>(deferringActions);
    }

    /**
     * @return getSourceDependencies().getTargetElement() StrutsSessionObject
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getSessionObjects()
     */
    protected List<StrutsSessionObject> handleGetSessionObjects()
    {
        final List<StrutsSessionObject> objectsList = new ArrayList<StrutsSessionObject>();

        for (final DependencyFacade dependency : this.getSourceDependencies())
        {
            final ModelElementFacade modelElement = dependency.getTargetElement();
            if (modelElement instanceof StrutsSessionObject)
            {
                objectsList.add((StrutsSessionObject)modelElement);
            }
        }

        return objectsList;
    }

    /**
     * @return getOperations().getArguments()
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getAllArguments()
     */
    protected List<ParameterFacade> handleGetAllArguments()
    {
        final List<ParameterFacade> allArguments = new ArrayList<ParameterFacade>();
        for (final OperationFacade operationFacade : this.getOperations())
        {
            for (final ParameterFacade parameterFacade : operationFacade.getArguments())
            {
                allArguments.add(parameterFacade);
            }
        }

        return allArguments;
    }
}
