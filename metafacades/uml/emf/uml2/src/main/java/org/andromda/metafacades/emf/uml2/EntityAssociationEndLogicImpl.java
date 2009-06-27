package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityAssociationEnd;
import org.andromda.metafacades.uml.EntityAttribute;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.EntityAssociationEnd.
 *
 * @see org.andromda.metafacades.uml.EntityAssociationEnd
 * @author Bob Fields
 */
public class EntityAssociationEndLogicImpl
    extends EntityAssociationEndLogic
{
    public EntityAssociationEndLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(EntityAssociationEndLogicImpl.class);

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected String handleGetName()
    {
        final String nameMask =
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ENTITY_PROPERTY_NAME_MASK));
        return NameMasker.mask(
            super.handleGetName(),
            nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getColumnName()
     */
    protected String handleGetColumnName()
    {
        String columnName = null;

        // prevent ClassCastException if the association isn't an Entity
        if (this.getType() instanceof Entity)
        {
            final String columnNamePrefix =
                this.isConfiguredProperty(UMLMetafacadeProperties.COLUMN_NAME_PREFIX)
                ? ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.COLUMN_NAME_PREFIX)) : null;
            columnName =
                EntityMetafacadeUtils.getSqlNameFromTaggedValue(
                    columnNamePrefix,
                    this,
                    UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN,
                    ((Entity)this.getType()).getMaxSqlNameLength(),
                    this.getForeignKeySuffix(),
                    this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
        }
        return columnName;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getForeignKeySuffix()
     */
    protected java.lang.String handleGetForeignKeySuffix()
    {
        return (String)this.getConfiguredProperty(UMLMetafacadeProperties.FOREIGN_KEY_SUFFIX);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#isForeignIdentifier()
     */
    protected boolean handleIsForeignIdentifier()
    {
        final Object value = this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_FOREIGN_IDENTIFIER);
        return value != null && Boolean.valueOf(String.valueOf(value)).booleanValue();
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getForeignKeyConstraintName()
     */
    protected java.lang.String handleGetForeignKeyConstraintName()
    {
        return EntityMetafacadeUtils.getForeignKeyConstraintName(
            (EntityAssociationEnd)THIS(),
            ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.CONSTRAINT_SUFFIX)).trim(),
            ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR)).trim(),
            ObjectUtils.toString(getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH)).trim());
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getColumnIndex()
     */
    protected java.lang.String handleGetColumnIndex()
    {
        final String index = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_INDEX);
        return index != null ? StringUtils.trimToEmpty(index) : null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getSqlType()
     */
    protected java.lang.String handleGetSqlType()
    {
        String value = null;
        if (this.getSqlMappings() != null)
        {
            EntityAttribute identifier = null;

            // we retrieve the column length from the first identifier of the
            // primary key
            // on the other side (since that should correspond to the foreign
            // key).
            if (this.getType() instanceof Entity)
            {
                final Entity type = (Entity)this.getType();
                final Collection identifiers = type.getIdentifiers();
                if (identifiers != null && !identifiers.isEmpty())
                {
                    AttributeFacade attribute = (AttributeFacade)identifiers.iterator().next();
                    if (attribute instanceof EntityAttribute)
                    {
                        identifier = (EntityAttribute)attribute;
                    }
                }
            }
            if (identifier != null && identifier.getType() != null)
            {
                String typeName = identifier.getType().getFullyQualifiedName(true);
                value = this.getSqlMappings().getTo(typeName);
                final String columnLength = identifier.getColumnLength();
                if (StringUtils.isNotEmpty(columnLength))
                {
                    value = EntityMetafacadeUtils.constructSqlTypeName(
                            value,
                            columnLength);
                }
            }
        }
        return value;
    }

    /**
     * Gets the SQL mappings that have been set for this entity attribute.
     *
     * @return the SQL Mappings instance.
     */
    public TypeMappings getSqlMappings()
    {
        final String propertyName = UMLMetafacadeProperties.SQL_MAPPINGS_URI;
        final Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        String uri;
        if (property instanceof String)
        {
            uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                this.setProperty(
                    propertyName,
                    mappings);
            }
            catch (Throwable throwable)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '" + uri + "'";
                this.logger.error(
                    errMsg,
                    throwable);

                // don't throw the exception
            }
        }
        else
        {
            mappings = (TypeMappings)property;
        }
        return mappings;
    }

    protected boolean handleIsTransient()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_TRANSIENT);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#isIdentifiersPresent()
     */
    protected boolean handleIsIdentifiersPresent() {
        return this.hasStereotype(UMLProfile.STEREOTYPE_IDENTIFIER);
    }
    
    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getUniqueGroup()
     */
    protected String handleGetUniqueGroup() {
        final String group = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_ASSOCIATION_END_UNIQUE_GROUP);
        return group != null ? StringUtils.trimToEmpty(group) : null;
    }
}