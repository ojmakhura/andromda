package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
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
    /**
     * @param metaObject
     * @param context
     */
    public NamespaceFacadeLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return metaObject.getOwnedElements()
     * @see org.andromda.metafacades.uml.NamespaceFacade#getOwnedElements()
     */
    protected Collection handleGetOwnedElements()
    {
        return ((Namespace)this.metaObject).getOwnedElements();
    }
}