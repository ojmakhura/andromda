package org.andromda.metafacades.emf.uml2;

import org.apache.commons.collections.CollectionUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.AssociationClassFacade.
 *
 * @see org.andromda.metafacades.uml.AssociationClassFacade
 */
public class AssociationClassFacadeLogicImpl
    extends AssociationClassFacadeLogic
{
    public AssociationClassFacadeLogicImpl(
        final org.eclipse.uml2.AssociationClass metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationClassFacade#getConnectionAssociationEnds()
     */
    protected java.util.Collection handleGetConnectionAssociationEnds()
    {
        return CollectionUtils.collect(
                this.metaObject.getMemberEnds(),
                UmlUtilities.ELEMENT_TRANSFORMER);
    }
}