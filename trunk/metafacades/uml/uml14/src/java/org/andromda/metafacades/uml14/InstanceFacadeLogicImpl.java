package org.andromda.metafacades.uml14;

import java.util.Collection;
import org.omg.uml.behavioralelements.commonbehavior.Instance;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.InstanceFacade.
 *
 * @see org.andromda.metafacades.uml.InstanceFacade
 * @author Bob Fields
 */
public class InstanceFacadeLogicImpl
    extends InstanceFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public InstanceFacadeLogicImpl (Instance metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getClassifiers()
     */
    @Override
    protected Collection handleGetClassifiers()
    {
        return metaObject.getClassifier();
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getLinkEnds()
     */
    @Override
    protected Collection handleGetLinkEnds()
    {
        return metaObject.getLinkEnd();
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getOwnedInstances()
     */
    @Override
    protected Collection handleGetOwnedInstances()
    {
        return metaObject.getOwnedInstance();
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getOwnedLinks()
     */
    @Override
    protected Collection handleGetOwnedLinks()
    {
        return metaObject.getOwnedLink();
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getSlots()
     */
    @Override
    protected Collection handleGetSlots()
    {
        return metaObject.getSlot();
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getAttributeLinks()
     */
    @Override
    protected Collection handleGetAttributeLinks()
    {
        // wouter: in UML1.4 the slots only convey the attribute links (unless I'm mistaken this is different in UML2)
        return metaObject.getSlot();
    }
}