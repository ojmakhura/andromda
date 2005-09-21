package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.TemplateArgumentFacade.
 *
 * @see org.andromda.metafacades.uml.TemplateArgumentFacade
 */
public class TemplateArgumentFacadeLogicImpl
    extends TemplateArgumentFacadeLogic
{

    public TemplateArgumentFacadeLogicImpl (org.omg.uml.foundation.core.TemplateArgument metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.metafacades.uml.TemplateArgumentFacade#getElement()
     */
    protected java.lang.Object handleGetElement()
    {
        return metaObject.getModelElement();
    }
}