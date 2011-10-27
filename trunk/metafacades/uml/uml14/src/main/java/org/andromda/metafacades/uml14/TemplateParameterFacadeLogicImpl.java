package org.andromda.metafacades.uml14;

import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TemplateParameter;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.TemplateParameterFacade.
 *
 * @see org.andromda.metafacades.uml.TemplateParameterFacade
 * @author Bob Fields
 */
public class TemplateParameterFacadeLogicImpl
    extends TemplateParameterFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public TemplateParameterFacadeLogicImpl (TemplateParameter metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getParameter()
     */
    @Override
    protected ModelElement handleGetParameter()
    {
        return metaObject.getParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getDefaultElement
     */
    @Override
    protected ModelElement handleGetDefaultElement()
    {
        return metaObject.getDefaultElement();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationName
     */
    @Override
    public String getValidationName()
    {
        return metaObject.getParameter().getName();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner
     */
    @Override
    public ModelElement getValidationOwner()
    {
        return metaObject.getTemplate();
    }
}