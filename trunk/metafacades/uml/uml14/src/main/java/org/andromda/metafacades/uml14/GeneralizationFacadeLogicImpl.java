package org.andromda.metafacades.uml14;

import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.Generalization;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.GeneralizationFacade
 * @author Bob Fields
 */
public class GeneralizationFacadeLogicImpl
    extends GeneralizationFacadeLogic
{
    private static final long serialVersionUID = 5541756660727351738L;

    /**
     * @param metaObject
     * @param context
     */
    public GeneralizationFacadeLogicImpl(
        Generalization metaObject,
        String context)
    {
        super(metaObject, context);
    }

    // ------------- relations ------------------

    /**
     * @see org.andromda.metafacades.uml.GeneralizationFacade#getChild()
     */
    @Override
    public GeneralizableElement handleGetChild()
    {
        return metaObject.getChild();
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizationFacade#getParent()
     */
    @Override
    public GeneralizableElement handleGetParent()
    {
        return metaObject.getParent();
    }
}