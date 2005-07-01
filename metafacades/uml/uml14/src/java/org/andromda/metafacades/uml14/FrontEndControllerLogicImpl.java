package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndController.
 *
 * @see org.andromda.metafacades.uml.FrontEndController
 */
public class FrontEndControllerLogicImpl
    extends FrontEndControllerLogic
{
    public FrontEndControllerLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndController#getFullPath()
     */
    protected java.lang.String handleGetFullPath()
    {
        return '/' + getPackageName().replace('.', '/') + '/' + getName();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndController#getServiceReferences()
     */
    protected java.util.List handleGetServiceReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
            {
                public boolean evaluate(Object object)
                {
                    return ((DependencyFacade)object).getTargetElement() instanceof Service;
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndController#getUseCase()
     */
    protected java.lang.Object handleGetUseCase()
    {
        Object useCase = null;

        final StateMachineFacade graphContext = this.getStateMachineContext();
        if (graphContext instanceof FrontEndActivityGraph)
        {
            useCase = ((ActivityGraphFacade)graphContext).getUseCase();
        }
        else
        {
            final Object useCaseTaggedValue = findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_CONTROLLER_USECASE);
            if (useCaseTaggedValue != null)
            {
                final String tag = useCaseTaggedValue.toString();

                // return the first use-case with this name
                useCase = getModel().findUseCaseWithNameAndStereotype(tag, UMLProfile.STEREOTYPE_FRONT_END_USECASE);
            }
        }
        return useCase;
    }
}