package org.andromda.modules.xmilink.uml14;

import org.andromda.modules.xmilink.ExportContext;
import org.andromda.modules.xmilink.ExportStrategyFactory;

import com.togethersoft.openapi.model.elements.Entity;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 15.10.2004
 */
public class UMLOperationExportStrategy
        extends UMLEntityExportStrategy
{

    // the return type of the operation
    private String returnType;

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("Operation",
                UMLOperationExportStrategy.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#reset()
     */
    protected void reset()
    {
        super.reset();
        this.returnType = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#getEntityName()
     */
    protected String getEntityName()
    {
        return "UML:Operation";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#doExportProperty(java.lang.String,
     *      java.lang.String)
     */
    protected boolean doExportProperty(String name, String value)
    {
        if (name.equalsIgnoreCase("$returnType"))
        {
            this.returnType = value;
            return false;
        }
        else
        {
            return super.doExportProperty(name, value);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#exportEntityBody(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityBody(Entity entity)
    {
        writeStereotype();
        exportTaggedValues();

        // export the parameters / return type
        boolean hasChildren = entity.children().hasMoreElements();
        boolean hasReturnType = returnType != null;

        boolean writeChildren = hasChildren || hasReturnType;
        if (writeChildren)
        {
            ExportContext.getWriter().writeOpeningElement("UML:BehavioralFeature.parameter");
        }

        // export return type:
        if (hasReturnType)
        {
            exportReturnType(entity);
        }

        // export parameters:
        super.exportEntityBody(entity);

        if (writeChildren)
        {
            ExportContext.getWriter().writeClosingElement("UML:BehavioralFeature.parameter");
        }
    }

    /**
     * @param entity
     *            TODO
     * 
     */
    private void exportReturnType(Entity entity)
    {
        // <UML:Parameter xmi.id = 'sm$c5aa00:fe48388b81:-7edc' name = 'param_4'
        // isSpecification = 'false'
        // kind = 'return'>
        ExportContext.getWriter().writeOpeningElementStart("UML:Parameter");
        ExportContext.getWriter().writeProperty("kind", "return");
        ExportContext.getWriter().writeProperty("name", "return");
        ExportContext.getWriter().writeOpeningElementEnd(false);

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
            ExportContext.getWriter().writeProperty("name", returnType);
        }
        ExportContext.getWriter().writeOpeningElementEnd(true);

        // </UML:Parameter.type>
        ExportContext.getWriter().writeClosingElement("UML:Parameter.type");

        // </UML:Parameter>
        ExportContext.getWriter().writeClosingElement("UML:Parameter");
    }

    private String getDatatypeRefID(Entity child)
    {
        String referenceName = child.getPropertyValue("$returnTypeReferencedElement");
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
