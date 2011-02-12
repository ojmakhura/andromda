package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.TemplateParameterFacade.
 *
 * @see org.andromda.metafacades.uml.TemplateParameterFacade
 */
public class TemplateParameterFacadeLogicImpl
    extends TemplateParameterFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public TemplateParameterFacadeLogicImpl(
        final org.eclipse.uml2.TemplateParameter metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return metaObject.getParameteredElement()
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getParameter()
     */
    protected Object handleGetParameter()
    {
        // TODO: Be sure it works with RSM / MD11.5
        return metaObject.getParameteredElement();
    }

    /**
     * @return metaObject.getDefault()
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getDefaultElement()
     */
    protected Object handleGetDefaultElement()
    {
        // TODO: Be sure it works with RSM / MD11.5
        return metaObject.getDefault();
    }
}