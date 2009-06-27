package org.andromda.cartridges.deployment.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.deployment.profile.DeploymentProfile;
import org.andromda.metafacades.uml.DependencyFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.deployment.metafacades.ComponentFacade.
 *
 * @see org.andromda.cartridges.deployment.metafacades.ComponentFacade
 */
public class ComponentFacadeLogicImpl
    extends ComponentFacadeLogic
{

    public ComponentFacadeLogicImpl (org.omg.uml.foundation.core.Component metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.cartridges.deployment.metafacades.ComponentFacade#getManifestingArtifacts()
     */
    protected java.util.Collection handleGetManifestingArtifacts()
    {
        ArrayList result = new ArrayList();

        Collection dependencies = getTargetDependencies();
        for (Iterator iter = dependencies.iterator(); iter.hasNext();)
        {
            DependencyFacade element = (DependencyFacade) iter.next();
            if (element.hasStereotype(DeploymentProfile.STEREOTYPE_MANIFEST))
            {
                result.add(element.getSourceElement());
            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.cartridges.deployment.metafacades.ComponentFacadeLogic#handleGetTasksForDeploymentPhase()
     */
    protected Collection handleGetTasksForDeploymentPhase()
    {
        ArrayList result = new ArrayList();

        Collection manifestingArtifacts = getManifestingArtifacts();
        for (Iterator iter = manifestingArtifacts.iterator(); iter.hasNext();)
        {
            ArtifactFacade element = (ArtifactFacade) iter.next();
            result.addAll(element.getTasksForDeploymentPhase());
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.cartridges.deployment.metafacades.ComponentFacadeLogic#handleGetTasksForCompilationPhase()
     */
    protected Collection handleGetTasksForCompilationPhase()
    {
        ArrayList result = new ArrayList();

        Collection manifestingArtifacts = getManifestingArtifacts();
        for (Iterator iter = manifestingArtifacts.iterator(); iter.hasNext();)
        {
            ArtifactFacade element = (ArtifactFacade) iter.next();
            result.addAll(element.getTasksForCompilationPhase());
        }

        return result;
    }

}