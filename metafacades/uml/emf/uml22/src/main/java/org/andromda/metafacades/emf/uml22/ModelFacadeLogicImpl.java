package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FinalStateFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.uml2.uml.Actor;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UseCase;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ModelFacade.
 *
 * @see org.andromda.metafacades.uml.ModelFacade
 */
public class ModelFacadeLogicImpl
    extends ModelFacadeLogic
{
    private static final long serialVersionUID = -7114972076481221838L;

    /**
     * @param metaObject
     * @param context
     */
    public ModelFacadeLogicImpl(
        final UMLResource metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger LOGGER = Logger.getLogger(ModelFacadeLogicImpl.class);

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseWithTaggedValueOrHyperlink(String,
     *      String)
     */
    @Override
    protected UseCaseFacade handleFindUseCaseWithTaggedValueOrHyperlink(
        final String tag,
        final String value)
    {
        UseCaseFacade useCaseWithTaggedValue = null;

        final Collection<UseCaseFacade> useCases = this.getAllUseCases();
        for (final Iterator<UseCaseFacade> useCaseIterator = useCases.iterator(); useCaseIterator.hasNext()
            && useCaseWithTaggedValue == null;)
        {
            final UseCaseFacade useCase = useCaseIterator.next();
            // UML2: taggedValue must belong to a defined stereotype
            if (useCase.findTaggedValue(tag, true) == null)
            {
                final Object taggedValue = useCase.findTaggedValue(UMLProfile.TAGGEDVALUE_MODEL_HYPERLINK);
                if (taggedValue != null)
                {
                    useCaseWithTaggedValue = useCase;
                }
            }
            else
            {
                useCaseWithTaggedValue = useCase;
            }
        }

        //throw new UnsupportedOperationException();
        return useCaseWithTaggedValue;
    }

    /**
     *
     * @see org.andromda.metafacades.uml.ModelFacade#findClassWithTaggedValueOrHyperlink(String,
     *      String)
     * @throws UnsupportedOperationException always
     */
    @Override
    protected ClassifierFacade handleFindClassWithTaggedValueOrHyperlink(
        final String tag,
        final String value)
    {
        ClassifierFacade classWithTaggedValue = null;

        final Collection<ClassifierFacade> classes = this.getAllClasses();
        for (final Iterator<ClassifierFacade> classIterator = classes.iterator(); classIterator.hasNext()
            && classWithTaggedValue == null;)
        {
            final ClassifierFacade clazz = classIterator.next();
            if (clazz.findTaggedValue(tag, true) == null)
            {
                final Object taggedValue = clazz.findTaggedValue(UMLProfile.TAGGEDVALUE_MODEL_HYPERLINK);
                if (taggedValue != null)
                {
                    classWithTaggedValue = clazz;
                }
            }
            else
            {
                classWithTaggedValue = clazz;
            }
        }

        return classWithTaggedValue;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findActivityGraphByName(String)
     */
    @Override
    protected ActivityGraphFacade handleFindActivityGraphByName(
        final String name)
    {
        return this.findActivityGraphByNameAndStereotype(
            name,
            null);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findActivityGraphByNameAndStereotype(String,
     *      String)
     */
    @Override
    protected ActivityGraphFacade handleFindActivityGraphByNameAndStereotype(
        final String name,
        final String stereotypeName)
    {
        ActivityGraphFacade agfFound = null;

        final List<StateMachine> agfCollection =
            (List<StateMachine>) UmlUtilities.getAllMetaObjectsInstanceOf(
            StateMachine.class,
            UmlUtilities.getModels());

        for (final Iterator<StateMachine> it = agfCollection.iterator(); it.hasNext() && agfFound == null;)
        {
            final ActivityGraphFacade agf = (ActivityGraphFacade)this.shieldedElement(it.next());
            if (agf.getName().equals(name)
                && (stereotypeName == null || agf.hasStereotype(stereotypeName)))
            {
                agfFound = agf;
            }
        }
        return agfFound;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseByName(String)
     */
    @Override
    protected UseCaseFacade handleFindUseCaseByName(final String name)
    {
        return this.findUseCaseWithNameAndStereotype(
            name,
            null);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseWithNameAndStereotype(String,
     *      String)
     */
    @Override
    protected UseCaseFacade handleFindUseCaseWithNameAndStereotype(
        final String name,
        final String stereotypeName)
    {
        UseCaseFacade ucfFound = null;
        final Collection<UseCaseFacade> ucCollections = this.getAllUseCases();
        for (final Iterator<UseCaseFacade> it = ucCollections.iterator(); it.hasNext() && ucfFound == null;)
        {
            final UseCaseFacade ucf = it.next();
            if (ucf.getName().equals(name) &&
                (stereotypeName == null || ucf.hasStereotype(stereotypeName)))
            {
                ucfFound = ucf;
            }
        }
        return ucfFound;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findFinalStatesWithNameOrHyperlink(org.andromda.metafacades.uml.UseCaseFacade)
     */
    @Override
    protected Collection<FinalStateFacade> handleFindFinalStatesWithNameOrHyperlink(
        final UseCaseFacade useCase)
    {
        final List<FinalState> fsCollection =
            (List<FinalState>) UmlUtilities.getAllMetaObjectsInstanceOf(
            FinalState.class,
            UmlUtilities.getModels());
        CollectionUtils.filter(
            fsCollection,
            new Predicate()
            {
                public boolean evaluate(final Object candidate)
                {
                    final FinalState finalState = (FinalState)candidate;
                    return (finalState != null && StringUtils.isNotBlank(finalState.getName()) && finalState.getName().equals(useCase.getName()));
                }
            });

        return this.shieldedElements(fsCollection);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActionStatesWithStereotype(org.andromda.metafacades.uml.ActivityGraphFacade,
     *      String)
     */
    @Override
    protected Collection<ActionStateFacade> handleGetAllActionStatesWithStereotype(
        final ActivityGraphFacade activityGraph,
        final String stereotypeName)
    {
        final Collection<ActionStateFacade> asCollection = this.getAllActionStates();
        CollectionUtils.filter(
            asCollection,
            new Predicate()
            {
                public boolean evaluate(final Object candidate)
                {
                    final ActionStateFacade asf = (ActionStateFacade)candidate;
                    return asf.hasStereotype(stereotypeName) &&
                    asf.getPartition().getActivityGraph().equals(activityGraph);
                }
            });

        return asCollection;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getRootPackage()
     */
    @Override
    protected Package handleGetRootPackage()
    {
        final Package model = UmlUtilities.findModel(this.metaObject);
        if (ModelFacadeLogicImpl.LOGGER.isDebugEnabled())
        {
            ModelFacadeLogicImpl.LOGGER.debug("Root package " + model);
        }
        return model;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActors()
     */
    @Override
    protected List<Actor> handleGetAllActors()
    {
        return (List<Actor>) UmlUtilities.getAllMetaObjectsInstanceOf(
            Actor.class,
            UmlUtilities.getModels());
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllUseCases()
     */
    @Override
    protected List<UseCase> handleGetAllUseCases()
    {
        return (List<UseCase>) UmlUtilities.getAllMetaObjectsInstanceOf(
            UseCase.class,
            UmlUtilities.getModels());
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActionStates()
     */
    @Override
    protected Collection<ActionStateFacade> handleGetAllActionStates()
    {
        // cf documentation, action states are mapped to uml2 normal state
        final Collection allActionStates =
            UmlUtilities.getAllMetaObjectsInstanceOf(
                State.class,
                UmlUtilities.getModels());
        CollectionUtils.filter(
            allActionStates,
            new Predicate()
            {
                public boolean evaluate(final Object candidate)
                {
                    return (!(candidate instanceof FinalState));
                }
            });
        return allActionStates;
    }

    /** NOT IMPLEMENTED
     * @see org.andromda.metafacades.uml.ModelFacade#getAllObjectFlowStates()
     */
    @Override
    protected Collection handleGetAllObjectFlowStates()
    {
        // TODO: Implement handleGetAllObjectFlowStates
        return Collections.emptyList();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllClasses()
     */
    @Override
    protected List<ClassifierFacade> handleGetAllClasses()
    {
        List<ClassifierFacade> facades = new ArrayList<ClassifierFacade>();
        List<Class> classes = (List<Class>) UmlUtilities.getAllMetaObjectsInstanceOf(
            Class.class,
            UmlUtilities.getModels());
        // Some examples where UML2 Class does not map to andromda ClassifierFacade:
        // ActivityImpl -> EventFacadeLogicImpl
        // Must filter out classes that don't map correctly, we just want Classes and Datatypes and Interfaces
        for (Class metaClass : classes)
        {
            MetafacadeBase metafacade = this.shieldedElement(metaClass);
            if (metafacade instanceof ClassifierFacade)
            {
                facades.add((ClassifierFacade) metafacade);
            }
        }
        return facades;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ModelFacadeLogic#handleGetAllTransitions()
     */
    @Override
    protected List<Transition> handleGetAllTransitions()
    {
        return (List<Transition>) UmlUtilities.getAllMetaObjectsInstanceOf(
            Transition.class,
            UmlUtilities.getModels());
    }
}
