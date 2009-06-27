package org.andromda.metafacades.uml14;

import org.omg.uml.foundation.core.Stereotype;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.StereotypeFacade
 * @author Bob Fields
 */
public class StereotypeFacadeLogicImpl
        extends StereotypeFacadeLogic
{
    // ---------------- constructor -------------------------------

    /**
     * @param metaObject
     * @param context
     */
    public StereotypeFacadeLogicImpl(Stereotype metaObject, String context)
    {
        super(metaObject, context);
    }
}
