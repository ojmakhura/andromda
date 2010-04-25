package org.andromda.metafacades.uml14;

import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TemplateArgument;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.TemplateArgumentFacade.
 *
 * @see org.andromda.metafacades.uml.TemplateArgumentFacade
 * @author Bob Fields
 */
public class TemplateArgumentFacadeLogicImpl
    extends TemplateArgumentFacadeLogic
{

    /**
     * @param metaObject
     * @param context
     */
    public TemplateArgumentFacadeLogicImpl (TemplateArgument metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.metafacades.uml.TemplateArgumentFacade#getElement()
     */
    @Override
    protected ModelElement handleGetElement()
    {
        return metaObject.getModelElement();
    }
}