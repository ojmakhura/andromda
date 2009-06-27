package org.andromda.metafacades.emf.uml2;

import org.apache.commons.collections.CollectionUtils;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkFacade.
 *
 * @see org.andromda.metafacades.uml.LinkFacade
 */
public class LinkFacadeLogicImpl extends LinkFacadeLogic
{
    public LinkFacadeLogicImpl(LinkInstance metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.LinkFacade#getLinkEnds()
     */
    protected java.util.Collection handleGetLinkEnds()
    {
        return CollectionUtils.collect(this.metaObject.getSlots(), UmlUtilities.ELEMENT_TRANSFORMER);
    }
}