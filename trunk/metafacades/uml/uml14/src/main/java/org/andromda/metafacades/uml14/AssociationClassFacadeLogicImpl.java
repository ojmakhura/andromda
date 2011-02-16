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
    private static final long serialVersionUID = 7868769285302664440L;

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

    /**
     * @see org.andromda.metafacades.uml14.AssociationClassFacadeLogic#handleGetConnectionAssociationEnds()
     */
    protected Collection handleGetConnectionAssociationEnds()
    {
        return this.metaObject.getConnection();
    }
}