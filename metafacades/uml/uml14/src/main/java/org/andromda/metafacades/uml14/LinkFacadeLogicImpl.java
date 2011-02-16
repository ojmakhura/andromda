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
    private static final long serialVersionUID = -9015446491643027873L;

    /**
     * @param metaObject
     * @param context
     */
    public LinkFacadeLogicImpl (Link metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.LinkFacadeLogic#handleGetLinkEnds()
     */
    protected Collection handleGetLinkEnds()
    {
        return this.metaObject.getConnection();
    }
}