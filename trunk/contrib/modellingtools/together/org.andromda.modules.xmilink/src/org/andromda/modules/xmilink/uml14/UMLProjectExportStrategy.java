package org.andromda.modules.xmilink.uml14;

import java.util.Date;

import org.andromda.modules.xmilink.BaseExportStrategy;
import org.andromda.modules.xmilink.ExportContext;
import org.andromda.modules.xmilink.ExportStrategyFactory;

import com.togethersoft.openapi.model.elements.Entity;
import com.togethersoft.openapi.model.elements.Property;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 21.09.2004
 */
public class UMLProjectExportStrategy
        extends BaseExportStrategy
{

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("Project",
                UMLProjectExportStrategy.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#exportEntityPrologue(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityPrologue(Entity entity)
    {
        // <?xml version = '1.0' encoding = 'UTF-8' ?>
        ExportContext.getWriter().writeText("<?xml version = '1.0' encoding = 'UTF-8' ?>");

        // <XMI xmi.version = '1.2' xmlns:UML = 'org.omg.xmi.namespace.UML'
        // timestamp = 'Fri Aug 13 08:58:21 CEST 2004'>
        ExportContext.getWriter().writeOpeningElementStart("XMI");
        ExportContext.getWriter().writeProperty("xmi.version", "1.2");
        ExportContext.getWriter().writeProperty("xmlns:UML", "org.omg.xmi.namespace.UML");
        ExportContext.getWriter().writeProperty("timestamp", new Date().toString());
        ExportContext.getWriter().writeOpeningElementEnd(false);

        // <XMI.header>
        ExportContext.getWriter().writeOpeningElementStart("XMI.header");
        ExportContext.getWriter().writeOpeningElementEnd(false);

        // <XMI.documentation>
        ExportContext.getWriter().writeOpeningElementStart("XMI.documentation");
        ExportContext.getWriter().writeOpeningElementEnd(false);

        // <XMI.exporter>Lufthansa Systems XMI Writer</XMI.exporter>
        ExportContext.getWriter().writeOpeningElement("XMI.exporter");
        ExportContext.getWriter().writeText("Lufthansa Systems XMI Writer");
        ExportContext.getWriter().writeClosingElement("XMI.exporter");

        // <XMI.exporterVersion>1.0</XMI.exporterVersion>
        ExportContext.getWriter().writeOpeningElement("XMI.exporterVersion");
        ExportContext.getWriter().writeText("1.0");
        ExportContext.getWriter().writeClosingElement("XMI.exporterVersion");

        // </XMI.documentation>
        ExportContext.getWriter().writeClosingElement("XMI.documentation");
        // </XMI.header>
        ExportContext.getWriter().writeClosingElement("XMI.header");

        // <XMI.content>
        ExportContext.getWriter().writeOpeningElement("XMI.content");
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
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#exportEntityEpilogue(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityEpilogue(Entity entity)
    {
        // </XMI.content>
        ExportContext.getWriter().writeClosingElement("XMI.content");

        // </XMI>
        ExportContext.getWriter().writeClosingElement("XMI");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#exportEntityPrologueEnd(com.togethersoft.openapi.model.elements.Entity)
     */
    protected void exportEntityPrologueEnd(Entity entity)
    {
        // everything has been done in exportEntityPrologueStart!
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#doExportChildNodes()
     */
    protected boolean doExportChildNodes()
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#doExportDependencies()
     */
    protected boolean doExportDependencies()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#doExportProperty(java.lang.String)
     */
    protected boolean doExportProperty(String name, String value)
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#exportProperty(com.togethersoft.openapi.model.elements.Property)
     */
    protected void exportProperty(Property property)
    {
        // do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.BaseExportStrategy#doExportDependencyLink(com.togethersoft.openapi.model.elements.Entity)
     */
    protected boolean doExportDependencyLink(Entity link)
    {
        // REFACTOR
        return false;
    }
}
