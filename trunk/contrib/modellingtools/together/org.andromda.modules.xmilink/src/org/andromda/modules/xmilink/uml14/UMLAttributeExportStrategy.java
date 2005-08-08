package org.andromda.modules.xmilink.uml14;

import java.util.StringTokenizer;

import org.andromda.modules.xmilink.ExportContext;
import org.andromda.modules.xmilink.ExportStrategyFactory;

import com.togethersoft.openapi.model.elements.Entity;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 22.09.2004
 */
public class UMLAttributeExportStrategy
        extends UMLEntityExportStrategy
{

    // the type of the attribute
    private String type;

    private String multiplicity;

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("Attribute",
                UMLAttributeExportStrategy.class);
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
        this.multiplicity = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#getEntityName()
     */
    protected String getEntityName(Entity entity)
    {
        return "UML:Attribute";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#doExportProperty(java.lang.String)
     */
    protected boolean doExportProperty(String name, String value)
    {
        if (name.equalsIgnoreCase("$type"))
        {
            this.type = value;
            return false;
        }
        else if (name.equalsIgnoreCase("multiplicity"))
        {
            this.multiplicity = value;
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

        // export the multiplicity
        if (this.multiplicity != null)
        {
            // <UML:StructuralFeature.multiplicity>
            ExportContext.getWriter().writeOpeningElement("UML:StructuralFeature.multiplicity");

            // <UML:Multiplicity xmi.id = 'sm$1d2b9b7:101d5b30da8:-7ff3'>
            ExportContext.getWriter().writeOpeningElement("UML:Multiplicity");

            // <UML:Multiplicity.range>
            ExportContext.getWriter().writeOpeningElement("UML:Multiplicity.range");

            // <UML:MultiplicityRange xmi.id = 'sm$1d2b9b7:101d5b30da8:-7ff2'
            // lower = '0' upper = '1'/>
            ExportContext.getWriter().writeOpeningElementStart("UML:MultiplicityRange");

            StringTokenizer strTok = new StringTokenizer(this.multiplicity, "..");
            String multiplicityLowerBound = strTok.nextToken();
            String multiplicityUpperBound = strTok.nextToken();
            ExportContext.getWriter().writeProperty("lower", multiplicityLowerBound);
            ExportContext.getWriter().writeProperty("upper", multiplicityUpperBound);
            ExportContext.getWriter().writeOpeningElementEnd(true);

            // </UML:Multiplicity.range>
            ExportContext.getWriter().writeClosingElement("UML:Multiplicity.range");

            // </UML:Multiplicity>
            ExportContext.getWriter().writeClosingElement("UML:Multiplicity");

            // </UML:StructuralFeature.multiplicity>
            ExportContext.getWriter().writeClosingElement("UML:StructuralFeature.multiplicity");
        }

        exportTaggedValues();

        // export the type
        if (this.type != null)
        {

            // <UML:StructuralFeature.type>
            ExportContext.getWriter().writeOpeningElement("UML:StructuralFeature.type");

            // <UML:Class xmi.idref="sm$c5aa00:fe48388b81:-7fa9"/>
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

            // </UML:StructuralFeature.type>
            ExportContext.getWriter().writeClosingElement("UML:StructuralFeature.type");
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
