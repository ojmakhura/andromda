package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.collections.Predicate;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.UmlPackage;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class ModelFacadeLogicImpl
    extends ModelFacadeLogic
{
    // ---------------- constructor -------------------------------
    /**
     * @param metaObject
     * @param context
     */
    public ModelFacadeLogicImpl(
        UmlPackage metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getRootPackage()
     */
    @Override
    protected Object handleGetRootPackage()
    {
        Collection rootPackages = metaObject.getModelManagement().getModel().refAllOfType();
        return rootPackages.iterator().next();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActors()
     */
    @Override
    protected Collection handleGetAllActors()
    {
        return metaObject.getUseCases().getActor().refAllOfType();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActionStates()
     */
    @Override
    protected Collection handleGetAllActionStates()
    {
        return metaObject.getActivityGraphs().getActionState().refAllOfType();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllUseCases()
     */
    @Override
    protected Collection handleGetAllUseCases()
    {
        return metaObject.getUseCases().getUseCase().refAllOfType();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllClasses()
     */
    @Override
    protected Collection handleGetAllClasses()
    {
        return metaObject.getCore().getUmlClass().refAllOfType();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllObjectFlowStates()
     */
    @Override
    protected Collection handleGetAllObjectFlowStates()
    {
        return metaObject.getActivityGraphs().getObjectFlowState().refAllOfType();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseWithTaggedValueOrHyperlink(String, String)
     */
    @Override
    protected UseCaseFacade handleFindUseCaseWithTaggedValueOrHyperlink(
        String tag,
        String value)
    {
        return (UseCaseFacade)shieldedElement(UML14MetafacadeUtils.findUseCaseWithTaggedValueOrHyperlink(tag, value));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findClassWithTaggedValueOrHyperlink(String, String)
     */
    @Override
    protected ClassifierFacade handleFindClassWithTaggedValueOrHyperlink(
        String tag,
        String value)
    {
        return (ClassifierFacade)shieldedElement(UML14MetafacadeUtils.findClassWithTaggedValueOrHyperlink(tag, value));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findActivityGraphByName(String)
     */
    @Override
    protected ActivityGraphFacade handleFindActivityGraphByName(String name)
    {
        return (ActivityGraphFacade)shieldedElement(UML14MetafacadeUtils.findFirstActivityGraphWithName(name));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findActivityGraphByNameAndStereotype(String, String)
     */
    @Override
    protected ActivityGraphFacade handleFindActivityGraphByNameAndStereotype(
        String name,
        String stereotypeName)
    {
        return (ActivityGraphFacade)shieldedElement(
            UML14MetafacadeUtils.findFirstActivityGraphWithNameAndStereotype(name, stereotypeName));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseByName(String)
     */
    @Override
    protected UseCaseFacade handleFindUseCaseByName(String name)
    {
        return (UseCaseFacade)shieldedElement(UML14MetafacadeUtils.findFirstUseCaseWithName(name));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseWithNameAndStereotype(String, String)
     */
    @Override
    protected UseCaseFacade handleFindUseCaseWithNameAndStereotype(
        String name,
        String stereotypeName)
    {
        return (UseCaseFacade)shieldedElement(
            UML14MetafacadeUtils.findFirstUseCaseWithNameAndStereotype(name, stereotypeName));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findFinalStatesWithNameOrHyperlink(org.andromda.metafacades.uml.UseCaseFacade)
     */
    @Override
    protected Collection handleFindFinalStatesWithNameOrHyperlink(UseCaseFacade useCase)
    {
        UseCase useCaseMetaClass = UML14MetafacadeUtils.getMetaClass(useCase);
        return shieldedElements(UML14MetafacadeUtils.findFinalStatesWithNameOrHyperlink(useCaseMetaClass));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActionStatesWithStereotype(org.andromda.metafacades.uml.ActivityGraphFacade, String)
     */
    @Override
    protected Collection handleGetAllActionStatesWithStereotype(
        ActivityGraphFacade activityGraph,
        String stereotypeName)
    {
        ActivityGraph activityGraphMetaClass = UML14MetafacadeUtils.getMetaClass(activityGraph);

        CompositeState compositeState = (CompositeState)activityGraphMetaClass.getTop();
        return filter(
            compositeState.getSubvertex(),
            new ActionStateWithStereotypeFilter(stereotypeName));
    }

    protected Collection handleGetAllTransitions()
    {
        return this.metaObject.getStateMachines().getTransition().refAllOfType();
    }

    private final static class ActionStateWithStereotypeFilter
        implements Predicate
    {
        private String stereotypeName = null;

        public ActionStateWithStereotypeFilter(final String stereotypeName)
        {
            this.stereotypeName = stereotypeName;
        }

        public boolean evaluate(final Object object)
        {
            return (object instanceof ActionState) &&
            UML14MetafacadeUtils.isStereotypePresent((ModelElement)object, stereotypeName);
        }
    }

    private Collection filter(
        Collection collection,
        Predicate collectionFilter)
    {
        final Set filteredCollection = new LinkedHashSet();
        for (Object object : collection)
        {
            if (collectionFilter.evaluate(object))
            {
                filteredCollection.add(object);
            }
        }
        return filteredCollection;
    }
}