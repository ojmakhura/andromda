package org.andromda.metafacades.emf.uml2;


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
        final org.eclipse.uml2.Generalization metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return metaObject.getSpecific()
     * @see org.andromda.metafacades.uml.GeneralizationFacade#getChild()
     */
    protected Object handleGetChild()
    {
        return this.metaObject.getSpecific();
    }

    /**
     * @return metaObject.getGeneral()
     * @see org.andromda.metafacades.uml.GeneralizationFacade#getParent()
     */
    protected Object handleGetParent()
    {
        return this.metaObject.getGeneral();
    }
}