package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndController.
 *
 * @see org.andromda.metafacades.uml.FrontEndController
 * @author Bob Fields
 */
public class FrontEndControllerLogicImpl
    extends FrontEndControllerLogic
{
    private static final long serialVersionUID = 8320339804450106876L;

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndControllerLogicImpl(
        Object metaObject,
        String context)
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
            private static final long serialVersionUID = 2214453181869436835L;

            public boolean evaluate(Object object)
                {
                    return ((DependencyFacade)object).getTargetElement() instanceof Service;
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndController#getUseCase()
     */
    @Override
    protected UseCaseFacade handleGetUseCase()
    {
        UseCaseFacade useCase = null;
        final StateMachineFacade graphContext = this.getStateMachineContext();
        if (graphContext instanceof FrontEndActivityGraph)
        {
            useCase = ((ActivityGraphFacade)graphContext).getUseCase();
        }
        else
        {
            final Object useCaseTaggedValue = findTaggedValue(
                    UMLProfile.TAGGEDVALUE_PRESENTATION_CONTROLLER_USECASE);
            if (useCaseTaggedValue != null)
            {
                final String tag = useCaseTaggedValue.toString();

                // - return the first use-case with this name
                useCase = this.getModel().findUseCaseWithNameAndStereotype(
                        tag, UMLProfile.STEREOTYPE_FRONT_END_USECASE);
            }
        }
        return useCase;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndController#getDeferringActions()
     */
    @Override
    protected List<FrontEndAction> handleGetDeferringActions()
    {
        final Collection<FrontEndAction> deferringActions = new LinkedHashSet<FrontEndAction>();

        final Collection<OperationFacade> operations = getOperations();
        for (final Iterator<OperationFacade> operationIterator = operations.iterator(); operationIterator.hasNext();)
        {
            final FrontEndControllerOperation operation = (FrontEndControllerOperation)operationIterator.next();
            deferringActions.addAll(operation.getDeferringActions());
        }
        return new ArrayList<FrontEndAction>(deferringActions);
    }

    @Override
    protected String handleGetImplementationName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetFullyQualifiedImplementationName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetFullyQualifiedImplementationPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetBeanName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetControllerSerialVersionUID() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection handleGetAllServices() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection handleGetAllowedOperations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection handleGetSessionObjectReferences() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection handleGetServicesPackagesReferences() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetRestPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetFilename() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetTargetUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Collection handleGetAllowedRoles() {
        // TODO Auto-generated method stub
        return null;
    }
}
