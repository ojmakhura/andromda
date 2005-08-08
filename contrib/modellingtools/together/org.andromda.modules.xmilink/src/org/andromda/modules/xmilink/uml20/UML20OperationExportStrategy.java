package org.andromda.modules.xmilink.uml20;

import org.andromda.modules.xmilink.ExportStrategyFactory;
import org.andromda.modules.xmilink.uml14.UMLClassExportStrategy;
import org.andromda.modules.xmilink.uml14.UMLOperationExportStrategy;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 25.10.2004
 */
public class UML20OperationExportStrategy
        extends UMLOperationExportStrategy
{

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("Operation20",
                UML20OperationExportStrategy.class);
    }

}
