package org.andromda.metafacades.emf.uml22;


import org.eclipse.uml2.uml.Stereotype;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.StereotypeFacade.
 *
 * @see org.andromda.metafacades.uml.StereotypeFacade
 */
public class StereotypeFacadeLogicImpl
    extends StereotypeFacadeLogic
{
    private static final long serialVersionUID = 6286379442773783893L;

    /**
     * @param metaObject
     * @param context
     */
    public StereotypeFacadeLogicImpl(
        final Stereotype metaObject,
        final String context)
    {
        super(metaObject, context);
    }
}
