package org.andromda.metafacades.uml14;

import org.andromda.core.mapping.Mappings;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;


/**
 *
 *
 * Metaclass facade implementation.
 *
 */
public class EntityAttributeFacadeLogicImpl
       extends EntityAttributeFacadeLogic
       implements org.andromda.metafacades.uml.EntityAttributeFacade
{
    // ---------------- constructor -------------------------------

    public EntityAttributeFacadeLogicImpl (java.lang.Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttributeFacade#getColumnName()
     */
    public String handleGetColumnName() {
        return EntityMetafacadeUtils.getSqlNameFromTaggedValue(
            this,
            UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN,
            ((EntityFacade)this.getOwner()).getMaxSqlNameLength());
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttributeFacade#getColumnLength()
     */
    public String handleGetColumnLength() {
        Object value = this.findTaggedValue(
            UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH);
        return (String)value;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttributeFacade#isIdentifier()
     */
    public boolean handleIsIdentifier() {
        return this.hasStereotype(UMLProfile.STEREOTYPE_IDENTIFIER);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttributeFacade#getSqlType()
     */
    public java.lang.String handleGetSqlType() {
        String value = null;
        if (this.getSqlMappings() != null) {
            ClassifierFacade type = this.getType();
            String typeName = type.getFullyQualifiedName(true);
            String columnLength = this.getColumnLength();
            value = this.getSqlMappings().getTo(typeName);
            if (StringUtils.isBlank(value)) {
                logger.error("ERROR! missing SQL type mapping for model type '"
                        + typeName
                        + "' --> please adjust your model or SQL type mappings '"
                        + this.getSqlMappings().getResource() + "' accordingly");
            }
            if (StringUtils.isNotEmpty(columnLength)) {
                char beginChar = '(';
                char endChar = ')';
                int beginIndex = value.indexOf(beginChar);
                int endIndex = value.indexOf(endChar);
                if (beginIndex != -1 && endIndex != -1 && endIndex > beginIndex) {
                    String replacement = value.substring(beginIndex, endIndex) + endChar;
                    value = StringUtils.replace(
                        value,
                        replacement,
                        beginChar + columnLength + endChar);
                }
            }
        }
        return value;
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAttributeFacade#getJdbcType()
     */
    public java.lang.String handleGetJdbcType() {
        String value = null;
        if (this.getJdbcMappings() != null) {
            ClassifierFacade type = this.getType();
            if (type != null) {
                String typeName = type.getFullyQualifiedName(true);
                value = this.getJdbcMappings().getTo(typeName);
                if (StringUtils.isBlank(value)) {
                    logger.error("ERROR! missing JDBC type mapping for model type '"
                            + typeName
                            + "' --> please adjust your model or JDBC type mappings '"
                            + this.getJdbcMappings().getResource() + "' accordingly");
                }
            }
        }

        return value;
    }

    /**
     * Language specific mappings property reference.
     */
    private static final String SQL_MAPPINGS_URI = "sqlMappingsUri";

    /**
     * Allows the MetafacadeFactory to populate
     * the language mappings for this model element.
     *
     * @param sqlMappingsUri the URI of the language mappings resource.
     */
    public void setSqlMappingsUri(String sqlMappingsUri) {
        try {
            this.registerConfiguredProperty(
            	SQL_MAPPINGS_URI,
                Mappings.getInstance(sqlMappingsUri));
        } catch (Throwable th) {
            String errMsg = "Error setting '"
                + SQL_MAPPINGS_URI + "' with --> '"
                + sqlMappingsUri + "'";
            logger.error(errMsg, th);
            //don't throw the exception
        }
    }

    /**
     * Gets the SQL mappings that have been set
     * for this entity attribute.
     * @return the SQL Mappings instance.
     */
    public Mappings handleGetSqlMappings() {
        return (Mappings)this.getConfiguredProperty(SQL_MAPPINGS_URI);
    }

    /**
     * JDBC type specific mappings property reference.
     */
    private static final String JDBC_MAPPINGS_URI = "jdbcMappingsUri";

    /**
     * Allows the MetafacadeFactory to populate
     * the language mappings for this model element.
     *
     * @param jdbcMappingsUri the URI of the language mappings resource.
     */
    public void setJdbcMappingsUri(String jdbcMappingsUri) {
        try {
            this.registerConfiguredProperty(
                JDBC_MAPPINGS_URI, Mappings.getInstance(jdbcMappingsUri));
        } catch (Throwable th) {
            String errMsg = "Error setting '"
                + JDBC_MAPPINGS_URI + "' --> '"
                + jdbcMappingsUri + "'";
            logger.error(errMsg, th);
            //don't throw the exception
        }
    }

    /**
     * Gets the JDBC mappings.
     */
    public Mappings handleGetJdbcMappings() {
        return (Mappings)this.getConfiguredProperty(JDBC_MAPPINGS_URI);
    }

    // ------------- relations ------------------

}
