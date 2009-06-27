package org.andromda.schema2xmi;

import java.lang.reflect.Field;

import java.sql.Types;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * Provides the ability to find a <code>java.sql.Types</code> field name based
 * on an int value.
 * </p>
 *
 * @author Chad Brandon
 */
public class JdbcTypeFinder
{
    private static final Map jdbcTypes = new HashMap();

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
            throw new SchemaTransformerException(th);
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
        return (String)jdbcTypes.get(new Integer(jdbcType));
    }
}