package org.andromda.metafacades.uml14;

import org.andromda.core.mapping.Mappings;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.EntityMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;

/**
 * Metaclass facade implementation.
 */
public class EntityAttributeFacadeLogicImpl
    extends EntityAttributeFacadeLogic
    implements org.andromda.metafacades.uml.EntityAttributeFacade
{
    // ---------------- constructor -------------------------------

    public EntityAttributeFacadeLogicImpl(
        java.lang.Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttributeFacade#getColumnName()
     */
    public String handleGetColumnName()
    {
        return EntityMetafacadeUtils.getSqlNameFromTaggedValue(
            this,
            UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN,
            ((EntityFacade)this.getOwner()).getMaxSqlNameLength());
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttributeFacade#getColumnLength()
     */
    public String handleGetColumnLength()
    {
        Object value = this
            .findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH);
        return (String)value;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttributeFacade#isIdentifier()
     */
    public boolean handleIsIdentifier()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_IDENTIFIER);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttributeFacade#getSqlType()
     */
    public java.lang.String handleGetSqlType()
    {
        String value = null;
        if (this.getSqlMappings() != null)
        {
            ClassifierFacade type = this.getType();
            String typeName = type.getFullyQualifiedName(true);
            String columnLength = this.getColumnLength();
            value = this.getSqlMappings().getTo(typeName);
            if (StringUtils.isBlank(value))
            {
                logger.error("ERROR! missing SQL type mapping for model type '"
                    + typeName
                    + "' --> please adjust your model or SQL type mappings '"
                    + this.getSqlMappings().getResource() + "' accordingly");
            }
            if (StringUtils.isNotEmpty(columnLength))
            {
                char beginChar = '(';
                char endChar = ')';
                int beginIndex = value.indexOf(beginChar);
                int endIndex = value.indexOf(endChar);
                if (beginIndex != -1 && endIndex != -1 && endIndex > beginIndex)
                {
                    String replacement = value.substring(beginIndex, endIndex)
                        + endChar;
                    value = StringUtils.replace(value, replacement, beginChar
                        + columnLength + endChar);
                }
            }
        }
        return value;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttributeFacade#getJdbcType()
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
                if (StringUtils.isBlank(value))
                {
                    logger
                        .error("ERROR! missing JDBC type mapping for model type '"
                            + typeName
                            + "' --> please adjust your model or JDBC type mappings '"
                            + this.getJdbcMappings().getResource()
                            + "' accordingly");
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
    public Mappings handleGetSqlMappings()
    {
        return this.getMappingsProperty("sqlMappingsUri");
    }

    /**
     * Gets the JDBC mappings.
     */
    public Mappings handleGetJdbcMappings()
    {
        return this.getMappingsProperty("jdbcMappingsUri");
    }

    /**
     * Gets a Mappings instance from a property registered under the given
     * <code>propertyName</code>.
     * 
     * @param propertyName the property name to register under.
     * @return the Mappings instance.
     */
    private Mappings getMappingsProperty(final String propertyName)
    {
        Object property = this.getConfiguredProperty(propertyName);
        Mappings mappings = null;
        String uri = null;
        if (String.class.isAssignableFrom(property.getClass()))
        {
            uri = (String)property;
            try
            {
                mappings = Mappings.getInstance(uri);
                this.registerConfiguredProperty(propertyName, mappings);
            }
            catch (Throwable th)
            {
                String errMsg = "Error getting '" + propertyName + "' --> '"
                    + uri + "'";
                logger.error(errMsg, th);
                //don't throw the exception
            }
        }
        else
        {
            mappings = (Mappings)property;
        }
        return mappings;
    }

}