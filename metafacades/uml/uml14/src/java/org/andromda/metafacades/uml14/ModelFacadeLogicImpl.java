package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.*;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.statemachines.SignalEvent;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Parameter;
import org.apache.commons.collections.Predicate;

import java.util.Collection;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Iterator;


/**
 * Metaclass facade implementation.
 */
public class ModelFacadeLogicImpl
        extends ModelFacadeLogic
        implements org.andromda.metafacades.uml.ModelFacade
{
    // ---------------- constructor -------------------------------

    public ModelFacadeLogicImpl(org.omg.uml.UmlPackage metaObject, String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ModelDecorator ...

    // ------------- relations ------------------

    protected Object handleGetRootPackage()
    {
        Collection rootPackages =
                metaObject.getModelManagement().getModel().refAllOfType();
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



    protected UseCaseFacade handleGetUseCase(ActivityGraphFacade activityGraph)
    {
        UseCase useCase = null;
        if (activityGraph != null)
        {
            ModelElement graphElement = UMLMetafacadeUtils.getMetaClass(activityGraph);
            useCase = UMLMetafacadeUtils.getUseCase((ActivityGraph)graphElement);
        }
        return (UseCaseFacade)shieldedElement(useCase);
    }

    protected ActivityGraphFacade handleGetActivityGraph(UseCaseFacade useCase)
    {
        ActivityGraph activityGraph = null;
        if (activityGraph != null)
        {
            ModelElement element = UMLMetafacadeUtils.getMetaClass(useCase);
            activityGraph = UMLMetafacadeUtils.getFirstActivityGraph((UseCase)element);
        }
        return (ActivityGraphFacade)shieldedElement(activityGraph);
    }

    protected UseCaseFacade handleFindUseCaseWithTaggedValueOrHyperlink(String tag, String value)
    {
        return (UseCaseFacade)shieldedElement(UMLMetafacadeUtils.findUseCaseWithTaggedValueOrHyperlink(tag, value));
    }

    protected ClassifierFacade handleFindClassWithTaggedValueOrHyperlink(String tag, String value)
    {
        return (ClassifierFacade)shieldedElement(UMLMetafacadeUtils.findClassWithTaggedValueOrHyperlink(tag, value));
    }

    protected ActivityGraphFacade handleGetActivityGraphContext(ModelElementFacade modelElement)
    {
        ActivityGraph context = null;
        if (modelElement != null)
        {
            ModelElement element = UMLMetafacadeUtils.getMetaClass(modelElement);
            context = UMLMetafacadeUtils.getActivityGraphContext(element);
        }
        return (ActivityGraphFacade)shieldedElement(context);
    }

    protected ActivityGraphFacade handleFindActivityGraphByName(String name)
    {
        return (ActivityGraphFacade)shieldedElement(UMLMetafacadeUtils.findFirstActivityGraphWithName(name));
    }

    protected ActivityGraphFacade handleFindActivityGraphByNameAndStereotype(String name, String stereotypeName)
    {
        return (ActivityGraphFacade)shieldedElement(UMLMetafacadeUtils.findFirstActivityGraphWithNameAndStereotype(name, stereotypeName));
    }

    protected UseCaseFacade handleFindUseCaseByName(String name)
    {
        return (UseCaseFacade)shieldedElement(UMLMetafacadeUtils.findFirstUseCaseWithName(name));
    }

    protected UseCaseFacade handleFindUseCaseWithNameAndStereotype(String name, String stereotypeName)
    {
        return (UseCaseFacade)shieldedElement(UMLMetafacadeUtils.findFirstUseCaseWithNameAndStereotype(name, stereotypeName));
    }

    protected TransitionFacade handleGetParameterTransition(ParameterFacade parameter)
    {
        Parameter parameterMetaClass = UMLMetafacadeUtils.getMetaClass(parameter);
        SignalEvent signalEvent = UMLMetafacadeUtils.getSignalEvent(parameterMetaClass);
        Transition transition = UMLMetafacadeUtils.getTransition(signalEvent);

        return (TransitionFacade)shieldedElement(transition);
    }

    protected TransitionFacade handleGetEventTransition(EventFacade event)
    {
        Event eventMetaClass = UMLMetafacadeUtils.getMetaClass(event);
        return (TransitionFacade)shieldedElement(UMLMetafacadeUtils.getTransition(eventMetaClass));
    }

    protected OperationFacade handleGetParameterOperation(ParameterFacade parameter)
    {
        Parameter parameterMetaClass = UMLMetafacadeUtils.getMetaClass(parameter);
        return (OperationFacade)shieldedElement(UMLMetafacadeUtils.getOperation(parameterMetaClass));
    }

    protected Collection handleFindFinalStatesWithNameOrHyperlink(UseCaseFacade useCase)
    {
        UseCase useCaseMetaClass = UMLMetafacadeUtils.getMetaClass(useCase);
        return shieldedElements(UMLMetafacadeUtils.findFinalStatesWithNameOrHyperlink(useCaseMetaClass));
    }

    protected Collection handleGetAllActionStatesWithStereotype(ActivityGraphFacade activityGraph, String stereotypeName)
    {
        ActivityGraph activityGraphMetaClass = UMLMetafacadeUtils.getMetaClass(activityGraph);

        CompositeState compositeState = (CompositeState) activityGraphMetaClass.getTop();
        return filter(compositeState.getSubvertex(), new ActionStateWithStereotypeFilter(stereotypeName));
    }

    private class ActionStateWithStereotypeFilter implements Predicate
    {
        private String stereotypeName = null;

        public ActionStateWithStereotypeFilter(String stereotypeName)
        {
            this.stereotypeName = stereotypeName;
        }

        public boolean evaluate(Object o)
        {
            return (o instanceof ActionState) && UMLMetafacadeUtils.isStereotypePresent((ModelElement)o,stereotypeName);
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
