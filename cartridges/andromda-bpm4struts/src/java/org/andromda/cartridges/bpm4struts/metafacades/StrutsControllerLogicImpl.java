package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ServiceFacade;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController
 */
public class StrutsControllerLogicImpl
        extends StrutsControllerLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsController
{
    private String fullPath = null;
    private Collection services = null;
    private Object useCase = null;

    // ---------------- constructor -------------------------------
    
    public StrutsControllerLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsController ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getFullPath()()
     */
    public java.lang.String getFullPath()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && fullPath != null) return fullPath;
        return fullPath = '/' + getFullyQualifiedName().replace('.', '/');
    }

    // ------------- relations ------------------

    protected Collection handleGetServices()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && services != null) return services;

        final Collection servicesList = new LinkedList();
        final Collection dependencies = getDependencies();
        for (Iterator iterator = dependencies.iterator(); iterator.hasNext();)
        {
            DependencyFacade dependency = (DependencyFacade) iterator.next();
            ModelElementFacade target = dependency.getTargetElement();
            if (target instanceof ServiceFacade)
                servicesList.add(target);
        }
        return services = servicesList;
    }

    protected Object handleGetUseCase()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && useCase != null) return useCase;

        final Collection useCases = getModel().getAllUseCases();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            StrutsUseCase strutsUseCase = (StrutsUseCase) iterator.next();
            if (this.equals(strutsUseCase.getController()))
                return useCase = strutsUseCase;
        }
        return null;
    }
}
