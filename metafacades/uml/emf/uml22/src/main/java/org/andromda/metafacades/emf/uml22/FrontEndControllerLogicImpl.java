package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.Service;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.UseCase;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndController.
 *
 * @see org.andromda.metafacades.uml.FrontEndController
 */
public class FrontEndControllerLogicImpl
    extends FrontEndControllerLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndControllerLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndController#getServiceReferences()
     */
    @Override
    protected List<DependencyFacade> handleGetServiceReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
            {
                @Override
                public boolean evaluate(final Object object)
                {
                    return ((DependencyFacade)object).getTargetElement() instanceof Service;
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndController#getUseCase()
     */
    @Override
    protected Element handleGetUseCase()
    {
        Element owner = (Classifier)this.metaObject;
        while (!(owner == null || owner instanceof UseCase))
        {
            owner = owner.getOwner();
        }
        return owner;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndController#getDeferringActions()
     */
    @Override
    protected List<FrontEndAction> handleGetDeferringActions()
    {
        final Collection<FrontEndAction> deferringActions = new LinkedHashSet<FrontEndAction>();

        final Collection<OperationFacade> operations = this.getOperations();
        for (final Iterator<OperationFacade> operationIterator = operations.iterator(); operationIterator.hasNext();)
        {
            final FrontEndControllerOperation operation = (FrontEndControllerOperation)operationIterator.next();
            deferringActions.addAll(operation.getDeferringActions());
        }
        return new ArrayList<FrontEndAction>(deferringActions);
    }

    // TODO: We may want to override getPackageName here, since in uml2
    // statemachine and usecase are "packages".
    // We may return the getUseCase package name
    // For now, in ModelElement, we are handling this case.
}
