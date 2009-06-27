package org.andromda.metafacades.emf.uml22;


import org.eclipse.uml2.uml.Generalization;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.GeneralizationFacade.
 *
 * @see org.andromda.metafacades.uml.GeneralizationFacade
 */
public class GeneralizationFacadeLogicImpl
    extends GeneralizationFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public GeneralizationFacadeLogicImpl(
        final Generalization metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizationFacade#getChild()
     */
    @Override
    protected Object handleGetChild()
    {
        return this.metaObject.getSpecific();
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizationFacade#getParent()
     */
    @Override
    protected Object handleGetParent()
    {
        return this.metaObject.getGeneral();
    }
}
