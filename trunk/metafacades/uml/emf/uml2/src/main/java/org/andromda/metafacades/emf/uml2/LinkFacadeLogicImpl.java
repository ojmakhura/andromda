package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkFacade.
 *
 * @see org.andromda.metafacades.uml.LinkFacade
 */
public class LinkFacadeLogicImpl extends LinkFacadeLogic
{
    private static final long serialVersionUID = -1940280187951928498L;

    /**
     * @param metaObject
     * @param context
     */
    public LinkFacadeLogicImpl(LinkInstance metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return metaObject.getSlots()
     * @see org.andromda.metafacades.uml.LinkFacade#getLinkEnds()
     */
    protected Collection handleGetLinkEnds()
    {
        return CollectionUtils.collect(this.metaObject.getSlots(), UmlUtilities.ELEMENT_TRANSFORMER);
    }
}