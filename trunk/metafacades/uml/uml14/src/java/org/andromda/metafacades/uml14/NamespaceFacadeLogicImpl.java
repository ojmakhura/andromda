package org.andromda.metafacades.uml14;

import java.util.Collection;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.NamespaceFacade
 */
public class NamespaceFacadeLogicImpl
       extends NamespaceFacadeLogic
       implements org.andromda.metafacades.uml.NamespaceFacade
{
    // ---------------- constructor -------------------------------
    
    public NamespaceFacadeLogicImpl (org.omg.uml.foundation.core.Namespace metaObject, java.lang.String context)
    {
        super (metaObject, context);
    }
    // ------------- relations ------------------

    protected Collection handleGetOwnedElements()
    {
        return metaObject.getOwnedElement();
    }
}
