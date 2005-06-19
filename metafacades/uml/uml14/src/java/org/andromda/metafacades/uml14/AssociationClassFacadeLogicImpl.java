package org.andromda.metafacades.uml14;

import java.util.Collection;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AssociationClassFacade.
 *
 * @see org.andromda.metafacades.uml.AssociationClassFacade
 */
public class AssociationClassFacadeLogicImpl
    extends AssociationClassFacadeLogic
{
    public AssociationClassFacadeLogicImpl(
        org.omg.uml.foundation.core.AssociationClass metaObject,
        String context)
    {
        super(metaObject, context);
    }

    protected Collection handleGetConnectionAssociationEnds()
    {
        return this.metaObject.getConnection();
    }
}