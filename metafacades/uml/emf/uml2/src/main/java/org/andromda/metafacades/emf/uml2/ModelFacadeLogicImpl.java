package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
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
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseWithTaggedValueOrHyperlink(java.lang.String,
     *      java.lang.String)
     */
    protected org.andromda.metafacades.uml.UseCaseFacade handleFindUseCaseWithTaggedValueOrHyperlink(
        final java.lang.String tag,
        final java.lang.String value)
    {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @see org.andromda.metafacades.uml.ModelFacade#findClassWithTaggedValueOrHyperlink(java.lang.String,
     *      java.lang.String)
     */
    protected org.andromda.metafacades.uml.ClassifierFacade handleFindClassWithTaggedValueOrHyperlink(
        final java.lang.String tag,
        final java.lang.String value)
    {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findActivityGraphByName(java.lang.String)
     */
    protected org.andromda.metafacades.uml.ActivityGraphFacade handleFindActivityGraphByName(
        final java.lang.String name)
    {
        return this.findActivityGraphByNameAndStereotype(
            name,
            null);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findActivityGraphByNameAndStereotype(java.lang.String,
     *      java.lang.String)
     */
    protected org.andromda.metafacades.uml.ActivityGraphFacade handleFindActivityGraphByNameAndStereotype(
        final java.lang.String name,
        final java.lang.String stereotypeName)
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
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseByName(java.lang.String)
     */
    protected org.andromda.metafacades.uml.UseCaseFacade handleFindUseCaseByName(final java.lang.String name)
    {
        return this.findUseCaseWithNameAndStereotype(
            name,
            null);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#findUseCaseWithNameAndStereotype(java.lang.String,
     *      java.lang.String)
     */
    protected org.andromda.metafacades.uml.UseCaseFacade handleFindUseCaseWithNameAndStereotype(
        final java.lang.String name,
        final java.lang.String stereotypeName)
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
     * @see org.andromda.metafacades.uml.ModelFacade#findFinalStatesWithNameOrHyperlink(org.andromda.metafacades.uml.UseCaseFacade)
     */
    protected java.util.Collection handleFindFinalStatesWithNameOrHyperlink(
        final org.andromda.metafacades.uml.UseCaseFacade useCase)
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
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActionStatesWithStereotype(org.andromda.metafacades.uml.ActivityGraphFacade,
     *      java.lang.String)
     */
    protected java.util.Collection handleGetAllActionStatesWithStereotype(
        final org.andromda.metafacades.uml.ActivityGraphFacade activityGraph,
        final java.lang.String stereotypeName)
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
     * @see org.andromda.metafacades.uml.ModelFacade#getRootPackage()
     */
    protected java.lang.Object handleGetRootPackage()
    {
        Object model = UmlUtilities.findModel(this.metaObject);
        if (this.logger.isDebugEnabled())
        {
            this.logger.debug("Root package " + model);
        }
        return model;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActors()
     */
    protected java.util.Collection handleGetAllActors()
    {
        return UmlUtilities.getAllMetaObjectsInstanceOf(
            Actor.class,
            UmlUtilities.findModel(this.metaObject));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllUseCases()
     */
    protected java.util.Collection handleGetAllUseCases()
    {
        return UmlUtilities.getAllMetaObjectsInstanceOf(
            UseCase.class,
            UmlUtilities.findModel(this.metaObject));
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllActionStates()
     */
    protected java.util.Collection handleGetAllActionStates()
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
     * @see org.andromda.metafacades.uml.ModelFacade#getAllObjectFlowStates()
     */
    protected java.util.Collection handleGetAllObjectFlowStates()
    {
        // TODO: Not implemented
        return Collections.EMPTY_LIST;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelFacade#getAllClasses()
     */
    protected java.util.Collection handleGetAllClasses()
    {
        return UmlUtilities.getAllMetaObjectsInstanceOf(
            org.eclipse.uml2.Class.class,
            UmlUtilities.findModel(this.metaObject));
    }

    protected Collection handleGetAllTransitions()
    {
        return UmlUtilities.getAllMetaObjectsInstanceOf(
            org.eclipse.uml2.Transition.class,
            UmlUtilities.findModel(this.metaObject));
    }
}