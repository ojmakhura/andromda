package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.utils.StringUtilsHelper;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Extend;
import org.eclipse.uml2.uml.ExtensionPoint;
import org.eclipse.uml2.uml.Include;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UseCase;

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
     * @param metaObjectIn
     * @param context
     */
    public UseCaseFacadeLogicImpl(
        final UseCase metaObjectIn,
        final String context)
    {
        super(metaObjectIn, context);
    }

    /**
     * @see org.andromda.metafacades.uml.UseCaseFacade#getFirstActivityGraph()
     */
    @Override
    protected StateMachine handleGetFirstActivityGraph()
    {
        StateMachine activityGraph = null;
        Collection<Behavior> behaviors = new ArrayList<Behavior>();
        behaviors.addAll(this.metaObject.getOwnedBehaviors()); // For MD11.5
        //behaviors.addAll(this.metaObject.getOwnedStateMachines()); // For RSM // No longer exists in UML2 2.x
        for (final Iterator<Behavior> iterator = behaviors.iterator(); iterator.hasNext() && activityGraph == null;)
        {
            final Behavior modelElement = iterator.next();
            if (modelElement instanceof StateMachine)
            {
                activityGraph = (StateMachine)modelElement;
            }
        }

        return activityGraph;
    }

    /**
     * @see org.andromda.metafacades.uml.UseCaseFacade#getExtensionPoints()
     */
    @Override
    protected Collection<ExtensionPoint> handleGetExtensionPoints()
    {
        return this.metaObject.getExtensionPoints();
    }

    /**
     * @see org.andromda.metafacades.uml.UseCaseFacade#getExtends()
     */
    @Override
    protected Collection<Extend> handleGetExtends()
    {
        return this.metaObject.getExtends();
    }

    @Override
    protected String handleGetName()
    {
        return StringUtilsHelper.toSingleLine(super.handleGetName());
    }

    @Override
    protected Collection<Include> handleGetIncludes()
    {
        return this.metaObject.getIncludes();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
    public ModelElementFacade getValidationOwner()
    {
        return this.getPackage();
    }
}
