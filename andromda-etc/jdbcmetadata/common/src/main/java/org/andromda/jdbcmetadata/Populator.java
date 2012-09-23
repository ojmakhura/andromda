/**
 *
 */
package org.andromda.jdbcmetadata;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Populate DB metadata info in Objects from a JDBC database connection
 */
public class Populator
{
    private static final Logger LOGGER = Logger.getLogger(Populator.class);
    private String jdbcDriver;
    private String jdbcConnectionUrl;
    private String jdbcUser;
    private String jdbcPassword;
    private List<Table> tables = new ArrayList<Table>();
    private List<Column> columns = new ArrayList<Column>();
    private List<Index> indexes = new ArrayList<Index>();
    private List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();
    private List<Sequence> sequences = new ArrayList<Sequence>();
    private Schema schema = new Schema();
    private DatabaseMetaData metadata;
    private String tableNamePattern;
    private String tableNameExcludePattern;
    private String columnNamePattern;
    private String columnNameExcludePattern;
    private List<String> columnNameIncludePatterns = new ArrayList<String>();
    private List<String> columnNameExcludePatterns = new ArrayList<String>();
    private List<String> tableNameIncludePatterns = new ArrayList<String>();
    private List<String> tableNameExcludePatterns = new ArrayList<String>();
    private String typeMappingsUri;
    private String wrapperMappingsUri;
    private String expressionMappingsUri;
    private JdbcTypeFinder typeMapper = new JdbcTypeFinder();
    private Properties overrides = new Properties();

    /**
     *
     */
    public Populator()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Populator pop = new Populator();
        pop.setConfiguration();
        pop.populate();
    }

    /**
     * Connect to DB, populate tables, columns, indexes, keys, sequences
     */
    public void populate()
    {
        populatePattern(this.tableNamePattern, this.tableNameIncludePatterns);
        populatePattern(this.tableNameExcludePattern, this.tableNameExcludePatterns);
        populatePattern(this.columnNamePattern, this.columnNameIncludePatterns);
        populatePattern(this.columnNameExcludePattern, this.columnNameExcludePatterns);
        SqlToModelNameFormatter.loadWordMap();

        // TODO Load from classpath with andromda-core dependency
        this.setTypeMappingsUri("file:${basedir}/../mappings/RevengSqlMappings.xml");
        this.setWrapperMappingsUri("file:${basedir}/../mappings/UML2WrapperMappings.xml");
        this.setExpressionMappingsUri("file:${basedir}/../mappings/DBtoJavaExpressionMappings.xml");
        this.typeMapper.setTypeMappings(this.getTypeMappingsUri(), this.getWrapperMappingsUri());

        Connection conn = this.jdbcConnect();
        long startTime = System.currentTimeMillis();
        //int tableCount = 0;
        if (conn != null)
        {
            try
            {
                //tableCount =
                    populateTables(conn);
                populatePks(conn);
                populateIndexes(conn);
                populateUnique();
                populateColumns(conn);
                populateFks(conn);
                populateSequences(conn);
            }
            catch (SQLException ex)
            {
                LOGGER.error("SQLError on " + conn.toString(), ex);
            }
            DbUtils.closeQuietly(conn);
        }
        logResults(startTime);
    }

    /**
     * Set the configuration values
     */
    // TODO Read configuration from file or command line
    public void setConfiguration()
    {
        this.schema.setSchemaName("PUBLIC");
        this.schema.setCatalog("REVENG");
        // Java regexp .* means match all (match any character . any number of times *)
        // Also used when creating filters for reveng output file.
        // Use default .* when not specified for include patterns
        this.schema.setTableNamePattern(".*");
        this.tableNamePattern = ".*";
        this.columnNamePattern = ".*";
        // .*_VRSN$ and VRSN$ does not exclude CTCT_EVT_VRSN table when running reveng with hibernate tools
        this.tableNameExcludePattern = "ALL_.*,JDO_.*,.*_VRSN$";
        this.columnNameExcludePattern = "VRSN_.*,JDO_.*,CREATE_DTS,UPDT_DTS,CREATE_USR_ID_CD,UPDT_USR_ID_CD,PRGE_DTS";
        // Maven plugin: Get these values from the pom properties
        this.jdbcDriver = "org.h2.Driver";
        this.jdbcConnectionUrl = "jdbc:h2:~/reveng;MODE=ORACLE;AUTO_SERVER=TRUE;AUTO_RECONNECT=TRUE;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1";
        this.jdbcUser = "sa";
        this.jdbcPassword = "sa";
        // TODO set SqlToModelNameFormat config dir, wordMap, overrideMap file locations
        SqlToModelNameFormatter.setInputDirectory("${basedir}");
        readOverrideConfiguration("${basedir}/override.properties");
    }

    /**
     * Populate the UML default overrides for global/table/column configuration, for things like
     * sequence name, hibernate type, java type, insertable/updatable/nullable, transient, version. temporal type
     * @param propertyFileName file containing table.column.parameter=value entries
     * @return Map of String key = String value
     */
    public Properties readOverrideConfiguration(String propertyFileName)
    {
        if (StringUtils.isNotBlank(propertyFileName))
        {
            if (!propertyFileName.endsWith(".properties"))
            {
                propertyFileName += ".properties";
            }
            File propertyFile = new File(propertyFileName);
            // Check first if absolute pathname file exists
            if (propertyFile.exists())
            {
                try
                {
                    this.overrides.load(new FileInputStream(propertyFileName));
                }
                catch (Exception e)
                {
                    LOGGER.warn(propertyFileName, e);
                }
            }
            // Next try loading from classpath
            if (this.overrides.isEmpty())
            {
                try
                {
                    URL url = ClassLoader.getSystemResource(propertyFileName);
                    this.overrides.load(url.openStream());
                }
                catch (Exception e)
                {
                    LOGGER.warn(propertyFileName, e);
                }
            }
        }
        return this.overrides;
    }

    /**
     * Populate include and exclude pattern lists by parsing delimited string
     * @param pattern
     * @param patternList
     */
    public void populatePattern(String pattern, List<String> patternList)
    {
        if (StringUtils.isNotBlank(pattern) && patternList != null)
        {
            patternList.addAll(Arrays.asList(StringUtils.split(pattern, ",|")));
        }
    }

    /**
     * @return JDBC Connection
     */
    public Connection jdbcConnect()
    {
        long startTime = System.currentTimeMillis();
        if (this.jdbcDriver == null || this.jdbcConnectionUrl == null)
        {
            LOGGER.error(
                "JDBC Parameters not set, jdbcDriver=" + this.jdbcDriver + " jdbcConnectionUrl=" + this.jdbcConnectionUrl);
        }
        Connection connection = null;
        try
        {
            Class.forName(this.jdbcDriver);
            connection = DriverManager.getConnection(this.jdbcConnectionUrl,
                    this.jdbcUser, this.jdbcPassword);
        }
        catch (Exception ex)
        {
            LOGGER.error("Connection error", ex);
        }
        if (connection != null)
        {
            LOGGER.info(
                "Connected, TIME --> " + ((System.currentTimeMillis() - startTime) / 1000.0) + "[s]");
        }
        return connection;
    }

    /**
     * @param conn
     * @return Number of tables
     * @throws SQLException
     */
    public int populateTables(Connection conn) throws SQLException
    {
        int tableCount = 0;
        long startTime = System.currentTimeMillis();
        this.metadata = conn.getMetaData();
        ResultSet tableRs = this.metadata.getTables(
            //this.schema.getCatalog(),
            null,
            this.schema.getSchemaName(),
            //this.schema.getTableNamePattern(),
            null,
            new String[] {"TABLE"});
        //List<Table> tables = new ArrayList<Table>();
        //Pattern pTableEx = Pattern.compile(this.tableNameExcludePattern, Pattern.CASE_INSENSITIVE);
        //Pattern pTableIn = Pattern.compile(this.tableNameExcludePattern, Pattern.CASE_INSENSITIVE);
        while (tableRs.next())
        {
            Table table = new Table();
            String tableName = tableRs.getString("TABLE_NAME");
            table.setName(tableName);
            if (!isExcluded(tableName, this.tableNameIncludePatterns, this.tableNameExcludePatterns))
            {
                String umlName = getOverride(tableName, null, "umlName");
                if (StringUtils.isBlank(umlName))
                {
                    umlName = SqlToModelNameFormatter.toClassName(tableName);
                }
                table.setUmlName(umlName);
                String extend = getOverride(tableName, null, "extends");
                if (StringUtils.isNotBlank(extend))
                {
                    table.setExtend(extend);
                    LOGGER.info("Table " + table.getName() + " extends" + extend);
                }
                String implement = getOverride(tableName, null, "implements");
                if (StringUtils.isNotBlank(implement))
                {
                    table.setImplement(implement);
                    LOGGER.info("Table " + table.getName() + " implements" + implement);
                }
                table.setDescription(StringUtils.replaceChars(tableRs.getString("REMARKS"), "<>&", ""));
                table.setType(tableRs.getString("TABLE_TYPE"));
                table.setSchema(this.schema);
                tableCount++;
                LOGGER.debug("Table " + table.getName() + " class=" + table.getUmlName());
            }
            else
            {
                table.setExcluded(true);
                LOGGER.debug("Excluded Table " + table.getName());
            }
            this.tables.add(table);
        }
        LOGGER.info(
            "Populated " + tableCount + " tables in " +((System.currentTimeMillis() - startTime) / 1000.0) + "[s]");
        return tableCount;
    }

    /**
     * @param conn
     * @throws SQLException
     */
    public void populateColumns(Connection conn) throws SQLException
    {
        for (Table table : this.tables)
        {
            if (!table.isExcluded())
            {
                List<Column> tableColumns = new ArrayList<Column>();
                final ResultSet columnRs = this.metadata.getColumns(null,
                    this.schema.getSchemaName(), table.getName(), null);
                while (columnRs.next())
                {
                    final String columnName = columnRs.getString("COLUMN_NAME");
                    //Column column = new Column();
                    //column.setColumnName(columnName);
                    //column.setTable(table);
                    Column column = findColumnByName(table, columnName);
                    column.setSeqNum(columnRs.getInt("ORDINAL_POSITION"));
                    if (StringUtils.isBlank(column.getUmlName()))
                    {
                        String umlName = getOverride(table.getName() + '.' + columnName, null, "umlName");
                        if (StringUtils.isNotBlank(umlName))
                        {
                            column.setUmlName(umlName);
                        }
                        else
                        {
                            // If column is part of table name, remove table name from UML name
                            umlName = SqlToModelNameFormatter.toAttributeName(columnName);
                            if (StringUtils.capitalize(umlName).startsWith(table.getUmlName()))
                            {
                                umlName = StringUtils.uncapitalize(umlName.substring(table.getUmlName().length()));
                            }
                            column.setUmlName(umlName);
                        }
                    }
                    String dbName = table.getName() + "." + column.getColumnName();
                    String umlColumnName = table.getUmlName() + "." + column.getUmlName();
                    // Yes I know it's denormalized, but it's easier because we always need the table name with the column
                    column.setTableName(table.getName());
                    if (!isExcluded(columnName, this.columnNameIncludePatterns, this.columnNameExcludePatterns))
                    {
                        //logger.info("Column " + column.getColumnName().toUpperCase() + " exclude=" + this.columnNameExcludePattern.toUpperCase()
                        //        + " matches=" + column.getColumnName().toUpperCase().matches(this.columnNameExcludePattern.toUpperCase()));
                        column.setSqlType(columnRs.getString("TYPE_NAME"));
                        column.setJavaType(columnRs.getString("DATA_TYPE"));
                        column.setColSize(Integer.valueOf(columnRs.getString("COLUMN_SIZE")).intValue());
                        column.setDecPlaces(Integer.valueOf(columnRs.getString("DECIMAL_DIGITS")));
                        if (!column.isSetNullable())
                        {
                            column.setNullable(columnRs.getInt("NULLABLE") != DatabaseMetaData.attributeNoNulls ? true : false);
                        }
                        column.setDefaultValue(columnRs.getString("COLUMN_DEF"));
                        String definition = column.getSqlType() + "(" + column.getColSize()
                            + (JdbcTypeFinder.isNumeric(column.getSqlType()) ? ", " + column.getDecPlaces() : "")
                            + ")" + (!column.isNullable() ? " NOT NULL" : "");
                        // Set the hibernate type directly. i.e. for yes_no or other custom type
                        String defaultValue = this.getOverride(dbName, umlColumnName, "default");
                        if (!StringUtils.isBlank(defaultValue))
                        {
                            column.setJavaDefault(defaultValue);
                        }
                        if (StringUtils.isNotBlank(column.getDefaultValue()))
                        {
                            definition += " DEFAULT " + column.getDefaultValue();
                            if (StringUtils.isBlank(column.getJavaDefault()))
                            {
                                String javaExpression = ExpressionTypeFinder.getJavaExpression(column.getDefaultValue().replaceAll("'", ""),
                                    column.getJdbcType(), column.isNullable(), column.getColSize(), column.getPkTable() != null,
                                    column.isUnique());
                                // SQL default has single quotes for String types
                                column.setJavaDefault(javaExpression);
                                LOGGER.info("Column " + dbName + " JavaDefault=" + column.getJavaDefault());
                            }
                        }
                        column.setSqlDefinition(definition);
                        column.setAutoincrement(columnRs.getString("IS_AUTOINCREMENT").equals("YES") ? true : false);
                        String umlType = this.getOverride(dbName, umlColumnName, "umlType");
                        if (umlType == null)
                        {
                            column.setUmlType(this.typeMapper.getUmlType(column.getSqlType(), column.isNullable(), column.getColSize(),
                                    column.getDecPlaces(), column.getPkTable() != null, column.isUnique()));
                        }
                        else
                        {
                            column.setUmlType(umlType);
                        }
                        column.setDescription(StringUtils.replaceChars(columnRs.getString("REMARKS"), "<>&", ""));
                        //column.setUmlName(SqlToModelNameFormatter.toAttributeName(columnName));
                        //column.setNavigable(this.getBooleanOverride(dbName, umlColumnName, "navigable", true));
                        // Set the hibernate type directly. i.e. for yes_no or other custom type
                        column.setHibernateType(this.getOverride(dbName, umlColumnName, "hibernateType"));
                        LOGGER.info("Column " + dbName + " definition=" + column.getSqlDefinition()
                            + " umlName=" + umlColumnName + " umlType=" + column.getUmlType());
                    }
                    else
                    {
                        column.setExcluded(true);
                        LOGGER.info("Excluded Column " + dbName);
                    }
                    tableColumns.add(column);
                }
                Collections.sort(tableColumns);
                table.setColumns(tableColumns);
                this.columns.addAll(tableColumns);
            }
        }
    }

    /**
     * @param dbName
     * @param umlColumnName
     * @param property
     * @return rtnValue
     */
    public boolean getBooleanOverride(String dbName, String umlColumnName, String property, boolean defaultValue)
    {
        String stringDefault = getOverride(dbName, umlColumnName, property);
        if (StringUtils.isNotBlank(stringDefault))
        {
            if (stringDefault.equalsIgnoreCase("false") || stringDefault.equalsIgnoreCase("f") || stringDefault.equalsIgnoreCase("0"))
            {
                defaultValue=false;
            }
            else if (stringDefault.equalsIgnoreCase("true") || stringDefault.equalsIgnoreCase("t") || stringDefault.equalsIgnoreCase("1"))
            {
                defaultValue=true;
            }
        }
        return defaultValue;
    }

    /**
     * @param dbName
     * @param umlColumnName
     * @param property
     * @return rtnValue
     */
    public String getStringOverride(String dbName, String umlColumnName, String property, String defaultValue)
    {
        String stringDefault = getOverride(dbName, umlColumnName, property);
        if (StringUtils.isNotBlank(stringDefault))
        {
            defaultValue=stringDefault;
        }
        return defaultValue;
    }

    /**
     * @param dbName
     * @param umlName
     * @param property
     * @return rtnValue
     */
    @SuppressWarnings("null")
    public String getOverride(String dbName, String umlName, String property)
    {
        String rtnValue = null;
        if (umlName != null)
        {
            rtnValue = this.overrides.getProperty(umlName + "." + property, null);
        }
        if (StringUtils.isBlank(rtnValue))
        {
            rtnValue = this.overrides.getProperty(dbName + "." + property, null);
            if (StringUtils.isNotBlank(rtnValue))
            {
                rtnValue = rtnValue.trim();
                LOGGER.info("overrides DB " + dbName + " property " + property + "=" + rtnValue);
            }
            else
            // Check default property value
            {
                rtnValue = this.overrides.getProperty("default." + property, null);
                if (StringUtils.isNotBlank(rtnValue))
                {
                    rtnValue = rtnValue.trim();
                    LOGGER.info("overrides DB " + dbName + " default property " + property + "=" + rtnValue);
                }
            }
        }
        else
        {
            if (StringUtils.isNotBlank(rtnValue))
            {
                rtnValue = rtnValue.trim();
            }
            LOGGER.info("overrides Attribute " + umlName + " property " + property + "=" + rtnValue);
        }
        /*if (StringUtils.isBlank(rtnValue))
        {
            LOGGER.info("overrides Not Found for " + umlName + " or " + dbName + " property " + property);
        }*/
        return rtnValue;
    }

    /**
     * @param name Column name to check for inclusion/exclusion against patterns
     * @param includePatterns
     * @param excludePatterns
     * @return true if column or table is excluded based on include/exclude patterns
     */
    public boolean isExcluded(String name, List<String> includePatterns, List<String> excludePatterns)
    {
        if (includePatterns == null)
        {
            includePatterns = new ArrayList<String>();
        }
        if (excludePatterns == null)
        {
            excludePatterns = new ArrayList<String>();
        }
        boolean excluded = true;
        if (StringUtils.isBlank(name))
        {
            return excluded;
        }
        final String checkName = name.toUpperCase();
        // Verify that name matches any included pattern
        if (includePatterns.isEmpty())
        {
            excluded = false;
        }
        for (String nameIncludePattern : includePatterns)
        {
            if ((StringUtils.isBlank(nameIncludePattern) ||
                checkName.matches(nameIncludePattern.toUpperCase())))
            {
                excluded = false;
                break;
            }
        }
        // Verify that name does not match any excluded pattern
        if (!excluded && !excludePatterns.isEmpty())
        {
            for (String nameExcludePattern : excludePatterns)
            {
                if (checkName.matches(nameExcludePattern.toUpperCase()))
                {
                    excluded = true;
                    break;
                }
            }
        }
        return excluded;
    }

    /**
     * @param conn
     * @throws SQLException
     */
    public void populateIndexes(Connection conn) throws SQLException
    {
        for (Table table : this.tables)
        {
            if (!table.isExcluded())
            {
                Index index = null;
                final ResultSet indexRs = this.metadata.getIndexInfo(null,
                    this.schema.getSchemaName(), table.getName(), false, true);
                while (indexRs.next())
                {
                    final String indexName = indexRs.getString("INDEX_NAME");
                    // results ordered by index name, seq
                    if (index == null || !indexName.equals(index.getName()))
                    {
                        index = new Index();
                        index.setName(indexName);
                        index.setUnique(!indexRs.getBoolean("NON_UNIQUE"));
                        index.setTable(table);
                        this.indexes.add(index);
                        // Bidirectional - add reference on both sides
                        table.getIndexes().add(index);
                    }
                    IndexColumn indexCol = new IndexColumn();
                    final String columnName = indexRs.getString("COLUMN_NAME");
                    indexCol.setSeqNum(indexRs.getInt("ORDINAL_POSITION"));
                    Column column = findColumnByName(table, columnName);
                    indexCol.setColumn(column);
                    index.getColumns().add(indexCol);
                    LOGGER.info("Index " + table.getName() + "." + column.getColumnName() + " seq=" + indexCol.getSeqNum()
                        + " name=" + index.getName() + " unique=" + index.isUnique());
                }
            }
        }
    }

    /**
     * Iterates through all table indexes to find unique or pk indexes with only one column
     * @throws SQLException
     */
    public void populateUnique() throws SQLException
    {
        for (Table table : this.tables)
        {
            if (!table.isExcluded())
            {
                if (table.getPkColumns().size() == 1)
                {
                    Column column = table.getPkColumns().iterator().next();
                    column.setUnique(true);
                    LOGGER.info("populatePkUnique " + table.getName() + "." + column.getColumnName());
                }
                for (Index index : table.getIndexes())
                {
                    if (index.isUnique() && index.getColumns().size() == 1)
                    {
                        Column column = index.getColumns().iterator().next().getColumn();
                        column.setUnique(true);
                        LOGGER.info("populateUnique " + table.getName() + "." + column.getColumnName());
                    }
                }
            }
        }
    }

    /**
     * @param conn
     * @throws SQLException
     */
    public void populatePks(Connection conn) throws SQLException
    {
        for (Table table : this.tables)
        {
            if (!table.isExcluded())
            {
                final ResultSet pkRs = this.metadata.getPrimaryKeys(null,
                    this.schema.getSchemaName(), table.getName());
                while (pkRs.next())
                {
                    //PkColumn pkColumn = new PkColumn();
                    table.setPkName(pkRs.getString("PK_NAME"));
                    final String columnName = pkRs.getString("COLUMN_NAME");
                    //pkColumn.setSeqNum(pkRs.getInt("KEY_SEQ"));
                    Column column = findColumnByName(table, columnName);
                    column.setPkSeqNum(pkRs.getInt("KEY_SEQ"));
                    table.getPkColumns().add(column);
                    String umlName = getOverride(table.getName() + '.' + columnName, null, "umlName");
                    if (StringUtils.isNotBlank(umlName))
                    {
                        column.setUmlName(umlName);
                    }
                    else
                    {
                        // If column is part of table PK, set UML name back to original full name
                        column.setUmlName(SqlToModelNameFormatter.toAttributeName(column.getColumnName()));
                    }
                    column.setNullable(false); // For use in findBy a
                    column.setPkTable(table);
                    LOGGER.info("PK " + table.getName() + "." + column.getColumnName() + " seq=" + column.getPkSeqNum()
                        + " pk=" + column.getColumnName());
                }
            }
        }
    }

    /**
     * @param conn
     * @throws SQLException
     */
    public void populateFks(Connection conn) throws SQLException
    {
        for (Table table : this.tables)
        {
            if (!table.isExcluded())
            {
                ForeignKey fk = null;
                // primary key columns that are referenced by the given table's
                // foreign key columns (the primary keys imported by a table)
                final ResultSet keysRs = this.metadata.getImportedKeys(null,
                    this.schema.getSchemaName(), table.getName());
                while (keysRs.next())
                {
                    final String fkName = keysRs.getString("FK_NAME");
                    final String pkName = keysRs.getString("PK_NAME");
                    //final String fkTableName = keysRs.getString("FKTABLE_NAME");
                    final String fkColumnName = keysRs.getString("FKCOLUMN_NAME");
                    final String pkTableName = keysRs.getString("PKTABLE_NAME");
                    final String pkColumnName = keysRs.getString("PKCOLUMN_NAME");
                    // results ordered by key name, seq. If key or table name changes, create new FK
                    if (fk == null || !fkName.equals(fk.getFkName()))
                    {
                        fk = new ForeignKey();
                        fk.setFkName(fkName);
                        fk.setPkName(pkName);
                        fk.setFkTable(table);
                        fk.setReferencedTable(findTableByName(pkTableName));
                        fk.setUpdateRule(getCascadeType(keysRs.getInt("UPDATE_RULE")));
                        fk.setDeleteRule(getCascadeType(keysRs.getInt("DELETE_RULE")));
                        this.foreignKeys.add(fk);
                        table.getForeignKeys().add(fk);
                    }
                    FKColumn fkSeq = new FKColumn();
                    fkSeq.setSeqNum(keysRs.getInt("KEY_SEQ"));
                    Column column = findColumnByName(table, fkColumnName);
                    Column refColumn = findColumnByName(findTableByName(pkTableName), pkColumnName);
                    fkSeq.setFkColumn(column);
                    fkSeq.setFkRefColumn(refColumn);
                    fkSeq.setForeignKey(fk);
                    fk.getColumns().add(fkSeq);
                    column.getFkSequences().add(fkSeq);
                    refColumn.getFkRefSequences().add(fkSeq);
                    this.setNavAgg(table, fk);
                    LOGGER.info("FK " + table.getName() + "." + column.getColumnName() + " seq=" + fkSeq.getSeqNum()
                        + " name=" + fk.getFkName() + " pkColumn=" + fk.getReferencedTable().getName() + "." + fkSeq.getFkRefColumn().getColumnName());
                }
            }
        }
    }

    /**
     * @param table
     * @param fk
     */
    public void setNavAgg(Table table, ForeignKey fk)
    {
        String pkTable = StringUtils.uncapitalize(fk.getReferencedTable().getUmlName());
        String fkTable = StringUtils.uncapitalize(fk.getFkTable().getUmlName());
        boolean one2One = isOne2One(fk);
        fk.setOneToOne(one2One);
        if (!one2One)
        {
            fkTable = StringUtilsHelper.pluralize(fkTable);
        }
        String umlPKName = fk.getReferencedTable().getUmlName() + '.' + fkTable;
        String umlFKName = fk.getFkTable().getUmlName() + '.' + pkTable;
        fk.setFkUmlPropertyName(pkTable);
        // TODO Customize override associationEnd property name
        String navigable = getOverride(umlPKName, null, "navigable");
        if (StringUtils.isNotBlank(navigable))
        {
            try
            {
                fk.setNavigableFK(Boolean.valueOf(navigable).booleanValue());
            }
            catch (Exception ex)
            {
                LOGGER.error("not boolean value for navigable property on " + umlPKName, ex);
            }
        }
        String aggType = getOverride(umlPKName, null, "aggregation");
        if (StringUtils.isNotBlank(aggType))
        {
            // Change aggregation type from default 'none' to 'shared' or 'composite'
            fk.setAggregationFK(AggregationType.fromString(aggType.toUpperCase()));
        }

        fk.setPkUmlPropertyName(fkTable);
        navigable = getOverride(umlFKName, null, "navigable");
        if (StringUtils.isNotBlank(navigable))
        {
            try
            {
                fk.setNavigablePK(Boolean.valueOf(navigable).booleanValue());
            }
            catch (Exception ex)
            {
                LOGGER.error("not boolean value for navigable property on " + umlFKName, ex);
            }
        }
        aggType = getOverride(umlFKName, null, "aggregation");
        if (StringUtils.isNotBlank(aggType))
        {
            // Change aggregation type from default 'none' to 'shared' or 'composite'
            fk.setAggregationPK(AggregationType.fromString(aggType.toUpperCase()));
        }
        LOGGER.info("setNavAgg one2one=" + one2One + " PK property=" + umlFKName + " nav=" + fk.isNavigablePK() + " agg=" + fk.getAggregationPK()
                + "  FK=" + umlPKName + " nav=" + fk.isNavigableFK() + " agg=" + fk.getAggregationFK());
    }

    /**
     * The relationship between to tables is 1:1 if all of the table FK columns
     * are also PK columns, and correspond to the referenced table PK columns
     * @param fk
     * @return true if 1:1 relationship
     */
    public boolean isOne2One(ForeignKey fk)
    {
        boolean one2one = false;
        // 1:1 tables must have the same PK columns
        //LOGGER.info("isOne2One RefPKTable: " + fk.getReferencedTable().getUmlName() + "=" + fk.getReferencedTable().getPkColumns().size() + " FkTable " + fk.getFkTable().getUmlName() + "=" + fk.getColumns().size());
        if (fk.getFkTable().getPkColumns().size() ==
            fk.getColumns().size())
        {
            one2one = true;
            int colNum = 0;
            for (FKColumn fkColumn : fk.getColumns())
            {
                // If every FK column corresponds to a PK column OneToOne=true
                Column pkColumn = fk.getFkTable().getPkColumns().get(colNum);
                if (!fkColumn.getFkColumn().equals(pkColumn))
                {
                    one2one = false;
                }
                /*LOGGER.info("isOne2One pkColumn=" + pkColumn);
                LOGGER.info("isOne2One fkColumn=" + fkColumn.getFkColumn());
                LOGGER.info("isOne2One fkRefColumn=" + fkColumn.getFkRefColumn());
                LOGGER.info("isOne2One one2one=" + one2one + " " + colNum + " fk=" + fkColumn.getFkColumn().getTable().getUmlName() + "." + fkColumn.getFkColumn().getColumnName() + colNum +
                    " pk=" + pkColumn.getTable().getUmlName() + "." + pkColumn.getColumnName());*/
                colNum++;
            }
        }
        // one2one is always false if no FK columns or any column not part of PK
        if (one2one)
        {
            LOGGER.info("isOne2One one2one=" + one2one + " fk=" + fk.getFkTable().getUmlName()
                    + " pk=" + fk.getReferencedTable().getUmlName());
        }
        return one2one;
    }

    // From databaseMetaData source
    /** 0 */
    public static final int importedKeyCascade = 0;
    /** 1 */
    public static final int importedKeyRestrict = 1;
    /** 2 */
    public static final int importedKeySetNull = 2;
    /** 3 */
    public static final int importedKeyNoAction = 3;
    /** 4 */
    public static final int importedKeySetDefault = 4;
    /**
     * @param type
     * @return CascadeType
     */
    public CascadeType getCascadeType(int type)
    {
        // Default RESTRICT if not set
        if (type == 0)
        {
            return CascadeType.CASCADE;
        }
        /*else if (type == 1)
        {
            return CascadeType.RESTRICT;
        }*/
        else if (type == 2)
        {
            return CascadeType.SET_NULL;
        }
        else if (type == 3)
        {
            return CascadeType.NO_ACTION;
        }
        else if (type == 4)
        {
            return CascadeType.SET_DEFAULT;
        }
        else
        {
            return CascadeType.RESTRICT;
        }
    }

    /**
     * @param conn
     * @throws SQLException
     */
    public void populateSequences(Connection conn) throws SQLException
    {
        // Get system tables with the word 'SEQUENCE'
        final ResultSet seqTablesRs = this.metadata.getTables(null,
            null, "%SEQUENCE%", new String[] { "SYSTEM TABLE" });
        while (seqTablesRs.next())
        {
            final String tableName = seqTablesRs.getString("TABLE_NAME");
            final String tableCat = seqTablesRs.getString("TABLE_CAT");
            final String tableSchema = seqTablesRs.getString("TABLE_SCHEM");
            final Table table = findTableByName(tableName);
            if (table == null || !table.isExcluded())
            {
                String cols = " Columns: ";
                String query = "SELECT ";
                String nameColumn = null;
                // Get column called 'NAME' from SEQUENCES table
                final ResultSet tableRs = this.metadata.getColumns(tableCat,
                    tableSchema, tableName, null);
                while (tableRs.next())
                {
                    final String columnName = tableRs.getString("COLUMN_NAME");
                    cols += columnName + ", ";
                    if (columnName.contains("NAME"))
                    {
                        query += columnName;
                        nameColumn = columnName;
                    }
                }
                LOGGER.debug("System Table " + tableName + " catalog=" + tableCat + " schema=" + tableSchema + cols);
                // Create and execute query to select names from sequence table
                query += " FROM " + tableSchema + "." + tableName;
                Statement statement = null;
                try
                {
                    statement = conn.createStatement();
                    ResultSet seqRs = statement.executeQuery(query);
                    while (seqRs.next())
                    {
                        Sequence seq = new Sequence();
                        final String seqName = seqRs.getString(nameColumn);
                        seq.setName(seqName);
                        this.sequences.add(seq);
                        LOGGER.info("Added Sequence " + seqName);
                    }
                } catch (SQLException e ) {
                    e.printStackTrace();
                } finally {
                    if (statement != null) { statement.close(); }
                }
            }
        }
        /*// If there is only one sequence in the DB, it must be used as generator for all numeric PK
        if (this.sequences.size() == 1)
        {
            for (Table table : this.tables)
            {
                if (table.getPkColumns().size() == 1)
                {
                    for (Column column : table.getPkColumns())
                    {
                        // Check if column type is numeric
                        if (column.getJavaType())
                        column.set
                    }
                }
            }
        }*/
    }

    /**
     * Return the Table object reference with the given name
     * @param tableName Name to be found
     * @return Table object with a given name
     */
    public Table findTableByName(String tableName)
    {
        for (Table table : this.tables)
        {
            if (table.getName().toUpperCase().equalsIgnoreCase(tableName))
            {
                return table;
            }
        }
        return null;
    }

    /**
     * Return the Column object reference with the given name on a table
     * @param table Table containing column
     * @param columnName Column name to be found
     * @return Table object with a given name
     */
    public Column findColumnByName(Table table, String columnName)
    {
        for (Column column : this.columns)
        {
            if (column.getColumnName().toUpperCase().equalsIgnoreCase(columnName)
                && column.getTable().getName().toUpperCase().equalsIgnoreCase(table.getName()))
            {
                return column;
            }
        }
        // Column does not yet exist on this table - create and add it
        Column column = new Column();
        column.setColumnName(columnName);
        column.setTableName(table.getName());
        column.setTable(table);
        table.getColumns().add(column);
        this.columns.add(column);
        return column;
    }

    /**
     * @return the tables
     */
    public List<Table> getTables()
    {
        return this.tables;
    }

    /**
     * @param tables the tables to set
     */
    public void setTables(List<Table> tables)
    {
        this.tables = tables;
    }

    /**
     * @return the columns
     */
    public List<Column> getColumns()
    {
        return this.columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(List<Column> columns)
    {
        this.columns = columns;
    }

    /**
     * @return the indexes
     */
    public List<Index> getIndexes()
    {
        return this.indexes;
    }

    /**
     * @param indexes the indexes to set
     */
    public void setIndexes(List<Index> indexes)
    {
        this.indexes = indexes;
    }

    /**
     * @return the foreignKeys
     */
    public List<ForeignKey> getForeignKeys()
    {
        return this.foreignKeys;
    }

    /**
     * @param foreignKeys the foreignKeys to set
     */
    public void setForeignKeys(List<ForeignKey> foreignKeys)
    {
        this.foreignKeys = foreignKeys;
    }

    /**
     * @return the sequences
     */
    public List<Sequence> getSequences()
    {
        return this.sequences;
    }

    /**
     * @param sequences the sequences to set
     */
    public void setSequences(List<Sequence> sequences)
    {
        this.sequences = sequences;
    }

    /**
     * @return the schema
     */
    public Schema getSchema()
    {
        return this.schema;
    }

    /**
     * @param schema the schema to set
     */
    public void setSchema(Schema schema)
    {
        this.schema = schema;
    }

    /**
     * @return the metadata
     */
    public DatabaseMetaData getMetadata()
    {
        return this.metadata;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(DatabaseMetaData metadata)
    {
        this.metadata = metadata;
    }

    /**
     * @return the tableNamePattern
     */
    public String getTableNamePattern()
    {
        return this.tableNamePattern;
    }

    /**
     * @param tableNamePattern the tableNamePattern to set
     */
    public void setTableNamePattern(String tableNamePattern)
    {
        this.tableNamePattern = tableNamePattern;
    }

    /**
     * @return the tableNameExcludePattern
     */
    public String getTableNameExcludePattern()
    {
        return this.tableNameExcludePattern;
    }

    /**
     * @param tableNameExcludePattern the tableNameExcludePattern to set
     */
    public void setTableNameExcludePattern(String tableNameExcludePattern)
    {
        this.tableNameExcludePattern = tableNameExcludePattern;
    }

    /**
     * @return the columnNamePattern
     */
    public String getColumnNamePattern()
    {
        return this.columnNamePattern;
    }

    /**
     * @param columnNamePattern the columnNamePattern to set
     */
    public void setColumnNamePattern(String columnNamePattern)
    {
        this.columnNamePattern = columnNamePattern;
    }

    /**
     * @return the columnNameExcludePatterns
     */
    public List<String> getColumnNameExcludePatterns()
    {
        return this.columnNameExcludePatterns;
    }

    /**
     * @param columnNameExcludePattern the columnNameExcludePattern to set
     */
    public void setColumnNameExcludePattern(String columnNameExcludePattern)
    {
        this.columnNameExcludePattern = columnNameExcludePattern;
    }

    /**
     * Log output for each table
     * @param startTime
     */
    public void logResults(long startTime)
    {
        int excludedCount = 0;
        for (Table table : this.tables)
        {
            LOGGER.info("Table " + table.getName() + (table.isExcluded() ? " excluded" : " class=" + table.getUmlName()));
            LOGGER.info("Columns=" + table.getColumns().size() + " Indexes=" + table.getIndexes().size() + " PKColumns=" + table.getPkColumns().size() + " FKs=" + table.getForeignKeys().size());
            if (table.isExcluded())
            {
                excludedCount++;
            }
        }
        LOGGER.info("Completed " + this.tables.size() + " tables Excluded " + excludedCount + " in " +
            ((System.currentTimeMillis() - startTime) / 1000.0) + "[s]");
    }

    /**
     * @return the columnNameIncludePatterns
     */
    public List<String> getColumnNameIncludePatterns()
    {
        return this.columnNameIncludePatterns;
    }

    /**
     * @param columnNameIncludePatterns the columnNameIncludePatterns to set
     */
    public void setColumnNameIncludePatterns(List<String> columnNameIncludePatterns)
    {
        this.columnNameIncludePatterns = columnNameIncludePatterns;
    }

    /**
     * @return the tableNameIncludePatterns
     */
    public List<String> getTableNameIncludePatterns()
    {
        return this.tableNameIncludePatterns;
    }

    /**
     * @param tableNameIncludePatterns the tableNameIncludePatterns to set
     */
    public void setTableNameIncludePatterns(List<String> tableNameIncludePatterns)
    {
        this.tableNameIncludePatterns = tableNameIncludePatterns;
    }

    /**
     * @return the tableNameExcludePatterns
     */
    public List<String> getTableNameExcludePatterns()
    {
        return this.tableNameExcludePatterns;
    }

    /**
     * @param tableNameExcludePatterns the tableNameExcludePatterns to set
     */
    public void setTableNameExcludePatterns(List<String> tableNameExcludePatterns)
    {
        this.tableNameExcludePatterns = tableNameExcludePatterns;
    }

    /**
     * @return the columnNameExcludePattern
     */
    public String getColumnNameExcludePattern()
    {
        return this.columnNameExcludePattern;
    }

    /**
     * @param columnNameExcludePatterns the columnNameExcludePatterns to set
     */
    public void setColumnNameExcludePatterns(List<String> columnNameExcludePatterns)
    {
        this.columnNameExcludePatterns = columnNameExcludePatterns;
    }

    /**
     * @return the jdbcDriver
     */
    public String getJdbcDriver()
    {
        return this.jdbcDriver;
    }

    /**
     * @param jdbcDriver the jdbcDriver to set
     */
    public void setJdbcDriver(String jdbcDriver)
    {
        this.jdbcDriver = jdbcDriver;
    }

    /**
     * @return the jdbcConnectionUrl
     */
    public String getJdbcConnectionUrl()
    {
        return this.jdbcConnectionUrl;
    }

    /**
     * @param jdbcConnectionUrl the jdbcConnectionUrl to set
     */
    public void setJdbcConnectionUrl(String jdbcConnectionUrl)
    {
        this.jdbcConnectionUrl = jdbcConnectionUrl;
    }

    /**
     * @return the jdbcUser
     */
    public String getJdbcUser()
    {
        return this.jdbcUser;
    }

    /**
     * @param jdbcUser the jdbcUser to set
     */
    public void setJdbcUser(String jdbcUser)
    {
        this.jdbcUser = jdbcUser;
    }

    /**
     * @return the jdbcPassword
     */
    public String getJdbcPassword()
    {
        return this.jdbcPassword;
    }

    /**
     * @param jdbcPassword the jdbcPassword to set
     */
    public void setJdbcPassword(String jdbcPassword)
    {
        this.jdbcPassword = jdbcPassword;
    }

    /**
     * @return TypeMappingsUri
     */
    public String getTypeMappingsUri()
    {
        return this.typeMappingsUri;
    }

    /**
     * @param typeMappingsUri
     */
    public void setTypeMappingsUri(String typeMappingsUri)
    {
        this.typeMappingsUri = typeMappingsUri;
    }

    /**
     * @return WrapperMappingsUri
     */
    public String getWrapperMappingsUri()
    {
        return this.wrapperMappingsUri;
    }

    /**
     * @param wrapperMappingsUri
     */
    public void setWrapperMappingsUri(String wrapperMappingsUri)
    {
        this.wrapperMappingsUri = wrapperMappingsUri;
    }

    /**
     * @param expressionMappingsUri
     */
    public void setExpressionMappingsUri(String expressionMappingsUri)
    {
        this.expressionMappingsUri = expressionMappingsUri;
    }
}
