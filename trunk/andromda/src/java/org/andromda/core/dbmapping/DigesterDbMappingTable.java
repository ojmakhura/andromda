package org.andromda.core.dbmapping;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.andromda.core.common.DbMappingTable;
import org.andromda.core.common.RepositoryReadException;

import org.apache.commons.digester.Digester;


/**
 * <p>Implements DbMappingTable by using a Jakarta Commons Digester to
 * read the <code>TypeMappings.xml</code> file.</p>
 * 
 * @author Stefan Kuehnel
 * @author Matthias Bohlen
 * @author <a href="http://www.amowers.com">Anthony Mowers</a>
 *
 * @see <a href="http://jakarta.apache.org/commons/digester/">Jakarta Commons Digester</a>
 */
public class DigesterDbMappingTable
    implements DbMappingTable 
{
    private Map map = Collections.EMPTY_MAP;
    private Digester digester = new Digester();

    public DigesterDbMappingTable()
    {
        digester.addObjectCreate("mappings", Mappings.class);
        digester.addSetProperties("mappings", "database", "database");
        digester.addObjectCreate("mappings/mapping", Mapping.class);
        digester.addCallMethod("mappings/mapping/type", "addJavaType", 0);
        digester.addObjectCreate("mappings/mapping/jdbc-type", JdbcType.class);
        digester.addSetProperties("mappings/mapping/jdbc-type", "name", "name");
        digester.addSetNext("mappings/mapping/jdbc-type", "setJdbcType");
        digester.addObjectCreate("mappings/mapping/sql-type", SqlType.class);
        digester.addSetProperties("mappings/mapping/sql-type",
                                  new String[] { "pattern", "default-length" },
                                  new String[] { "pattern", "defaultLength" });
        digester.addSetNext("mappings/mapping/sql-type", "setSqlType");
        digester.addSetNext("mappings/mapping", "addMapping");
    }

    /**
     * @see org.andromda.core.common.DbMappingTable#read(File)
     */
    public void read(File mappingsFile)
        throws RepositoryReadException, IOException 
    {
	try {
            //ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            //ClassLoader classLoader = getClass().getClassLoader();
            //digester.setClassLoader(classLoader);
            Mappings mappings = (Mappings)digester.parse(mappingsFile);
            initialize(mappings);
	} catch (Exception ex) {
            throw new RepositoryReadException(
                "Error in TypeMappings file: " + mappingsFile, ex);
	}
    }

    /**
     * @see org.andromda.core.common.DbMappingTable#getJDBCType(String)
     */
    public String getJDBCType(String javaType)
    {
        Mapping m = (Mapping) map.get(javaType);
        if (null == m)
        {
            return "** MISSING JDBC type mapping for " + javaType;
        }

        return m.getJdbcType().getName();
    }

    /**
     * @see org.andromda.core.common.DbMappingTable#getSQLType(String, String)
     */
    public String getSQLType(String javaType, String desiredFieldLength) 
    {
        Mapping m = (Mapping) map.get(javaType);
        if (null == m)
        {
            return "** MISSING SQL type mapping for " + javaType;
        }
        SqlType sqlType = m.getSqlType();

        String pattern = sqlType.getPattern();

        String fieldLength =
            (null == desiredFieldLength)
                || ("".equals(desiredFieldLength))
                    ? sqlType.getDefaultLength()
                    : desiredFieldLength;
        Object[] arguments = { fieldLength };

        return MessageFormat.format(pattern, arguments);
    }

    private void initialize(Mappings mappings)
    {
        map = new HashMap();
        
        for (Iterator i = mappings.getMappings().iterator(); i.hasNext(); )
        {
            Mapping mapping = (Mapping)i.next();
            for (Iterator j = mapping.getJavaTypes().iterator(); j.hasNext(); )
            {
                String type = (String)j.next();
                if (map.containsKey(type)) {
                    System.out.println("WARNING: Duplicate mappings for Java type '"+type+"'. Using new value.");
                }
                map.put(type, mapping);
            }
        }
    }

    public static class Mappings {
        private List mappings = new ArrayList();
        private String database;

        /**
         * Gets the list of mappings.
         *
         * @return a List with all mappings
         */
        public List getMappings() {
            return mappings;
        }

        /**
         * Adds the given mapping to our list.
         *
         * @param m the new mapping to add
         */
        public void addMapping(Mapping m) {
            mappings.add(m);
        }

        /**
         * Gets the name of the database this mappings are for.
         *
         * @return a String with the name of the database this mappings are for
         */
        public String getDatabase() {
            return database;
        }

        /**
         * Sets the name of the database this mappings are for.
         *
         * @param database String with the name of the database this mappings are for
         */
        public void setDatabase(String database) {
            this.database = database;
        }
    }

    public static class Mapping {
        private Set javaTypes = new HashSet();
        private JdbcType jdbcType;
        private SqlType sqlType;

        /**
         * Gets the Java types this mapping applies to.
         *
         * @return a Set with the Java types this mapping applies to
         */
        public Set getJavaTypes() {
            return javaTypes;
        }

        /**
         * Adds a Java type this mapping applies to.
         *
         * @param javaType a String with a Java type this mapping applies to
         */
        public void addJavaType(String javaType) {
            javaTypes.add(javaType);
        }

        /**
         * Gets the JDBC type the Java types map to.
         * 
         * @return the JdbcType the Java types map to
         */
        public JdbcType getJdbcType() {
            return jdbcType;
        }

        /**
         * Sets the JDBC type the Java types map to.
         * 
         * @param jdbcType the JdbcType the Java types map to
         */
        public void setJdbcType(JdbcType jdbcType) {
            this.jdbcType = jdbcType;
        }

        /**
         * Gets the SQL type the Java types map to.
         * 
         * @return the SqlType the Java types map to
         */
        public SqlType getSqlType() {
            return sqlType;
        }

        /**
         * Sets the SQL type the Java types map to.
         * 
         * @param sqlType the SqlType the Java types map to
         */
        public void setSqlType(SqlType sqlType) {
            this.sqlType = sqlType;
        }

    }

    public static class JdbcType {
        private String name;

        /**
         * Gets the name of the JDBC type.
         *
         * @return a String with the name of the JDBC type
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the name of the JDBC type.
         *
         * @param name a String with the name of the JDBC type
         */
        public void setName(String name) {
            this.name = name;
        }
    }

    public static class SqlType {
        private String pattern;
        private String defaultLength;

        /**
         * Gets the pattern to build the SQL type.
         *
         * @return a String with the pattern to build the SQL type
         */
        public String getPattern() {
            return pattern;
        }

        /**
         * Sets the pattern to build the SQL type.
         *
         * @param pattern a String with the pattern to build the SQL type
         */
        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        /**
         * Gets the default length for the SQL type.
         *
         * @return a String with the default length for the SQL type
         */
        public String getDefaultLength() {
            return defaultLength;
        }

        /**
         * Sets the default length for the SQL type.
         *
         * @param defaultLength a String with the default length for the SQL type
         */
        public void setDefaultLength(String defaultLength) {
            this.defaultLength = defaultLength;
        }
    }

}
