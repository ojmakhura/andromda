package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.collections.Predicate;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * Metaclass facade implementation.
 */
public class ModelFacadeLogicImpl
    extends ModelFacadeLogic
    implements org.andromda.metafacades.uml.ModelFacade
{
    // ---------------- constructor -------------------------------

    public ModelFacadeLogicImpl(
        org.omg.uml.UmlPackage metaObject,
        String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ModelDecorator ...

    // ------------- relations ------------------

    protected Object handleGetRootPackage()
    {
        Collection rootPackages = metaObject.getModelManagement().getModel()
            .refAllOfType();
        return rootPackages.iterator().next();
    }

    protected Collection handleGetAllActors()
    {
        return metaObject.getUseCases().getActor().refAllOfType();
    }

    protected Collection handleGetAllActionStates()
    {
        return metaObject.getActivityGraphs().getActionState().refAllOfType();
    }

    protected Collection handleGetAllUseCases()
    {
        return metaObject.getUseCases().getUseCase().refAllOfType();
    }

    // ------------------------------------------------------------

    protected UseCaseFacade handleFindUseCaseWithTaggedValueOrHyperlink(
        String tag,
        String value)
    {
        return (UseCaseFacade)shieldedElement(UMLMetafacadeUtils
            .findUseCaseWithTaggedValueOrHyperlink(tag, value));
    }

    protected ClassifierFacade handleFindClassWithTaggedValueOrHyperlink(
        String tag,
        String value)
    {
        return (ClassifierFacade)shieldedElement(UMLMetafacadeUtils
            .findClassWithTaggedValueOrHyperlink(tag, value));
    }

    protected ActivityGraphFacade handleFindActivityGraphByName(String name)
    {
        return (ActivityGraphFacade)shieldedElement(UMLMetafacadeUtils
            .findFirstActivityGraphWithName(name));
    }

    protected ActivityGraphFacade handleFindActivityGraphByNameAndStereotype(
        String name,
        String stereotypeName)
    {
        return (ActivityGraphFacade)shieldedElement(UMLMetafacadeUtils
            .findFirstActivityGraphWithNameAndStereotype(name, stereotypeName));
    }

    protected UseCaseFacade handleFindUseCaseByName(String name)
    {
        return (UseCaseFacade)shieldedElement(UMLMetafacadeUtils
            .findFirstUseCaseWithName(name));
    }

    protected UseCaseFacade handleFindUseCaseWithNameAndStereotype(
        String name,
        String stereotypeName)
    {
        return (UseCaseFacade)shieldedElement(UMLMetafacadeUtils
            .findFirstUseCaseWithNameAndStereotype(name, stereotypeName));
    }

    protected Collection handleFindFinalStatesWithNameOrHyperlink(
        UseCaseFacade useCase)
    {
        UseCase useCaseMetaClass = UMLMetafacadeUtils.getMetaClass(useCase);
        return shieldedElements(UMLMetafacadeUtils
            .findFinalStatesWithNameOrHyperlink(useCaseMetaClass));
    }

    protected Collection handleGetAllActionStatesWithStereotype(
        ActivityGraphFacade activityGraph,
        String stereotypeName)
    {
        ActivityGraph activityGraphMetaClass = UMLMetafacadeUtils
            .getMetaClass(activityGraph);

        CompositeState compositeState = (CompositeState)activityGraphMetaClass
            .getTop();
        return filter(
            compositeState.getSubvertex(),
            new ActionStateWithStereotypeFilter(stereotypeName));
    }

    private class ActionStateWithStereotypeFilter
        implements Predicate
    {
        private String stereotypeName = null;

        public ActionStateWithStereotypeFilter(
            String stereotypeName)
        {
            this.stereotypeName = stereotypeName;
        }

        public boolean evaluate(Object o)
        {
            return (o instanceof ActionState)
                && UMLMetafacadeUtils.isStereotypePresent(
                    (ModelElement)o,
                    stereotypeName);
        }
    }

    private Collection filter(Collection collection, Predicate collectionFilter)
    {
        final Set filteredCollection = new LinkedHashSet();
        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
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
