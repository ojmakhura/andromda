package org.andromda.metafacades.emf.uml22;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Namespace;
import java.util.Collection;

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
     * @see org.andromda.metafacades.uml.NamespaceFacade#getOwnedElements()
     */
    @Override
    protected Collection<Element> handleGetOwnedElements()
    {
        return ((Namespace)this.metaObject).getOwnedElements();
    }
}
