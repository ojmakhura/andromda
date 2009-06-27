package org.andromda.metafacades.emf.uml22;

import java.util.Collection;
import java.util.Iterator;
import org.andromda.core.metafacade.MetafacadeImplsException;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.EntityAssociation.
 *
 * @see org.andromda.metafacades.uml.EntityAssociation
 */
public class EntityAssociationLogicImpl
    extends EntityAssociationLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public EntityAssociationLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociation#getTableName()
     */
    @Override
    protected String handleGetTableName()
    {
        String tableName = null;
        final Collection<AssociationEndFacade> ends = this.getAssociationEnds();
        if (ends != null && !ends.isEmpty())
        {
            final AssociationEndFacade end = (AssociationEndFacade)ends.iterator().next();
            final ClassifierFacade type = end.getType();
            if (type != null && end.isMany2Many())
            {
                // - prevent ClassCastException if the association isn't an Entity
                if (type instanceof Entity)
                {
                    final String prefixProperty = UMLMetafacadeProperties.TABLE_NAME_PREFIX;
                    final String tableNamePrefix =
                        this.isConfiguredProperty(prefixProperty)
                        ? ObjectUtils.toString(this.getConfiguredProperty(prefixProperty)) : null;
                    tableName =
                        EntityMetafacadeUtils.getSqlNameFromTaggedValue(
                            tableNamePrefix,
                            this,
                            UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,
                            ((Entity)type).getMaxSqlNameLength(),
                            this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
                }
            }
        }
        return tableName;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociation#getSchema()
     */
    // TODO: Duplicated method from ancestor
    @Override
    protected String handleGetSchema()
    {
        String schemaName = ObjectUtils.toString(this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_SCHEMA));
        if (StringUtils.isBlank(schemaName))
        {
            schemaName = ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.SCHEMA_NAME));
        }
        return schemaName;
    }

    /**
     * It is an entity association if both ends are entities (have the entity
     * stereotype
     */
    @Override
    protected boolean handleIsEntityAssociation()
    {
        // TODO: There may be a better implementation
        // But it has to be tested (it may cause a stack overflow.
        // return (getAssociationEndA().getType() instanceof Entity) &&
        // (getAssociationEndB().getType() instanceof Entity);
        if (this.metaObject == null || !(this.metaObject instanceof Association))
        {
            throw new MetafacadeImplsException("Incorrect metafacade mapping for " + this.toString());
        }
        boolean isEntityAssociation = true;
        for (Iterator<Property> ends = ((Association)this.metaObject).getMemberEnds().iterator(); ends.hasNext();)
        {
            final Property prop = (Property)ends.next();
            final Type propertyType = prop.getType();
            if (propertyType == null || !UmlUtilities.containsStereotype(
                    propertyType,
                    UMLProfile.STEREOTYPE_ENTITY))
            {
                isEntityAssociation = false;
            }
        }
        return isEntityAssociation;
    }
}
