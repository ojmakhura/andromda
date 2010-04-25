package org.andromda.cartridges.deployment.metafacades;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.cartridges.deployment.psm.ant.CompilerTaskCall;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.deployment.metafacades.DeployedPackageFacade.
 *
 * @see org.andromda.cartridges.deployment.metafacades.DeployedPackageFacade
 */
public class DeployedPackageFacadeLogicImpl
    extends DeployedPackageFacadeLogic
{

    public DeployedPackageFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected Collection handleGetTasksForCompilationPhase()
    {
        // compile this single package
        ArrayList result = new ArrayList(1);
        result.add(new CompilerTaskCall(getFullyQualifiedName(), "javac"));
        return result;
    }
}