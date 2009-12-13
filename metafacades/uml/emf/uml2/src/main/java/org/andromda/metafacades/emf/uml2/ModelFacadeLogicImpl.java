package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.eclipse.uml2.Actor;
import org.eclipse.uml2.FinalState;
import org.eclipse.uml2.State;
import org.eclipse.uml2.StateMachine;
import org.eclipse.uml2.UseCase;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ModelFacade.
 *
 * @see org.andromda.metafacades.uml.ModelFacade
 * @author Bob Fields
 */
public class ModelFacadeLogicImpl
    extends ModelFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ModelFacadeLogicImpl(
        final org.eclipse.uml2.util.UML2Resource metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(ModelFacadeLogicImpl.class);

    /**
     * @param tag
     * @param value
     * @return UnsupportedOperationException
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseWithTaggedValueOrHyperlink(String,
     *      String)
     */
    protected UseCaseFacade handleFindUseCaseWithTaggedValueOrHyperlink(
        final String tag,
        final String value)
    {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param tag
     * @param value
     * @return UnsupportedOperationException
     * @see org.andromda.metafacades.uml.ModelFacade#findClassWithTaggedValueOrHyperlink(String,
     *      String)
     */
    protected ClassifierFacade handleFindClassWithTaggedValueOrHyperlink(
        final String tag,
        final String value)
    {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * @param name
     * @return activityGraphByName
     * @see org.andromda.metafacades.uml.ModelFacade#findActivityGraphByName(String)
     */
    protected ActivityGraphFacade handleFindActivityGraphByName(
        final String name)
    {
        return this.findActivityGraphByNameAndStereotype(
            name,
            null);
    }

    /**
     * @param name
     * @param stereotypeName
     * @return activityGraphByNameAndStereotype
     * @see org.andromda.metafacades.uml.ModelFacade#findActivityGraphByNameAndStereotype(String,
     *      String)
     */
    protected ActivityGraphFacade handleFindActivityGraphByNameAndStereotype(
        final String name,
        final String stereotypeName)
    {
        ActivityGraphFacade agfFound = null;

        Collection agfCollection =
            UmlUtilities.getAllMetaObjectsInstanceOf(
                StateMachine.class,
                UmlUtilities.findModel(this.metaObject));

        for (Iterator it = agfCollection.iterator(); it.hasNext() && agfFound == null;)
        {
            ActivityGraphFacade agf = (ActivityGraphFacade)this.shieldedElement(it.next());
            if (agf.getName().equals(name))
            {
                if(stereotypeName == null || agf.hasStereotype(stereotypeName))
                {
                agfFound = agf;
                }
            }
        }
        return agfFound;
    }

    /**
     * @param name
     * @return useCaseByName
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseByName(String)
     */
    protected UseCaseFacade handleFindUseCaseByName(final String name)
    {
        return this.findUseCaseWithNameAndStereotype(
            name,
            null);
    }

    /**
     * @param name
     * @param stereotypeName
     * @return useCaseWithNameAndStereotype
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseWithNameAndStereotype(String,
     *      String)
     */
    protected UseCaseFacade handleFindUseCaseWithNameAndStereotype(
        final String name,
        final String stereotypeName)
    {
        UseCaseFacade ucfFound = null;
        Collection ucCollections = this.getAllUseCases();
        for (Iterator it = ucCollections.iterator(); it.hasNext() && ucfFound == null;)
        {
            UseCaseFacade ucf = (UseCaseFacade)it.next();
            if (ucf.getName().equals(name))
            {
                if (stereotypeName == null || ucf.hasStereotype(stereotypeName))
                {
                    ucfFound = ucf;
                }
            }
        }
        return ucfFound;
    }

    /**
     * @param useCase
     * @return finalStatesWithNameOrHyperlink
     * @see org.andromda.metafacades.uml.ModelFacade#findFinalStatesWithNameOrHyperlink(org.andromda.metafacades.uml.UseCaseFacade)
     */
    protected Collection handleFindFinalStatesWithNameOrHyperlink(
        final UseCaseFacade useCase)
    {
        Collection fsCollection =
            UmlUtilities.getAllMetaObjectsInstanceOf(
                FinalState.class,
                UmlUtilities.findModel(this.metaObject));
        CollectionUtils.filter(
            fsCollection,
            new Predicate()
            {
                public boolean evaluate(final Object candidate)
                {
                    FinalState fs = (FinalState)candidate;
                    return fs.getName().equals(useCase.getName());
                }
            });

        return fsCollection;
    }

    /**
     * @param activityGraph
     * @param stereotypeName
     * @return aAllActionStatesWithStereotype
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActionStatesWithStereotype(org.andromda.metafacades.uml.ActivityGraphFacade,
     *      String)
     */
    protected Collection handleGetAllActionStatesWithStereotype(
        final ActivityGraphFacade activityGraph,
        final String stereotypeName)
    {
        Collection asCollection = this.getAllActionStates();
        CollectionUtils.filter(
            asCollection,
            new Predicate()
            {
                public boolean evaluate(final Object candidate)
                {
                    ActionStateFacade asf = (ActionStateFacade)candidate;
                    return asf.hasStereotype(stereotypeName) &&
                    asf.getPartition().getActivityGraph().equals(activityGraph);
                }
            });

        return asCollection;
    }

    /**
     * @return getRootPackage
     * @see org.andromda.metafacades.uml.ModelFacade#getRootPackage()
     */
    protected Object handleGetRootPackage()
    {
        Object model = UmlUtilities.findModel(this.metaObject);
        if (ModelFacadeLogicImpl.logger.isDebugEnabled())
        {
            ModelFacadeLogicImpl.logger.debug("Root package " + model);
        }
        return model;
    }

    /**
     * @return getAllActors
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActors()
     */
    protected Collection handleGetAllActors()
    {
        return UmlUtilities.getAllMetaObjectsInstanceOf(
            Actor.class,
            UmlUtilities.findModel(this.metaObject));
    }

    /**
     * @return getAllUseCases
     * @see org.andromda.metafacades.uml.ModelFacade#getAllUseCases()
     */
    protected Collection handleGetAllUseCases()
    {
        return UmlUtilities.getAllMetaObjectsInstanceOf(
            UseCase.class,
            UmlUtilities.findModel(this.metaObject));
    }

    /**
     * @return getAllActionStates
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActionStates()
     */
    protected Collection handleGetAllActionStates()
    {
        // cf documentation, action states are mapped to uml2 normal state
        Collection allActionStates =
            UmlUtilities.getAllMetaObjectsInstanceOf(
                State.class,
                UmlUtilities.findModel(this.metaObject));
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

    /**
     * @return getAllObjectFlowStates
     * @see org.andromda.metafacades.uml.ModelFacade#getAllObjectFlowStates()
     */
    protected Collection handleGetAllObjectFlowStates()
    {
        // TODO: Not implemented
        return Collections.emptyList();
    }

    /**
     * @return getAllClasses
     * @see org.andromda.metafacades.uml.ModelFacade#getAllClasses()
     */
    protected Collection handleGetAllClasses()
    {
        return UmlUtilities.getAllMetaObjectsInstanceOf(
            org.eclipse.uml2.Class.class,
            UmlUtilities.findModel(this.metaObject));
    }

    /**
     * @return getAllTransitions
     * @see org.andromda.metafacades.uml.ModelFacade#getAllTransitions()
     */
    protected Collection handleGetAllTransitions()
    {
        return UmlUtilities.getAllMetaObjectsInstanceOf(
            org.eclipse.uml2.Transition.class,
            UmlUtilities.findModel(this.metaObject));
    }
}