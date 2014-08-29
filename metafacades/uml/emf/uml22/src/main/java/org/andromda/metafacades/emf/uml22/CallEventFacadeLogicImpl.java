package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.andromda.metafacades.uml.OperationFacade;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Operation;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.CallEventFacade. UML1.4 Event is mapped to UML2
 * Activity (because UML2 Event doesn't contain parameters)
 *
 * @see org.andromda.metafacades.uml.CallEventFacade
 */
public class CallEventFacadeLogicImpl extends CallEventFacadeLogic
{
    private static final long serialVersionUID = 7223650138117667366L;

    /**
     * @param metaObject
     * @param context
     */
    public CallEventFacadeLogicImpl(final Activity metaObject,
            final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.CallEventFacade#getOperation()
     */
    @Override
    protected OperationFacade handleGetOperation()
    {
        final Collection<OperationFacade> operations = this.getOperations();
        return operations.isEmpty() ? null : operations.iterator().next();
    }

    /**
     * @see org.andromda.metafacades.uml.CallEventFacade#getOperations()
     */
    @Override
    public List<Operation> handleGetOperations()
    {
        // We get every operation from each CallOperationAction instance.
        final List<Operation> operations = new ArrayList<Operation>();
        final Collection<ActivityNode> nodes = this.metaObject.getNodes();
        /* Collection<ActivityNode> nodes = this.metaObject.getNodes();
        // UML2 v3 and later: What previously was in getNodes is now in getOwnedNodes, while getNodes returns null
        // This causes JSF cartridge to fail unless getOwnedNodes exists in UML2 metamodel.
        // Activity Node operation parameters will have the incorrect owner type.
        if (nodes==null || nodes.isEmpty())
        {
            nodes = this.metaObject.getOwnedNodes();
        }*/
        for (final ActivityNode nextNode : nodes)
        {
            if (nextNode instanceof CallOperationAction)
            {
                final Operation operation = ((CallOperationAction) nextNode).getOperation();
                if (operation != null)
                {
                    operations.add(operation);
                }
            }
        }
        return operations;
    }

}
