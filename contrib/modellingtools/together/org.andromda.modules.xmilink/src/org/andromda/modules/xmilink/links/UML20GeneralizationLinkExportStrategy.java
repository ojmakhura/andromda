package org.andromda.modules.xmilink.links;

import org.andromda.modules.xmilink.ExportContext;
import org.andromda.modules.xmilink.ExportStrategyFactory;
import org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy;

import com.togethersoft.openapi.model.elements.Entity;
import com.togethersoft.openapi.model.elements.UniqueName;

/**
 * TODO Specify purpose, please. REFACTOR Introduce superclass for links. Pay
 * attention to exportParticipant()!
 *
 * @author Peter Friese
 * @version 1.0
 * @since 01.11.2004
 */
public class UML20GeneralizationLinkExportStrategy
        extends UMLEntityExportStrategy
{

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("GeneralizationLink",
                UML20GeneralizationLinkExportStrategy.class);
        ExportStrategyFactory.getInstance().registerStrategy("Generalization20",
                UML20GeneralizationLinkExportStrategy.class);
        ExportStrategyFactory.getInstance().registerStrategy("ImplementationLink",
                UML20GeneralizationLinkExportStrategy.class);
        ExportStrategyFactory.getInstance().registerStrategy("Implementation20",
                UML20GeneralizationLinkExportStrategy.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#getEntityName()
     */
    protected String getEntityName(Entity entity)
    {
        return "UML:Generalization";
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

        // generalization child
        Entity child = entity.getSource();
        exportParticipant(child, "child");

        // generalization parent
        Entity parent = entity.getDestination();
        exportParticipant(parent, "parent");
    }

    /**
     * @param participant
     * @param participantType
     */
    private void exportParticipant(Entity participant, String participantType)
    {
        if (participant != null)
        {
            // <UML:Generalization.child>
            ExportContext.getWriter().writeOpeningElement("UML:Generalization." + participantType);

            // <UML:Class xmi.idref = 'sm$c5aa00:fe48388b81:-7f8f'/>
            ExportContext.getWriter().writeOpeningElementStart("UML:Class");
            UniqueName uName = participant.getUniqueName();
            if (uName != null)
            {
                String id = uName.getLocation();
                ExportContext.getWriter().writeProperty("xmi.idref", id);
            }
            ExportContext.getWriter().writeOpeningElementEnd(true);

            // </UML:Generalization.client>
            ExportContext.getWriter().writeClosingElement("UML:Generalization." + participantType);
        }
    }

}
