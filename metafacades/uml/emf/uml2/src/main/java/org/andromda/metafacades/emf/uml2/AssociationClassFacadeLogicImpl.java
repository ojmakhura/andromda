package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
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
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public AssociationClassFacadeLogicImpl(
        final org.eclipse.uml2.AssociationClass metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return metaObject.getMemberEnds()
     * @see org.andromda.metafacades.uml.AssociationClassFacade#getConnectionAssociationEnds()
     */
    protected Collection handleGetConnectionAssociationEnds()
    {
        return CollectionUtils.collect(
                this.metaObject.getMemberEnds(),
                UmlUtilities.ELEMENT_TRANSFORMER);
    }
}