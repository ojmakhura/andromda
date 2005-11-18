package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.GeneralizationFacade.
 *
 * @see org.andromda.metafacades.uml.GeneralizationFacade
 */
public class GeneralizationFacadeLogicImpl
    extends GeneralizationFacadeLogic
{
    public GeneralizationFacadeLogicImpl(
        org.eclipse.uml2.Generalization metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizationFacade#getChild()
     */
    protected java.lang.Object handleGetChild()
    {
        return metaObject.getSpecific();
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizationFacade#getParent()
     */
    protected java.lang.Object handleGetParent()
    {
        return metaObject.getGeneral();
    }
}