package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.andromda.utils.StringUtilsHelper;
import org.eclipse.uml2.Behavior;
import org.eclipse.uml2.StateMachine;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.UseCaseFacade.
 *
 * @see org.andromda.metafacades.uml.UseCaseFacade
 */
public class UseCaseFacadeLogicImpl
    extends UseCaseFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public UseCaseFacadeLogicImpl(
        final org.eclipse.uml2.UseCase metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getFirstActivityGraph
     * @see org.andromda.metafacades.uml.UseCaseFacade#getFirstActivityGraph()
     */
    protected Object handleGetFirstActivityGraph()
    {
        StateMachine activityGraph = null;
        Collection behaviors = new ArrayList();
        behaviors.addAll(this.metaObject.getOwnedBehaviors()); // For MD11.5
        behaviors.addAll(this.metaObject.getOwnedStateMachines()); // For RSM
        for (final Iterator iterator = behaviors.iterator(); iterator.hasNext() && activityGraph == null;)
        {
            final Behavior modelElement = (Behavior)iterator.next();
            if (modelElement instanceof StateMachine)
            {
                activityGraph = (StateMachine)modelElement;
            }
        }

        return activityGraph;
    }

    /**
     * @return metaObject.getExtensionPoints()
     * @see org.andromda.metafacades.uml.UseCaseFacade#getExtensionPoints()
     */
    protected Collection handleGetExtensionPoints()
    {
        return this.metaObject.getExtensionPoints();
    }

    /**
     * @return metaObject.getExtends()
     * @see org.andromda.metafacades.uml.UseCaseFacade#getExtends()
     */
    protected Collection handleGetExtends()
    {
        return this.metaObject.getExtends();
    }

    protected String handleGetName()
    {
        return StringUtilsHelper.toSingleLine(super.handleGetName());
    }

    protected Collection handleGetIncludes()
    {
        return this.metaObject.getIncludes();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getPackage();
    }
}