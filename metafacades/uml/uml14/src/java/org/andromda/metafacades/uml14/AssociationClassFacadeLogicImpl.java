package org.andromda.metafacades.uml14;

import java.util.Collection;
import org.omg.uml.foundation.core.AssociationClass;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AssociationClassFacade.
 *
 * @see org.andromda.metafacades.uml.AssociationClassFacade
 * @author Bob Fields
 */
public class AssociationClassFacadeLogicImpl
    extends AssociationClassFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public AssociationClassFacadeLogicImpl(
        AssociationClass metaObject,
        String context)
    {
        super(metaObject, context);
    }

    protected Collection handleGetConnectionAssociationEnds()
    {
        return this.metaObject.getConnection();
    }
}