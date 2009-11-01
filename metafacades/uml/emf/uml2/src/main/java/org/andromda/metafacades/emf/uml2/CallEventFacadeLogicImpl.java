package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.uml2.CallOperationAction;
import org.eclipse.uml2.Operation;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.CallEventFacade. UML1.4 Event are mapped to UML2
 * Activity (because UML2 Event doesn't contain parameter)
 * 
 * @see org.andromda.metafacades.uml.CallEventFacade
 */
public class CallEventFacadeLogicImpl extends CallEventFacadeLogic
{

    public CallEventFacadeLogicImpl(final org.eclipse.uml2.Activity metaObject,
            final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.CallEventFacade#getOperation()
     */
    protected Object handleGetOperation()
    {
        final List operations = this.getOperations();
        return operations.isEmpty() ? null : operations.iterator().next();
    }

    /**
     * @see org.andromda.metafacades.uml.CallEventFacade#getOperations()
     */
    public List handleGetOperations()
    {
        // We get every operation from each CallOperationAction instance.
        final List operations = new ArrayList();
        Collection nodes = this.metaObject.getNodes();
        for (final Iterator iterator = nodes.iterator(); iterator.hasNext();)
        {
            final Object nextNode = iterator.next();
            if (nextNode instanceof CallOperationAction)
            {
                final Operation operation = ((CallOperationAction)nextNode).getOperation();
                if (operation != null)
                {
                    operations.add(operation);
                }
            }
        }
        return operations;
    }

}