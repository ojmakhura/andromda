package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.collections.Predicate;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.ModelElement;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Metaclass facade implementation.
 */
public class ModelFacadeLogicImpl
        extends ModelFacadeLogic
{
    // ---------------- constructor -------------------------------

    public ModelFacadeLogicImpl(org.omg.uml.UmlPackage metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getRootPackage()
     */
    protected Object handleGetRootPackage()
    {
        Collection rootPackages = metaObject.getModelManagement().getModel().refAllOfType();
        return rootPackages.iterator().next();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActors()
     */
    protected Collection handleGetAllActors()
    {
        return metaObject.getUseCases().getActor().refAllOfType();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacadeLogic#getAllActionStates()
     */
    protected Collection handleGetAllActionStates()
    {
        return metaObject.getActivityGraphs().getActionState().refAllOfType();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllUseCases()
     */
    protected Collection handleGetAllUseCases()
    {
        return metaObject.getUseCases().getUseCase().refAllOfType();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllClasses()
     */
    protected Collection handleGetAllClasses()
    {
        return metaObject.getCore().getUmlClass().refAllOfType();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacadeLogic#findUseCaseWithTaggedValueOrHyperlink(java.lang.String, java.lang.String)
     */
    protected UseCaseFacade handleFindUseCaseWithTaggedValueOrHyperlink(String tag, String value)
    {
        return (UseCaseFacade)shieldedElement(UML14MetafacadeUtils.findUseCaseWithTaggedValueOrHyperlink(tag, value));
    }
    
    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findClassWithTaggedValueOrHyperlink(java.lang.String, java.lang.String)
     */
    protected ClassifierFacade handleFindClassWithTaggedValueOrHyperlink(String tag, String value)
    {
        return (ClassifierFacade)shieldedElement(UML14MetafacadeUtils.findClassWithTaggedValueOrHyperlink(tag, value));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findActivityGraphByName(java.lang.String)
     */
    protected ActivityGraphFacade handleFindActivityGraphByName(String name)
    {
        return (ActivityGraphFacade)shieldedElement(UML14MetafacadeUtils.findFirstActivityGraphWithName(name));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findActivityGraphByNameAndStereotype(java.lang.String, java.lang.String)
     */
    protected ActivityGraphFacade handleFindActivityGraphByNameAndStereotype(String name, String stereotypeName)
    {
        return (ActivityGraphFacade)shieldedElement(UML14MetafacadeUtils.findFirstActivityGraphWithNameAndStereotype(
                name, stereotypeName));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseByName(java.lang.String)
     */
    protected UseCaseFacade handleFindUseCaseByName(String name)
    {
        return (UseCaseFacade)shieldedElement(UML14MetafacadeUtils.findFirstUseCaseWithName(name));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseWithNameAndStereotype(java.lang.String, java.lang.String)
     */
    protected UseCaseFacade handleFindUseCaseWithNameAndStereotype(String name, String stereotypeName)
    {
        return (UseCaseFacade)shieldedElement(UML14MetafacadeUtils.findFirstUseCaseWithNameAndStereotype(name,
                stereotypeName));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findFinalStatesWithNameOrHyperlink(org.andromda.metafacades.uml.UseCaseFacade)
     */
    protected Collection handleFindFinalStatesWithNameOrHyperlink(UseCaseFacade useCase)
    {
        UseCase useCaseMetaClass = UML14MetafacadeUtils.getMetaClass(useCase);
        return shieldedElements(UML14MetafacadeUtils.findFinalStatesWithNameOrHyperlink(useCaseMetaClass));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActionStatesWithStereotype(org.andromda.metafacades.uml.ActivityGraphFacade, java.lang.String)
     */
    protected Collection handleGetAllActionStatesWithStereotype(ActivityGraphFacade activityGraph,
                                                                String stereotypeName)
    {
        ActivityGraph activityGraphMetaClass = UML14MetafacadeUtils.getMetaClass(activityGraph);

        CompositeState compositeState = (CompositeState)activityGraphMetaClass.getTop();
        return filter(compositeState.getSubvertex(), new ActionStateWithStereotypeFilter(stereotypeName));
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
            return (object instanceof ActionState) && UML14MetafacadeUtils.isStereotypePresent((ModelElement)object,
                    stereotypeName);
        }
    }

    private final Collection filter(Collection collection, Predicate collectionFilter)
    {
        final Set filteredCollection = new LinkedHashSet();
        for (final Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (collectionFilter.evaluate(object))
            {
                filteredCollection.add(object);
            }
        }
        return filteredCollection;
    }

}
