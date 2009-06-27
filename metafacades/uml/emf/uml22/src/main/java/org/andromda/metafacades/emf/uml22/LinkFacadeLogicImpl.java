package org.andromda.metafacades.emf.uml22;

import org.apache.commons.collections.CollectionUtils;


import java.util.Collection;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkFacade.
 *
 * @see org.andromda.metafacades.uml.LinkFacade
 */
public class LinkFacadeLogicImpl extends LinkFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public LinkFacadeLogicImpl(LinkInstance metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.LinkFacade#getLinkEnds()
     */
    @Override
    protected Collection handleGetLinkEnds()
    {
        return CollectionUtils.collect(this.metaObject.getSlots(), UmlUtilities.ELEMENT_TRANSFORMER);
    }
}
