package org.andromda.cartridges.meta.metafacades;

import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;

/**
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @since 25.02.2004
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
        final String metafacadeName,
        final OperationFacade operation)
    {
        super(
            metafacadeName,
            operation.getVisibility(),
            operation.isAbstract(),
            operation.getGetterSetterReturnTypeName(),
            operation.getName(),
            operation.getDocumentation("    * "));

        for (ParameterFacade parameter : operation.getArguments())
        {
            addArgument(
                new ArgumentData(
                    parameter.getType().getFullyQualifiedName(),
                    parameter.getName()));
        }
    }
}