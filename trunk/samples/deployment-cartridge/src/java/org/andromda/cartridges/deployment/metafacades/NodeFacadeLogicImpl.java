package org.andromda.cartridges.deployment.metafacades;

import java.util.ArrayList;
import java.util.Iterator;

import org.andromda.cartridges.deployment.psm.ant.Project;
import org.andromda.cartridges.deployment.psm.ant.Target;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.deployment.metafacades.NodeFacade.
 * 
 * @see org.andromda.cartridges.deployment.metafacades.NodeFacade
 */
public class NodeFacadeLogicImpl extends NodeFacadeLogic
{

    public NodeFacadeLogicImpl(org.omg.uml.foundation.core.Node metaObject,
            String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.deployment.metafacades.NodeFacade#getDeployedComponents()
     */
    protected java.util.Collection handleGetDeployedComponents()
    {
        return this.metaObject.getDeployedComponent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.cartridges.deployment.metafacades.NodeFacadeLogic#handleTransformToAntProject()
     */
    protected Project handleTransformToAntProject()
    {
        ArrayList compilationTasks = new ArrayList();
        ArrayList deploymentTasks = new ArrayList();
        for (Iterator iter = getDeployedComponents().iterator(); iter.hasNext();)
        {
            ComponentFacade element = (ComponentFacade) iter.next();
            compilationTasks.addAll(element.getTasksForCompilationPhase());
            deploymentTasks.addAll(element.getTasksForDeploymentPhase());
        }

        Target compilationTarget = new Target("compile", null, compilationTasks);
        Target deploymentTarget = new Target("deploy", "compile",
                deploymentTasks);

        ArrayList allTargets = new ArrayList(2);
        allTargets.add(compilationTarget);
        allTargets.add(deploymentTarget);

        Project p = new Project(getFullyQualifiedName(), "deploy", allTargets);
        return p;
    }

}