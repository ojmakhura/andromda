package org.andromda.metafacades.uml14;

import java.util.Collection;
import org.omg.uml.behavioralelements.commonbehavior.Link;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkFacade.
 *
 * @see org.andromda.metafacades.uml.LinkFacade
 * @author Bob Fields
 */
public class LinkFacadeLogicImpl
    extends LinkFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public LinkFacadeLogicImpl (Link metaObject, String context)
    {
        super (metaObject, context);
    }

    protected Collection handleGetLinkEnds()
    {
        return this.metaObject.getConnection();
    }
}