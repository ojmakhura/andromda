package org.andromda.jdbcmetadata;

import java.util.HashMap;
import java.util.Map;
import org.andromda.core.mapping.Mappings;
import org.andromda.metafacades.uml.TypeMappings;

/**
 * <p>
 * Create a Java expression based on the DB SQL expression. Takes into
 * account the datatype, size, nullability (wrapped/primitive), and other factors.
 * </p>
 *
 * @author Bob Fields
 */
public class ExpressionTypeFinder
{
    private static final Map<String, String> dbToJavaExpressionMap = new HashMap<String, String>();

    /**
     * Stores the schema types to model type mappings.
     */
    private static String expressionMappingsUri = null;
    private static String wrapperMappingsUri = null;
    private static Mappings expressionMappings = null;
    private static Mappings wrapperMappings = null;

    /**
     * Sets the <code>mappingsUri</code> which is the URI to the sql types to
     * model type mappings.
     *
     * @param expressionMappingsUri The typeMappings to set.
     * @param wrapperMappingsUri The URI for the wrapper mappings xml file
     */
    public static void setExpressionMappings(String expressionMappingsUri, String wrapperMappingsUri)
    {
        // TODO: Load from classpath dependency, or bundle resources with this jar
        if (ExpressionTypeFinder.wrapperMappingsUri == null)
        {
            ExpressionTypeFinder.wrapperMappingsUri = "file:${basedir}/../mappings/UML2WrapperMappings.xml";
        }
        if (ExpressionTypeFinder.expressionMappingsUri == null)
        {
            ExpressionTypeFinder.expressionMappingsUri = "file:${basedir}/../mappings/DBtoJavaExpressionMappings.xml";
        }
        if (ExpressionTypeFinder.wrapperMappingsUri != null && ExpressionTypeFinder.expressionMappingsUri != null)
        {
            try
            {
                ExpressionTypeFinder.expressionMappings = Mappings.getInstance(ExpressionTypeFinder.expressionMappingsUri);
                ExpressionTypeFinder.wrapperMappings = Mappings.getInstance(ExpressionTypeFinder.wrapperMappingsUri);
            }
            catch (final Throwable throwable)
            {
                throw new RuntimeException(throwable);
            }
        }
    }

    /**
     * Sets the <code>mappingsUri</code> which is the URI to the sql types to
     * model type mappings.
     * @param dbExpression The SQL expression to be mapped to a Java expression
     * @param datatype The SQL datatype from the DBMS
     * @param nullable If the SQL column is nullable (return wrapper types)
     * @param size Number of digits or characters in the datatype, 1 to 38 for numeric
     * @param identifier If the column is part of the table primary key
     * @param unique If unique, umlDatatype will be nullable for findBy queries
     * @return umlType String
     */
    public static String getJavaExpression(String dbExpression, String datatype, boolean nullable, int size,
        boolean identifier, boolean unique)
    {
        // We expect this to be set before calling this method
        if (ExpressionTypeFinder.expressionMappings == null ||
                ExpressionTypeFinder.wrapperMappings == null)
        {
            ExpressionTypeFinder.setExpressionMappings(null, null);
        }
        if (ExpressionTypeFinder.expressionMappings.containsFrom(dbExpression))
        {
            dbExpression = ExpressionTypeFinder.expressionMappings.getTo(dbExpression);
        }
        // So that it can be used in findBy criteria queries with nullable values
        if (nullable || identifier || unique)
        {
            dbExpression = ExpressionTypeFinder.wrapperMappings.getTo(dbExpression);
        }
        String umlType = dbExpression;
        // Subset of UMLMetafacadeUtils for datatypes only
        // TODO If wrapped change to XXX.valueOf(expression)
        // TODO If primitive change to primitive expression
        return umlType;
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
        return mappings;
    }

    /*
     * Initialize the <code>jdbcTypes</code> Map.
    static
    {
        ExpressionTypeFinder.wrapperMappingsUri = "file:C:/Workspaces/A34/andromda342/andromda-etc/mappings/UML2WrapperMappings.xml";
        ExpressionTypeFinder.expressionMappingsUri = "file:C:/Workspaces/A34/andromda342/andromda-etc/mappings/DBtoJavaExpressionMappings.xml";
        try
        {
            ExpressionTypeFinder.expressionMappings = Mappings.getInstance(expressionMappingsUri);
            ExpressionTypeFinder.wrapperMappings = Mappings.getInstance(wrapperMappingsUri);
        }
        catch (final Throwable throwable)
        {
            throw new RuntimeException(throwable);
        }
    }
     */

    /**
     * Finds the cached java expression from the DB expression.
     *
     * @param jdbcType the JDBC type to find.
     * @return the JDBC type name.
     */
    public static String find(int jdbcType)
    {
        return dbToJavaExpressionMap.get(Integer.valueOf(jdbcType));
    }
}
