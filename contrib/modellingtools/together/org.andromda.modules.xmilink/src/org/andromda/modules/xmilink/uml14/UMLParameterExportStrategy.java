package org.andromda.modules.xmilink.uml14;

import org.andromda.modules.xmilink.ExportContext;
import org.andromda.modules.xmilink.ExportStrategyFactory;

import com.togethersoft.openapi.model.elements.Entity;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 18.10.2004
 */
public class UMLParameterExportStrategy
        extends UMLEntityExportStrategy
{

    // the type of the attribute
    private String type;

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("Parameter",
                UMLParameterExportStrategy.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#reset()
     */
    protected void reset()
    {
        super.reset();
        this.type = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#getEntityName()
     */
    protected String getEntityName()
    {
        return "UML:Parameter";
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
        else if (name.equalsIgnoreCase("$type"))
        {
            this.type = value;
            return false;
        }
        else
        {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#exportEntityBody(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityBody(Entity entity)
    {
        // export the type
        if (this.type != null)
        {
            // <UML:Parameter.type>
            ExportContext.getWriter().writeOpeningElement("UML:Parameter.type");

            // <UML:DataType xmi.idref = 'sm$c5aa00:fe48388b81:-7f99'/>
            ExportContext.getWriter().writeOpeningElementStart("UML:DataType");

            String datatypeRefID = getDatatypeRefID(entity);
            if (datatypeRefID != null)
            {
                ExportContext.getWriter().writeProperty("xmi.idref", datatypeRefID);
            }
            else
            {
                ExportContext.getWriter().writeProperty("name", type);
            }
            ExportContext.getWriter().writeOpeningElementEnd(true);

            // </UML:Parameter.type>
            ExportContext.getWriter().writeClosingElement("UML:Parameter.type");
        }

        super.exportEntityBody(entity);
    }

    private String getDatatypeRefID(Entity child)
    {
        String referenceName = child.getPropertyValue("$typeReferencedElement");
        if (referenceName != null)
        {
            Entity reference = (Entity)com.togethersoft.modules.project.language.model.UniqueNameService.Util
                    .getService(child.getModel()).findElementByPersistentName(referenceName);
            if (reference != null)
            {
                return getID(reference);
            }
        }
        return null;
    }

}
