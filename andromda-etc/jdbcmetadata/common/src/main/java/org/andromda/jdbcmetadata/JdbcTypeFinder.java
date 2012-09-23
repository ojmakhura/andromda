package org.andromda.jdbcmetadata;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import org.andromda.core.mapping.Mappings;
import org.andromda.metafacades.uml.TypeMappings;
import org.apache.log4j.Logger;

/**
 * <p>
 * Provides the ability to find a <code>java.sql.Types</code> field name based
 * on an int value. Maps from a JDBC SQL datatype to a UML datatype, taking into
 * account the nullability, size, precision, and scale.
 * </p>
 *
 * @author Bob Fields
 */
public class JdbcTypeFinder
{
    private static final Logger LOGGER = Logger.getLogger(JdbcTypeFinder.class);
    private static final Map<Object, String> jdbcTypes = new HashMap<Object, String>();

    /**
     * Stores the schema types to model type mappings.
     */
    private Mappings typeMappings = null;
    private Mappings wrapperMappings = null;

    /**
     * Sets the <code>mappingsUri</code> which is the URI to the sql types to
     * model type mappings.
     *
     * @param typeMappingsUri The typeMappings to set.
     * @param wrapperMappingsUri The URI for the wrapper mappings xml file
     */
    public void setTypeMappings(String typeMappingsUri, String wrapperMappingsUri)
    {
        try
        {
            this.typeMappings = Mappings.getInstance(typeMappingsUri);
            this.wrapperMappings = Mappings.getInstance(wrapperMappingsUri);
        }
        catch (final Throwable throwable)
        {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * Sets the <code>mappingsUri</code> which is the URI to the sql types to
     * model type mappings.
     * @param jdbcType The SQL datatype from the DBMS
     * @param nullable If the SQL column is nullable (return wrapper types)
     * @param size Number of digits or characters in the datatype, 1 to 38 for numeric
     * @param scale Number of decimal places in the datatype -84 to +127
     * @param identifier If the column is part of the table primary key
     * @param unique If unique, umlDatatype will be nullable for findBy queries
     * @return umlType String
     */
    public String getUmlType(String jdbcType, boolean nullable, int size, Integer scale,
        boolean identifier, boolean unique)
    {
        String umlType = jdbcType;
        if (isNumeric(jdbcType) && scale != null)
        {
            // Nothing to the right of the decimal point
            if (scale.intValue() < 1)
            {
                // Change from decimal to integer type based on size
                if (size < 4)
                {
                    jdbcType = "TINYINT"; // byte
                }
                else if (size < 6)
                {
                    jdbcType = "SMALLINT"; // short
                }
                else if (size < 11)
                {
                    jdbcType = "INT"; // int
                }
                else //if (size < 20)
                {
                    jdbcType = "BIGINT"; // long
                }
                // else keep existing type, i.e. DECIMAL = BigDecimal
            }
            else if (scale.intValue()==2)
            {
                // Two decimal places - assume Money datatype
                jdbcType = "MONEY"; // long
            }
        }
        // Get back Char instead of String datatype
        else if (jdbcType.startsWith("CHAR"))
        {
            if (size == 1)
            {
                jdbcType += "(1)";
            }
        }
        if (this.typeMappings.containsFrom(jdbcType))
        {
            umlType = this.typeMappings.getTo(jdbcType);
        }
        // So that it can be used in findBy criteria queries with nullable values
        if (nullable || identifier || unique)
        {
            umlType = this.wrapperMappings.getTo(umlType);
        }
        LOGGER.info("getUmlType umlType=" + umlType + " jdbcType=" + jdbcType + " nullable=" + nullable + " size=" + size + " scale=" + scale
                + " identifier=" + identifier + " unique=" + unique);
        return umlType;
    }

    /**
     * @param jdbcType
     * @return true if numeric
     */
    public static boolean isNumeric(String jdbcType)
    {
        boolean numeric = false;
        if (jdbcType.startsWith("NUM") || jdbcType.startsWith("TINYINT") ||
            jdbcType.startsWith("INT") || jdbcType.startsWith("SMALLINT") ||
            jdbcType.startsWith("MEDIUMINT") || jdbcType.startsWith("SIGNED") ||
            jdbcType.startsWith("BIGINT") || jdbcType.startsWith("IDENTITY") ||
            jdbcType.startsWith("DEC") || jdbcType.startsWith("DOUBLE") ||
            jdbcType.startsWith("FLOAT") || jdbcType.startsWith("REAL")
            )
        {
            numeric = true;
        }
        return numeric;
    }

    /**
     * Gets the mappings from primitive types to wrapper types. Some languages
     * have primitives (i.e., Java) and some languages don't, so therefore this
     * property is optional.
     * @param mappingsUri
     *
     * @return the wrapper mappings
     */
    protected TypeMappings getWrapperMappings(String mappingsUri)
    {
        TypeMappings mappings = TypeMappings.getInstance(mappingsUri);
        /*final String propertyName = "wrapperMappingsUri";
        final Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        if (property instanceof String)
        {
            final String uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                this.setProperty(
                    propertyName,
                    mappings);
            }
            catch (final Exception ex)
            {
                final String errMsg = "Error getting '" + propertyName + "' --> '" + uri + '\'';
                ClassifierFacadeLogicImpl.LOGGER.error(
                    errMsg, ex);

                // don't throw the exception
            }
        }
        else
        {
            mappings = (TypeMappings)property;
        }*/
        return mappings;
    }

    /**
     * Initialize the <code>jdbcTypes</code> Map.
     */
    static
    {
        try
        {
            Field[] fields = Types.class.getFields();
            int fieldsNum = fields.length;
            Field field;
            for (int ctr = 0; ctr < fieldsNum; ctr++)
            {
                field = fields[ctr];
                jdbcTypes.put(
                    field.get(null),
                    field.getName());
            }
        }
        catch (Throwable th)
        {
            throw new RuntimeException(th);
        }
    }

    /**
     * Finds the name of the <code>jdbcType</code> passed in, or null if there
     * is no type in the java.sql.Types class matching the given
     * <code>jdbcType</code>.
     *
     * @param jdbcType the JDBC type to find.
     * @return the JDBC type name.
     */
    public static String find(int jdbcType)
    {
        return jdbcTypes.get(Integer.valueOf(jdbcType));
    }
}
