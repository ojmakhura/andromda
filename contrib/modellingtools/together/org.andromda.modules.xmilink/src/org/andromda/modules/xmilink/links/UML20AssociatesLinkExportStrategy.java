package org.andromda.modules.xmilink.links;

import org.andromda.modules.xmilink.ExportContext;
import org.andromda.modules.xmilink.ExportStrategyFactory;
import org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy;
import org.apache.commons.lang.StringUtils;

import com.togethersoft.openapi.model.elements.Entity;
import com.togethersoft.openapi.model.elements.UniqueName;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 01.11.2004
 */
public class UML20AssociatesLinkExportStrategy
        extends UMLEntityExportStrategy
{

    public static final int SOURCE = 0;

    public static final int TARGET = 1;

    static
    {
        ExportStrategyFactory.getInstance().registerStrategy("AssociatesLink", UML20AssociatesLinkExportStrategy.class);
    }

    private int kind;

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#getEntityName()
     */
    protected String getEntityName()
    {
        return "UML:Association";
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

        exportConnection(entity);
    }

    /**
     * @param entity
     */
    private void exportConnection(Entity entity)
    {
        // <UML:Association.connection>
        ExportContext.getWriter().writeOpeningElement("UML:Association.connection");

        // source end
        exportAssociationEnd(entity, SOURCE);

        // target end
        exportAssociationEnd(entity, TARGET);

        // </UML:Association.connection>
        ExportContext.getWriter().writeClosingElement("UML:Association.connection");
    }

    /**
     * @param kind
     * @param source
     */
    private void exportAssociationEnd(Entity entity,
        int kind)
    {
        // <UML:AssociationEnd xmi.id = 'sm$19ecd80:1003e158985:-7ebd'
        // visibility = 'public'
        // isSpecification = 'false' isNavigable = 'false' ordering =
        // 'unordered' aggregation = 'none'
        // targetScope = 'instance' changeability = 'changeable'>
        ExportContext.getWriter().writeOpeningElementStart("UML:AssociationEnd");
        // hier sollte eigentlich die ID des association ends stehen und nicht
        // die der zielklasse!
        // UniqueName uName = associationEnd.getUniqueName();
        // if (uName != null) {
        // String id = uName.getLocation();
        // ExportContext.getWriter().writeProperty("xmi.idref", id);
        // }
        exportRoleName(entity, kind);
        exportNavigability(entity, kind);
        exportAggregation(entity, kind);
        ExportContext.getWriter().writeOpeningElementEnd(false);

        exportMultiplicity(entity, kind);
        // REFACTOR This should not be done like this!
        this.kind = kind;
        exportTaggedValues();
        exportParticipant(entity, kind);

        // </UML:AssociationEnd>
        ExportContext.getWriter().writeClosingElement("UML:AssociationEnd");

    }

    /*
     * (non-Javadoc)
     *
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#doExportTaggedValue(java.lang.String,
     *      java.lang.String)
     */
    protected boolean doExportTaggedValue(String key,
        String value)
    {
        System.out.println("key: " + key);
        if (key.startsWith("@supplier."))
        {
            return (kind == TARGET);
        }
        else if (key.startsWith("@client."))
        {
            return (kind == SOURCE);
        }
        else
        {
            return super.doExportTaggedValue(key, value);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.modules.xmilink.uml14.UMLEntityExportStrategy#translateTaggedValueName(java.lang.String,
     *      java.lang.String)
     */
    protected String translateTaggedValueName(String key,
        String value)
    {
        if (key.startsWith("@supplier"))
        {
            return StringUtils.replace(key, "@supplier.", "");
        }

        if (key.startsWith("@client."))
        {
            return StringUtils.replace(key, "@client.", "");
        }

        return super.translateTaggedValueName(key, value);

    }

    /**
     * @param entity
     * @param kind
     */
    private void exportParticipant(Entity entity,
        int kind)
    {
        Entity participant = null;
        if (kind == SOURCE)
        {
            participant = entity.getSource();
        }
        else if (kind == TARGET)
        {
            participant = entity.getDestination();
        }
        exportAssociationEndParticipant(participant);
    }

    /**
     * @param entity
     * @param kind
     */
    private void exportRoleName(Entity entity,
        int kind)
    {
        String roleName = null;
        if (kind == TARGET)
        {
            if (entity.hasProperty("supplierRole"))
            {
                roleName = entity.getPropertyValue("supplierRole");
            }
        }
        else if (kind == SOURCE)
        {
            if (entity.hasProperty("clientRole"))
            {
                roleName = entity.getPropertyValue("clientRole");
            }
        }
        if (roleName != null)
        {
            ExportContext.getWriter().writeProperty("name", roleName);
        }
    }

    /**
     * @param entity
     * @param kind
     */
    private void exportNavigability(Entity entity,
        int kind)
    {
        if (kind == TARGET)
        {
            if (entity.hasProperty("clientNavigability"))
            {
                ExportContext.getWriter().writeProperty("isNavigable", "true");
            }
        }
        else if (kind == SOURCE)
        {
            if (entity.hasProperty("supplierNavigability"))
            {
                ExportContext.getWriter().writeProperty("isNavigable", "true");
            }
        }
    }

    /**
     * @param entity
     * @param kind
     */
    private void exportAggregation(Entity entity,
        int kind)
    {
        if (kind == TARGET)
        {
            if (entity.hasProperty("aggregation"))
            {
                if (entity.hasProperty("composition"))
                {
                    ExportContext.getWriter().writeProperty("aggregation", "none");
                }
                else
                {
                    ExportContext.getWriter().writeProperty("aggregation", "none");
                }
            }

        }
        else if (kind == SOURCE)
        {
            if (entity.hasProperty("aggregation"))
            {
                if (entity.hasProperty("composition"))
                {
                    ExportContext.getWriter().writeProperty("aggregation", "composite");
                }
                else
                {
                    ExportContext.getWriter().writeProperty("aggregation", "aggregate");
                }
            }
        }
    }

    /**
     * @param entity
     * @param kind
     */
    private void exportMultiplicity(Entity entity,
        int kind)
    {
        String lower = null;
        String upper = null;
        String cardinality = null;
        if (kind == TARGET)
        {
            if (entity.hasProperty("supplierCardinality"))
            {
                cardinality = entity.getPropertyValue("supplierCardinality");
            }
        }
        else if (kind == SOURCE)
        {
            if (entity.hasProperty("clientCardinality"))
            {
                cardinality = entity.getPropertyValue("clientCardinality");
            }
        }
        lower = parseCardinalityLower(cardinality);
        upper = parseCardinalityUpper(cardinality);
        exportAssociationEndMultiplicity(lower, upper);
    }

    /**
     * @param lower
     * @param upper
     */
    private void exportAssociationEndMultiplicity(String lower,
        String upper)
    {
        // <UML:AssociationEnd.multiplicity>
        ExportContext.getWriter().writeOpeningElement("UML:AssociationEnd.multiplicity");

        // <UML:Multiplicity xmi.id = 'sm$19ecd80:1003e158985:-7e9e'>
        ExportContext.getWriter().writeOpeningElement("UML:Multiplicity");

        // <UML:Multiplicity.range>
        ExportContext.getWriter().writeOpeningElement("UML:Multiplicity.range");

        // <UML:MultiplicityRange xmi.id = 'sm$19ecd80:1003e158985:-7e9d' lower
        // = '1'
        // upper = '1'/>
        ExportContext.getWriter().writeOpeningElementStart("UML:MultiplicityRange");
        ExportContext.getWriter().writeProperty("lower", lower);
        ExportContext.getWriter().writeProperty("upper", upper);
        ExportContext.getWriter().writeOpeningElementEnd(true);

        // </UML:Multiplicity.range>
        ExportContext.getWriter().writeClosingElement("UML:Multiplicity.range");

        // </UML:Multiplicity>
        ExportContext.getWriter().writeClosingElement("UML:Multiplicity");

        // </UML:AssociationEnd.multiplicity
        ExportContext.getWriter().writeClosingElement("UML:AssociationEnd.multiplicity");
    }

    /**
     * @param cardinality
     * @return
     */
    private String parseCardinalityUpper(String cardinality)
    {
        if (cardinality == null)
        {
            return "1";
        }
        else
        {
            if (cardinality.length() == 1)
            {
                return parse(cardinality);
            }
            else
            {
                return parse(cardinality.substring(cardinality.lastIndexOf('.') + 1));
            }
        }
    }

    /**
     * @param cardinality
     * @return
     */
    private String parseCardinalityLower(String cardinality)
    {
        if (cardinality == null)
        {
            return "1";
        }
        else
        {
            if (cardinality.length() == 1)
            {
                return parse(cardinality);
            }
            else
            {
                return parse(cardinality.substring(0, 1));
            }
        }
    }

    /**
     * @param string
     * @return
     */
    private String parse(String string)
    {
        if (string == null)
        {
            return "";
        }
        else
        {
            if (string.equals("*"))
            {
                return "-1";
            }
            else
            {
                return string;
            }
        }
    }

    /**
     * @param participant
     */
    private void exportAssociationEndParticipant(Entity participant)
    {
        if (participant != null)
        {
            // <UML:AssociationEnd.participant>
            ExportContext.getWriter().writeOpeningElement("UML:AssociationEnd.participant");

            // <UML:Class xmi.idref = 'sm$c5aa00:fe48388b81:-7f8f'/>
            ExportContext.getWriter().writeOpeningElementStart("UML:Class");

            UniqueName uName = participant.getUniqueName();
            if (uName != null)
            {
                String id = uName.getLocation();
                ExportContext.getWriter().writeProperty("xmi.idref", id);
            }

            ExportContext.getWriter().writeOpeningElementEnd(true);

            // </UML:AssociationEnd.participant>
            ExportContext.getWriter().writeClosingElement("UML:AssociationEnd.participant");
        }
    }

}
