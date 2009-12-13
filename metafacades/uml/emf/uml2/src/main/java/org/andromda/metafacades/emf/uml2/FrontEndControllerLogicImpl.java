package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.Service;
import org.eclipse.uml2.Classifier;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.UseCase;


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
     * @return getSourceDependencies() instanceof Service
     * @see org.andromda.metafacades.uml.FrontEndController#getServiceReferences()
     */
    protected List handleGetServiceReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
            {
                public boolean evaluate(final Object object)
                {
                    return ((DependencyFacade)object).getTargetElement() instanceof Service;
                }
            };
    }

    /**
     * @return metaObject instanceof UseCase .getOwner()
     * @see org.andromda.metafacades.uml.FrontEndController#getUseCase()
     */
    protected Object handleGetUseCase()
    {
        Element owner = (Classifier)this.metaObject;
        while (!(owner == null || owner instanceof UseCase))
        {
            owner = owner.getOwner();
        }
        return owner;
    }

    /**
     * @return getOperations().getDeferringActions()
     * @see org.andromda.metafacades.uml.FrontEndController#getDeferringActions()
     */
    protected List handleGetDeferringActions()
    {
        final Collection deferringActions = new LinkedHashSet();

        final Collection operations = this.getOperations();
        for (final Iterator operationIterator = operations.iterator(); operationIterator.hasNext();)
        {
            final FrontEndControllerOperation operation = (FrontEndControllerOperation)operationIterator.next();
            deferringActions.addAll(operation.getDeferringActions());
        }
        return new ArrayList(deferringActions);
    }

    // TODO: We may want to override getPackageName here, since in uml2
    // statemachine and usecase are "packages".
    // We may return the getUseCase package name
    // For now, in ModelElement, we are handling this case.
}