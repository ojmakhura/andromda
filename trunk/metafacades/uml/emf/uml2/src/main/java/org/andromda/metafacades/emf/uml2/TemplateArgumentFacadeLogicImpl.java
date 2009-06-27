package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.TemplateArgumentFacade.
 *
 * @see org.andromda.metafacades.uml.TemplateArgumentFacade
 */
public class TemplateArgumentFacadeLogicImpl
    extends TemplateArgumentFacadeLogic
{
    public TemplateArgumentFacadeLogicImpl(
        final org.eclipse.uml2.TemplateParameterSubstitution metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.TemplateArgumentFacade#getElement()
     */
    protected java.lang.Object handleGetElement()
    {
        // TODO: Be sure it works with RSM / MD11.5
        // It may be ownedActual
        Collection actuals = new ArrayList(this.metaObject.getActuals());
        if (actuals.isEmpty())
        {
            return null;
        }
        return actuals.iterator().next();
    }
}