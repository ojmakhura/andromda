package org.andromda.metafacades.emf.uml22;


import org.eclipse.uml2.uml.ParameterableElement;
import org.eclipse.uml2.uml.TemplateParameter;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.TemplateParameterFacade.
 *
 * @see org.andromda.metafacades.uml.TemplateParameterFacade
 */
public class TemplateParameterFacadeLogicImpl
    extends TemplateParameterFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public TemplateParameterFacadeLogicImpl(
        final TemplateParameter metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getParameter()
     */
    @Override
    protected ParameterableElement handleGetParameter()
    {
        // TODO: Be sure it works with RSM / MD11.5
        return this.metaObject.getParameteredElement();
    }

    /**
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getDefaultElement()
     */
    @Override
    protected ParameterableElement handleGetDefaultElement()
    {
        // TODO: Be sure it works with RSM / MD11.5
        return this.metaObject.getDefault();
    }
}
