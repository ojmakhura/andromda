package org.andromda.cartridges.meta.metafacades;

import java.util.Iterator;

import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;

/**
 * @since 25.02.2004
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 */
public class UMLOperationData
    extends MethodData
{
    /**
     * Constructs a MethodData object from an OperationFacade.
     * 
     * @param metafacadeName the name of the parent class
     * @param operation the operation facade
     */
    public UMLOperationData(
        String metafacadeName,
        OperationFacade operation)
    {
        super(metafacadeName, operation.getVisibility(), operation.isAbstract(), operation
            .getReturnType().getFullyQualifiedName(), operation.getName(), operation
            .getDocumentation("    * "));

        for (Iterator it = operation.getArguments().iterator(); it.hasNext();)
        {
            ParameterFacade p = (ParameterFacade)it.next();
            addArgument(new ArgumentData(p.getType().getFullyQualifiedName(), p
                .getName()));
        }
    }

}
