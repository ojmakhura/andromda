package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.TransitionFacade;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Vertex;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.StateVertexFacade.
 *
 * @see org.andromda.metafacades.uml.StateVertexFacade
 */
public class StateVertexFacadeLogicImpl
    extends StateVertexFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public StateVertexFacadeLogicImpl(
        final Vertex metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getOutgoings()
     */
    @Override
    protected Collection<Transition> handleGetOutgoings()
    {
        List<Transition> outList = new ArrayList<Transition>();
        outList.addAll(this.metaObject.getOutgoings());
        return outList;
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getIncomings()
     */
    @Override
    protected Collection<Transition> handleGetIncomings()
    {
        List<Transition> inList = new ArrayList<Transition>();
        inList.addAll(this.metaObject.getIncomings());
        return inList;
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getContainer()
     */
    @Override
    protected Namespace handleGetContainer()
    {
        //TODO: What's this ?
        return this.metaObject.getContainer().getNamespace();
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getPartition()
     */
    @Override
    protected Region handleGetPartition()
    {
        return this.metaObject.getContainer();
    }

    /**
     * @see org.andromda.metafacades.uml.StateVertexFacade#getStateMachine()
     */
    @Override
    protected Element handleGetStateMachine()
    {
        Element owner = this.metaObject;
        while (!(owner instanceof StateMachine))
        {
            owner = owner.getOwner();
        }
        return owner;
    }

    /**
     * getStateMachine
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        return getStateMachine();
    }
}
