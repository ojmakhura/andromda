package org.andromda.metafacades.uml14;

import java.util.Collection;

import org.omg.uml.foundation.core.Namespace;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.NamespaceFacade
 */
public class NamespaceFacadeLogicImpl
    extends NamespaceFacadeLogic
{
    // ---------------- constructor -------------------------------
    public NamespaceFacadeLogicImpl(
        org.omg.uml.foundation.core.Namespace metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    // ------------- relations ------------------
    protected Collection handleGetOwnedElements()
    {
        return ((Namespace)metaObject).getOwnedElement();
    }
}