package org.andromda.cartridges.thymeleaf.metafacades;

import java.util.Collection;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UseCaseFacade;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf..metafacades.ThymeleafPortletPreferences.
 *
 * @see org.andromda.cartridges.thymeleaf..metafacades.ThymeleafPortletPreferences
 */
public class ThymeleafPortletPreferencesLogicImpl
    extends ThymeleafPortletPreferencesLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafPortletPreferencesLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }
    /**
     * @return useCase
     * @see org.andromda.cartridges.thymeleaf..metafacades.ThymeleafPortletPreferences#getUseCase()
     */
    protected Object handleGetUseCase()
    {
        UseCaseFacade useCase = null;
        final Collection<DependencyFacade> dependencies = this.getTargetDependencies();
        if (dependencies != null && !dependencies.isEmpty())
        {
            for (final DependencyFacade dependency : dependencies)
            {
                final ModelElementFacade source = dependency.getSourceElement();
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
