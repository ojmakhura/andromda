package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;

/**
 * Metaclass facade implementation.
 */
public class EntityAttributeLogicImpl
        extends EntityAttributeLogic
{

    public EntityAttributeLogicImpl(java.lang.Object metaObject, String context)
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
        final String nameMask = String.valueOf(this.getConfiguredProperty(
                UMLMetafacadeProperties.ENTITY_PROPERTY_NAME_MASK));
        return NameMasker.mask(super.handleGetName(), nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnName()
     */
    public String handleGetColumnName()
    {
        final Short maxSqlNameLength = 
            Short.valueOf((String)this.getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH));
        return EntityMetafacadeUtils.getSqlNameFromTaggedValue(this, UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN, 
                maxSqlNameLength,this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR));
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnLength()
     */
    public String handleGetColumnLength()
    {
        Object value = this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH);
        return (String)value;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#isIdentifier()
     */
    public boolean handleIsIdentifier()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_IDENTIFIER);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#isUnique()
     */
    public boolean handleIsUnique()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_UNIQUE);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getColumnIndex()
     */
    public java.lang.String handleGetColumnIndex()
    {
        String index = (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_INDEX);
        return index != null ? StringUtils.trimToEmpty(index) : null;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getSqlType()
     */
    public java.lang.String handleGetSqlType()
    {
        String value = null;
        if (this.getSqlMappings() != null)
        {
            ClassifierFacade type = this.getType();
            if (type != null)
            {
                String typeName = type.getFullyQualifiedName(true);
                // if its an enumeration, the sql type is the literal type
                if (type.isEnumeration())
                {
                    ClassifierFacade literalType = ((EnumerationFacade)type).getLiteralType();
                    if (literalType != null)
                    {
                        typeName = literalType.getFullyQualifiedName(true);
                    }
                }
                value = this.getSqlMappings().getTo(typeName);
                String columnLength = this.getColumnLength();
                if (StringUtils.isNotEmpty(columnLength))
                {
                    value = EntityMetafacadeUtils.constructSqlTypeName(value, columnLength);
                }
            }
        }
        return value;
    }
    

    /**
     * @see org.andromda.metafacades.uml.EntityAttribute#getJdbcType()
     */
    public java.lang.String handleGetJdbcType()
    {
        String value = null;
        if (this.getJdbcMappings() != null)
        {
            ClassifierFacade type = this.getType();
            if (type != null)
            {
                String typeName = type.getFullyQualifiedName(true);
                value = this.getJdbcMappings().getTo(typeName);
            }
        }

        return value;
    }

    /**
     * Gets the SQL mappings that have been set for this entity attribute.
     *
     * @return the SQL Mappings instance.
     */
    public TypeMappings handleGetSqlMappings()
    {
        return this.getMappingsProperty(UMLMetafacadeProperties.SQL_MAPPINGS_URI);
    }

    /**
     * Gets the JDBC mappings.
     */
    public TypeMappings handleGetJdbcMappings()
    {
        return this.getMappingsProperty(UMLMetafacadeProperties.JDBC_MAPPINGS_URI);
    }

    /**
     * Gets a Mappings instance from a property registered under the given <code>propertyName</code>.
     *
     * @param propertyName the property name to register under.
     * @return the Mappings instance.
     */
    private TypeMappings getMappingsProperty(final String propertyName)
    {
        Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        String uri = null;
        if (property instanceof String)
        {
            uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                this.setProperty(propertyName, mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '" + uri + "'";
                logger.error(errMsg, th);
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