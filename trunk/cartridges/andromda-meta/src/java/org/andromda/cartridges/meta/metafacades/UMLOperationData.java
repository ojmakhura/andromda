package org.andromda.cartridges.meta.metafacades;

import java.util.Iterator;

import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;

/**
 *
 * @since 25.02.2004
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class UMLOperationData extends MethodData
{
    /**
     * Constructs a MethodData object from an OperationFacade.
     * 
     * @param interfaceName the name of the parent class
     * @param op the operation facade
     */
    public UMLOperationData(String interfaceName, OperationFacade op)
    {
        super(
            interfaceName,
            op.getVisibility().toString(),
            op.isAbstract(),
            op.getType().getFullyQualifiedName(),
            op.getName(),
            op.getDocumentation("    * "));

        for (Iterator it = op.getArguments().iterator(); it.hasNext();)
        {
            ParameterFacade p = (ParameterFacade) it.next();
            addArgument(
                new ArgumentData(
                    p.getType().getFullyQualifiedName(),
                    p.getName()));
        }
    }

}
