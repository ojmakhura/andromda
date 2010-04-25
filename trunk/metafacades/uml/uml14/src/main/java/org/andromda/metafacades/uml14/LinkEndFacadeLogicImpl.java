package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Collections;
import org.andromda.metafacades.uml.InstanceFacade;
import org.omg.uml.behavioralelements.commonbehavior.Instance;
import org.omg.uml.behavioralelements.commonbehavior.Link;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.behavioralelements.commonbehavior.LinkEnd;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkEndFacade.
 *
 * @see org.andromda.metafacades.uml.LinkEndFacade
 * @author Bob Fields
 */
public class LinkEndFacadeLogicImpl
    extends LinkEndFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public LinkEndFacadeLogicImpl (LinkEnd metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getInstance()
     */
    @Override
    protected Instance handleGetInstance()
    {
        return metaObject.getInstance();
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getAssociationEnd()
     */
    @Override
    protected AssociationEnd handleGetAssociationEnd()
    {
        return metaObject.getAssociationEnd();
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getLink()
     */
    @Override
    protected Link handleGetLink()
    {
        return metaObject.getLink();
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getInstances()
     */
    @Override
    protected Collection<InstanceFacade> handleGetInstances()
    {
        return Collections.singleton(this.getInstance());
    }
}