package org.andromda.metafacades.emf.uml22;

import org.eclipse.uml2.uml.ParameterableElement;
import org.eclipse.uml2.uml.TemplateParameterSubstitution;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.TemplateArgumentFacade.
 *
 * @see org.andromda.metafacades.uml.TemplateArgumentFacade
 */
public class TemplateArgumentFacadeLogicImpl
    extends TemplateArgumentFacadeLogic
{
    private static final long serialVersionUID = 6177943588326576358L;

    /**
     * @param metaObject
     * @param context
     */
    public TemplateArgumentFacadeLogicImpl(
        final TemplateParameterSubstitution metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.TemplateArgumentFacade#getElement()
     */
    @Override
    protected ParameterableElement handleGetElement()
    {
        // TODO: Be sure it works with RSM / MD11.5
        // It may be ownedActual. UML2 3.0 uses getActual(). v2 uses getActuals()
        return this.metaObject.getActual();
        /*Collection<ParameterableElement> actuals = new ArrayList<ParameterableElement>(this.metaObject.getActuals());
        if (actuals.isEmpty())
        {
            return null;
        }
        return actuals.iterator().next();*/
    }
}
