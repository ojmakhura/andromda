package org.andromda.modules.xmilink.uml20;

import org.andromda.modules.xmilink.ExportStrategyFactory;
import org.andromda.modules.xmilink.uml14.UMLClassExportStrategy;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 25.10.2004
 */
public class UML20ClassExportStrategy
        extends UMLClassExportStrategy
{

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("Class20",
                UML20ClassExportStrategy.class);
    }

}
