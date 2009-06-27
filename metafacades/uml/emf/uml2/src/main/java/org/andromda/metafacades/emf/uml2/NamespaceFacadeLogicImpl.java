package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.Namespace;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.NamespaceFacade.
 *
 * @see org.andromda.metafacades.uml.NamespaceFacade
 */
public class NamespaceFacadeLogicImpl
    extends NamespaceFacadeLogic
{
    public NamespaceFacadeLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.NamespaceFacade#getOwnedElements()
     */
    protected java.util.Collection handleGetOwnedElements()
    {
        return ((Namespace)this.metaObject).getOwnedElements();
    }
}