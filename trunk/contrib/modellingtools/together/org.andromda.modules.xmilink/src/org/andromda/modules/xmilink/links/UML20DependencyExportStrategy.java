package org.andromda.modules.xmilink.links;

import org.andromda.modules.xmilink.ExportContext;
import org.andromda.modules.xmilink.ExportStrategyFactory;
import org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy;

import com.togethersoft.openapi.model.elements.Entity;
import com.togethersoft.openapi.model.elements.UniqueName;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 01.11.2004
 */
public class UML20DependencyExportStrategy
        extends UMLEntityExportStrategy
{

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("Dependency20",
                UML20DependencyExportStrategy.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#getEntityName()
     */
    protected String getEntityName(Entity entity)
    {
        return "UML:Dependency";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#doExportChildNodes()
     */
    protected boolean doExportChildNodes()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#doExportDependencies()
     */
    protected boolean doExportDependencies()
    {
        return false;
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

        Entity client = entity.getSource();
        exportDependencyParticipant(client, "client");

        // supplier
        Entity supplier = entity.getDestination();
        exportDependencyParticipant(supplier, "supplier");
    }

    /**
     * @param participant
     * @param participantType
     */
    private void exportDependencyParticipant(Entity participant, String participantType)
    {
        if (participant != null)
        {
            // <UML:Dependency.client>
            ExportContext.getWriter().writeOpeningElement("UML:Dependency." + participantType);

            // <UML:Class xmi.idref = 'sm$c5aa00:fe48388b81:-7f8f'/>
            ExportContext.getWriter().writeOpeningElementStart("UML:Class");

            UniqueName uName = participant.getUniqueName();
            if (uName != null)
            {
                String id = uName.getLocation();
                ExportContext.getWriter().writeProperty("xmi.idref", id);
            }

            ExportContext.getWriter().writeOpeningElementEnd(true);

            // </UML:Dependency.client>
            ExportContext.getWriter().writeClosingElement("UML:Dependency." + participantType);
        }
    }

}
