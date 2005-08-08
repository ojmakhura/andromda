package org.andromda.modules.xmilink.uml14;

import org.andromda.modules.xmilink.ExportContext;
import org.andromda.modules.xmilink.ExportStrategyFactory;

import com.togethersoft.openapi.model.elements.Entity;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 17.09.2004
 */
public class UMLPackageExportStrategy
        extends UMLEntityExportStrategy
{

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("Package",
                UMLPackageExportStrategy.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#getEntityName()
     */
    protected String getEntityName(Entity entity)
    {
        return "UML:Package";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#doExportProperty(java.lang.String)
     */
    protected boolean doExportProperty(String name, String value)
    {
        if (name.equalsIgnoreCase("$name"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#suppressEntity(com.togethersoft.openapi.model.elements.Entity)
     */
    protected boolean suppressEntity(Entity entity)
    {
        if (entity != null)
        {
            String packageName = entity.getPropertyValue("$name");
            if (packageName.equals("<default>"))
            {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#exportEntityBody(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityBody(Entity entity)
    {
        boolean hasChildren = entity.children().hasMoreElements();
        boolean suppress = suppressEntity(entity);
        if (!suppress)
        {
            if (hasChildren)
            {
                ExportContext.getWriter().writeOpeningElement("UML:Namespace.ownedElement");
            }
        }

        super.exportEntityBody(entity);

        if (!suppress)
        {
            if (hasChildren)
            {
                ExportContext.getWriter().writeClosingElement("UML:Namespace.ownedElement");
            }
        }
    }

}
