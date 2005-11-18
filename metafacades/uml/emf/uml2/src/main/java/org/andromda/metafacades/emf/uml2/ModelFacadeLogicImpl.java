package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ModelFacade.
 *
 * @see org.andromda.metafacades.uml.ModelFacade
 */
public class ModelFacadeLogicImpl
    extends ModelFacadeLogic
{
    public ModelFacadeLogicImpl(
        org.eclipse.uml2.Package metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseWithTaggedValueOrHyperlink(java.lang.String, java.lang.String)
     */
    protected org.andromda.metafacades.uml.UseCaseFacade handleFindUseCaseWithTaggedValueOrHyperlink(
        java.lang.String tag,
        java.lang.String value)
    {
        return null;
    }

    /**
     *
     * @see org.andromda.metafacades.uml.ModelFacade#findClassWithTaggedValueOrHyperlink(java.lang.String, java.lang.String)
     */
    protected org.andromda.metafacades.uml.ClassifierFacade handleFindClassWithTaggedValueOrHyperlink(
        java.lang.String tag,
        java.lang.String value)
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findActivityGraphByName(java.lang.String)
     */
    protected org.andromda.metafacades.uml.ActivityGraphFacade handleFindActivityGraphByName(java.lang.String name)
    {
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findActivityGraphByNameAndStereotype(java.lang.String, java.lang.String)
     */
    protected org.andromda.metafacades.uml.ActivityGraphFacade handleFindActivityGraphByNameAndStereotype(
        java.lang.String name,
        java.lang.String stereotypeName)
    {
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseByName(java.lang.String)
     */
    protected org.andromda.metafacades.uml.UseCaseFacade handleFindUseCaseByName(java.lang.String name)
    {
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseWithNameAndStereotype(java.lang.String, java.lang.String)
     */
    protected org.andromda.metafacades.uml.UseCaseFacade handleFindUseCaseWithNameAndStereotype(
        java.lang.String name,
        java.lang.String stereotypeName)
    {
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findFinalStatesWithNameOrHyperlink(org.andromda.metafacades.uml.UseCaseFacade)
     */
    protected java.util.Collection handleFindFinalStatesWithNameOrHyperlink(
        org.andromda.metafacades.uml.UseCaseFacade useCase)
    {
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActionStatesWithStereotype(org.andromda.metafacades.uml.ActivityGraphFacade, java.lang.String)
     */
    protected java.util.Collection handleGetAllActionStatesWithStereotype(
        org.andromda.metafacades.uml.ActivityGraphFacade activityGraph,
        java.lang.String stereotypeName)
    {
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getRootPackage()
     */
    protected java.lang.Object handleGetRootPackage()
    {
        return metaObject.getModel();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActors()
     */
    protected java.util.Collection handleGetAllActors()
    {
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllUseCases()
     */
    protected java.util.Collection handleGetAllUseCases()
    {
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActionStates()
     */
    protected java.util.Collection handleGetAllActionStates()
    {
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllClasses()
     */
    protected java.util.Collection handleGetAllClasses()
    {
        ArrayList classes = new ArrayList();

        for (Iterator iterator = metaObject.getOwnedMembers().iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof org.eclipse.uml2.Class)
            {
                classes.add(object);
            }
        }
        return classes;
    }
}