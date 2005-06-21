package org.andromda.modules.xmilink.uml14;

import org.andromda.modules.xmilink.ExportContext;
import org.andromda.modules.xmilink.ExportStrategyFactory;
import org.andromda.modules.xmilink.Logger;

import com.togethersoft.openapi.model.elements.Entity;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 17.09.2004
 */
public class UMLModelExportStrategy
        extends UMLEntityExportStrategy
{

    /** ID for the documentation tagged value. */
    public static final String ID_DOCUMENTATION = "$andromda$documentation";

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("ModelPart",
                UMLModelExportStrategy.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#getEntityName()
     */
    protected String getEntityName()
    {
        return "UML:Model";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#doExportEntity(com.togethersoft.openapi.model.elements.Entity)
     */
    protected boolean doExportEntity(Entity entity)
    {
        String name = entity.getPropertyValue("$name");
        Logger.info("The name of the model entity is: " + name);
        if (name.equalsIgnoreCase("Model"))
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
     * @see org.andromda.modules.xmilink.BaseExportStrategy#exportEntityContents(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityContents(Entity entity)
    {
        // do nothing
        // TODO refactor!
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
     * @see org.andromda.modules.xmilink.BaseExportStrategy#exportEntityBody(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityBody(Entity entity)
    {
        boolean hasChildren = entity.children().hasMoreElements();
        if (hasChildren)
        {
            ExportContext.getWriter().writeOpeningElement("UML:Namespace.ownedElement");
        }
        super.exportEntityBody(entity);
        if (hasChildren)
        {
            ExportContext.getWriter().writeClosingElement("UML:Namespace.ownedElement");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#exportEntityEpilogue(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityEpilogue(Entity entity)
    {
        // // write documentation definition
        //		
        // // <UML:TagDefinition xmi.id = 'sm$19ecd80:1001cda5985:-7efb' name =
        // 'documentation'
        // // isSpecification = 'false' tagType = 'String'>
        // ExportContext.getWriter().writeOpeningElementStart("UML:TagDefinition");
        // ExportContext.getWriter().writeProperty("xmi.id",
        // UMLModelExportStrategy.ID_DOCUMENTATION);
        // ExportContext.getWriter().writeProperty("name", "documentation");
        // ExportContext.getWriter().writeProperty("tagType", "String");
        // ExportContext.getWriter().writeOpeningElementEnd(false);
        //      
        // // <UML:TagDefinition.multiplicity>
        // // <UML:Multiplicity xmi.id = 'sm$19ecd80:1001cda5985:-7efa'>
        // // <UML:Multiplicity.range>
        // // <UML:MultiplicityRange xmi.id = 'sm$19ecd80:1001cda5985:-7ef9'
        // lower = '1'
        // // upper = '1'/>
        // // </UML:Multiplicity.range>
        // // </UML:Multiplicity>
        // // </UML:TagDefinition.multiplicity>
        //		
        // // </UML:TagDefinition>
        // ExportContext.getWriter().writeClosingElement("UML:TagDefinition");

        super.exportEntityEpilogue(entity);
    }
}
