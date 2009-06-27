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
    public GeneralizationFacadeLogicImpl(
        final org.eclipse.uml2.Generalization metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizationFacade#getChild()
     */
    protected java.lang.Object handleGetChild()
    {
        return this.metaObject.getSpecific();
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizationFacade#getParent()
     */
    protected java.lang.Object handleGetParent()
    {
        return this.metaObject.getGeneral();
    }
}