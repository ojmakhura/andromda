package org.andromda.metafacades.emf.uml2;

import java.util.Collection;

import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityAttribute;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EntityAssociationEnd.
 *
 * @see org.andromda.metafacades.uml.EntityAssociationEnd
 */
public class EntityAssociationEndLogicImpl
    extends EntityAssociationEndLogic
{
    public EntityAssociationEndLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

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
    protected java.lang.String handleGetColumnName()
    {
        String columnName = null;

        // prevent ClassCastException if the association isn't an Entity
        if (this.getType() instanceof Entity)
        {
            columnName =
                EntityMetafacadeUtils.getSqlNameFromTaggedValue(
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
        boolean test = value != null && Boolean.valueOf(String.valueOf(value)).booleanValue();
        return test;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getForeignKeyConstraintName()
     */
    protected java.lang.String handleGetForeignKeyConstraintName()
    {
        String constraintName = null;

        final Object taggedValueObject =
            findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_FOREIGN_KEY_CONSTRAINT_NAME);
        if (taggedValueObject == null)
        {
            // we construct our own foreign key constraint name here
            final StringBuffer buffer = new StringBuffer();

            final ClassifierFacade type = getOtherEnd().getType();
            if (type instanceof Entity)
            {
                Entity entity = (Entity)type;
                buffer.append(entity.getTableName());
            }
            else
            {
                // should not happen
                buffer.append(type.getName().toUpperCase());
            }

            buffer.append(getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
            buffer.append(this.getColumnName());
            buffer.append(getConfiguredProperty(UMLMetafacadeProperties.CONSTRAINT_SUFFIX));

            constraintName = buffer.toString();
        }
        else
        {
            // use the tagged value
            constraintName = taggedValueObject.toString();
        }

        // we take into consideration the maximum length allowed
        final String maxLengthString = (String)getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH);
        final Short maxLength = Short.valueOf(maxLengthString);
        return EntityMetafacadeUtils.ensureMaximumNameLength(
            constraintName,
            maxLength);
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

            // we retrieve the column length from the first identifier of the primary key 
            // on the other side (since that should correspond to the foreign key).         
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
        String uri = null;
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
                logger.error(
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
}