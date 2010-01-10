package org.andromda.cartridges.jsf.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.UseCaseFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFPortletPreferences.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFPortletPreferences
 */
public class JSFPortletPreferencesLogicImpl
    extends JSFPortletPreferencesLogic
{

    public JSFPortletPreferencesLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFPortletPreferences#getUseCase()
     */
    protected java.lang.Object handleGetUseCase()
    {
        UseCaseFacade useCase = null;
        final Collection dependencies = this.getTargetDependencies();
        if (dependencies != null && !dependencies.isEmpty())
        {
            for (final Iterator iterator = dependencies.iterator(); iterator.hasNext();)
            {
                final DependencyFacade dependency = (DependencyFacade)iterator.next();
                final Object source = dependency.getSourceElement();
                if (source instanceof UseCaseFacade)
                {
                    useCase = (UseCaseFacade)source;
                    break;
                }
            }
        }
        return useCase;
    }

}