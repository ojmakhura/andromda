package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.TemplateParameterFacade.
 *
 * @see org.andromda.metafacades.uml.TemplateParameterFacade
 */
public class TemplateParameterFacadeLogicImpl
    extends TemplateParameterFacadeLogic
{

    public TemplateParameterFacadeLogicImpl (org.omg.uml.foundation.core.TemplateParameter metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getParameter()
     */
    protected Object handleGetParameter()
    {
        return metaObject.getParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getDefaultElement()
     */
    protected Object handleGetDefaultElement()
    {
        return metaObject.getDefaultElement();
    }

}