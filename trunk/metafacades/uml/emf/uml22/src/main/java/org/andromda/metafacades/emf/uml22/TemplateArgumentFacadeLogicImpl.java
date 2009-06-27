package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
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
        // It may be ownedActual
        Collection<ParameterableElement> actuals = new ArrayList<ParameterableElement>(this.metaObject.getActuals());
        if (actuals.isEmpty())
        {
            return null;
        }
        return actuals.iterator().next();
    }
}
