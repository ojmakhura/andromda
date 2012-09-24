package org.andromda.cartridges.deployment.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.andromda.cartridges.deployment.profile.DeploymentProfile;
import org.andromda.cartridges.deployment.psm.ant.JarTaskCall;
import org.andromda.metafacades.uml.DependencyFacade;
import org.omg.uml.foundation.core.Artifact;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.deployment.metafacades.ArtifactFacade.
 *
 * @see org.andromda.cartridges.deployment.metafacades.ArtifactFacade
 */
public class ArtifactFacadeLogicImpl extends ArtifactFacadeLogic
{
    public ArtifactFacadeLogicImpl(
            Artifact metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.deployment.metafacades.ArtifactFacade#getWrappedPackages()
     */
    protected Collection handleGetWrappedPackages()
    {
        ArrayList result = new ArrayList();

        Collection<DependencyFacade> dependencies = getSourceDependencies();
        for (DependencyFacade element : dependencies)
        {
            if (element.hasStereotype(DeploymentProfile.STEREOTYPE_WRAPS))
            {
                result.add(element.getTargetElement());
            }
        }

        return result;
    }

    /**
     *
     * @see org.andromda.cartridges.deployment.metafacades.ArtifactFacadeLogic#handleGetTasksForCompilationPhase()
     */
    protected Collection handleGetTasksForCompilationPhase()
    {
        ArrayList result = new ArrayList();

        Collection<DeployedPackageFacade> wrappedPackages = getWrappedPackages();
        for (DeployedPackageFacade element : wrappedPackages)
        {
            result.addAll(element.getTasksForCompilationPhase());
        }

        return result;
    }

    /**
     *
     * @see org.andromda.cartridges.deployment.metafacades.ArtifactFacadeLogic#handleGetTasksForDeploymentPhase()
     */
    protected Collection handleGetTasksForDeploymentPhase()
    {
        ArrayList<String> packages = new ArrayList<String>();

        // find all packages which deploy in this artifact
        Collection<DeployedPackageFacade> wrappedPackages = getWrappedPackages();
        for (DeployedPackageFacade element : wrappedPackages)
        {
            packages.add(element.getFullyQualifiedName());
        }

        // jar all packages into one jar
        JarTaskCall taskCall = new JarTaskCall(getName(), packages, "jar");
        ArrayList result = new ArrayList(1);
        result.add(taskCall);
        return result;
    }
}
