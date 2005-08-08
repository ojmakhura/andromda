package org.andromda.modules.xmilink.uml14;

import org.andromda.modules.xmilink.ExportContext;
import org.andromda.modules.xmilink.ExportStrategyFactory;

import com.togethersoft.openapi.model.elements.Entity;

/**
 * Exports a UML 1.4 class.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 17.09.2004
 */
public class UMLClassExportStrategy
        extends UMLEntityExportStrategy
{

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("Class", UMLClassExportStrategy.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#getEntityName()
     */
    protected String getEntityName(Entity entity)
    {
        if (entity.hasProperty("$interface"))
        {
            return "UML:Interface";
        }
        else
        {
            return "UML:Class";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#exportEntityBody(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityBody(Entity entity)
    {
        // REFACTOR
        writeStereotype();
        exportTaggedValues();

        // any children (e.g. attributes / operations)
        boolean hasChildren = entity.children().hasMoreElements();
        if (hasChildren)
        {
            ExportContext.getWriter().writeOpeningElement("UML:Classifier.feature");
        }
        super.exportEntityBody(entity);
        if (hasChildren)
        {
            ExportContext.getWriter().writeClosingElement("UML:Classifier.feature");
        }
    }

}
