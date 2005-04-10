package org.andromda.cartridges.meta.metafacades;

import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;

import java.util.Iterator;

/**
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @since 25.02.2004
 */
public class UMLOperationData extends MethodData
{
    /**
     * Constructs a MethodData object from an OperationFacade.
     *
     * @param metafacadeName the name of the parent class
     * @param operation      the operation facade
     */
    public UMLOperationData(String metafacadeName, OperationFacade operation)
    {
        super(metafacadeName, operation.getVisibility(), operation.isAbstract(), operation.getReturnType().getFullyQualifiedName(), operation.getName(), operation.getDocumentation(
                "    * "));

        for (Iterator iterator = operation.getArguments().iterator(); iterator.hasNext();)
        {
            ParameterFacade parameter = (ParameterFacade) iterator.next();
            addArgument(new ArgumentData(parameter.getType().getFullyQualifiedName(), parameter.getName()));
        }
    }

}
