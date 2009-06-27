package org.andromda.schema2xmi;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.engine.ModelProcessorException;
import org.andromda.core.mapping.Mappings;
import org.andromda.core.namespace.NamespaceComponents;
import org.andromda.core.repository.Repositories;
import org.andromda.core.repository.RepositoryFacade;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlAssociation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.DataTypesPackage;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.ModelManagementPackage;


/**
 * Performs the transformation of database schema to XMI.
 *
 * @todo This class really should have the functionality it uses (writing model
 *       elements) moved to the metafacades.
 * @todo This class should be refactored into smaller classes.
 * @author Chad Brandon
 * @author Joel Kozikowski
 */
public class SchemaTransformer
{
    private static final Logger logger = Logger.getLogger(SchemaTransformer.class);
    private RepositoryFacade repository = null;

    /**
     * The JDBC driver class
     */
    private String jdbcDriver = null;

    /**
     * The JDBC schema user.
     */
    private String jdbcUser = null;

    /**
     * The JDBC schema password.
     */
    private String jdbcPassword = null;

    /**
     * The JDBC connection URL.
     */
    private String jdbcConnectionUrl = null;

    /**
     * The name of the package in which the name of the elements will be
     * created.
     */
    private String packageName = null;

    /**
     * Stores the name of the schema where the tables can be found.
     */
    private String schema = null;

    /**
     * The regular expression pattern to match on when deciding what table names
     * to add to the transformed XMI.
     */
    private String tableNamePattern = null;

    /**
     * Stores the schema types to model type mappings.
     */
    private Mappings typeMappings = null;

    /**
     * Stores the classes keyed by table name.
     */
    private Map classes = new HashMap();

    /**
     * Stores the foreign keys for each table.
     */
    private Map foreignKeys = new HashMap();

    /**
     * Specifies the Class stereotype.
     */
    private String classStereotypes = null;

    /**
     * Stores the name of the column tagged value to use for storing the name of
     * the column.
     */
    private String columnTaggedValue = null;

    /**
     * Stores the name of the table tagged value to use for storing the name of
     * the table.
     */
    private String tableTaggedValue = null;

    /**
     * The metadata column name needed to retrieve the database column type field.
     */
    private String metaColumnTypeName = "TYPE_NAME";

    
    /**
     * The metadata column name needed to retrieve the database column size field.
     */
    private String metaColumnColumnSize = "COLUMN_SIZE";
//    private String metaColumnColumnSize = "PRECISION";
    
    
    /**
     * The metadata column name needed to retrieve the database column number of decimal places
     */
    private String metaColumnDecPlaces = null;
//    private String metaColumnDecPlaces = "SCALE";
    
    
    /**
     * The set of additional tagged values that are (optionally) added to each attribute
     * in the generated model.
     */
    private HashMap attributeTaggedValues = new HashMap();
    
    /**
     * Stores the version of XMI that will be produced.
     */
    private String xmiVersion = null;

    /**
     * Constructs a new instance of this SchemaTransformer.
     */
    public SchemaTransformer(
        String jdbcDriver,
        String jdbcConnectionUrl,
        String jdbcUser,
        String jdbcPassword)
    {
        ExceptionUtils.checkEmpty("jdbcDriver", jdbcDriver);
        ExceptionUtils.checkEmpty("jdbcConnectionUrl", jdbcConnectionUrl);
        ExceptionUtils.checkEmpty("jdbcUser", jdbcUser);
        ExceptionUtils.checkEmpty("jdbcPassword", jdbcPassword);

        NamespaceComponents.instance().discover();
        Repositories.instance().initialize();
        this.repository = Repositories.instance().getImplementation(Schema2XMIGlobals.REPOSITORY_NAMESPACE_NETBEANSMDR);
        if (repository == null)
        {
            throw new ModelProcessorException(
                "No Repository could be found, please make sure you have a repository with namespace " + Schema2XMIGlobals.REPOSITORY_NAMESPACE_NETBEANSMDR +
                " on your classpath");
        }
        this.repository.open();

        this.jdbcDriver = jdbcDriver;
        this.jdbcConnectionUrl = jdbcConnectionUrl;
        this.jdbcUser = jdbcUser;
        this.jdbcPassword = jdbcPassword;
        this.jdbcConnectionUrl = jdbcConnectionUrl;
    }

    /**
     * Transforms the Schema file and writes it to the location given by
     * <code>outputLocation</code>. The <code>inputModel</code> must be a
     * valid URL, otherwise an exception will be thrown.
     *
     * @param inputModel the location of the input model to start with (if there
     *        is one)
     * @param outputLocation The location to where the transformed output will
     *        be written.
     */
    public void transform(
        String inputModel,
        String outputLocation)
    {
        long startTime = System.currentTimeMillis();
        outputLocation = StringUtils.trimToEmpty(outputLocation);
        if (outputLocation == null)
        {
            throw new IllegalArgumentException("'outputLocation' can not be null");
        }
        Connection connection = null;
        try
        {
            if (inputModel != null)
            {
                logger.info("Input model --> '" + inputModel + "'");
            }
            this.repository.readModel(
                new String[] {inputModel},
                null);
            Class.forName(this.jdbcDriver);
            connection = DriverManager.getConnection(this.jdbcConnectionUrl, this.jdbcUser, this.jdbcPassword);
            repository.writeModel(
                transform(connection),
                outputLocation,
                this.xmiVersion);
        }
        catch (Throwable th)
        {
            throw new SchemaTransformerException(th);
        }
        finally
        {
            DbUtils.closeQuietly(connection);
            repository.close();
        }
        logger.info(
            "Completed adding " + this.classes.size() + " classes, writing model to --> '" + outputLocation +
            "', TIME --> " + ((System.currentTimeMillis() - startTime) / 1000.0) + "[s]");
    }

    /**
     * Sets the <code>mappingsUri</code> which is the URI to the sql types to
     * model type mappings.
     *
     * @param typeMappingsUri The typeMappings to set.
     */
    public void setTypeMappings(String typeMappingsUri)
    {
        try
        {
            this.typeMappings = Mappings.getInstance(typeMappingsUri);
        }
        catch (final Throwable throwable)
        {
            throw new SchemaTransformerException(throwable);
        }
    }

    /**
     * Sets the name of the package to which the model elements will be created.
     *
     * @param packageName The packageName to set.
     */
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    /**
     * Sets the name of the schema (where the tables can be found).
     *
     * @param schema The schema to set.
     */
    public void setSchema(String schema)
    {
        this.schema = schema;
    }

    /**
     * Sets the regular expression pattern to match on when deciding what table
     * names to add to the transformed XMI.
     *
     * @param tableNamePattern The tableNamePattern to set.
     */
    public void setTableNamePattern(String tableNamePattern)
    {
        this.tableNamePattern = StringUtils.trimToEmpty(tableNamePattern);
    }

    /**
     * The column name pattern.
     */
    private String columnNamePattern;

    /**
     * Sets the regular expression pattern to match on when deciding what attributes
     * ti create in the XMI.
     *
     * @param columnNamePattern The pattern for filtering the column name.
     */
    public void setColumnNamePattern(String columnNamePattern)
    {
        this.columnNamePattern = columnNamePattern;
    }

    /**
     * Sets the stereotype name for the new classes.
     *
     * @param classStereotypes The classStereotypes to set.
     */
    public void setClassStereotypes(String classStereotypes)
    {
        this.classStereotypes = StringUtils.deleteWhitespace(classStereotypes);
    }

    /**
     * Specifies the identifier stereotype.
     */
    private String identifierStereotypes = null;

    /**
     * Sets the stereotype name for the identifiers on the new classes.
     *
     * @param identifierStereotypes The identifierStereotypes to set.
     */
    public void setIdentifierStereotypes(String identifierStereotypes)
    {
        this.identifierStereotypes = StringUtils.deleteWhitespace(identifierStereotypes);
    }

    /**
     * Sets the name of the column tagged value to use for storing the name of
     * the column.
     *
     * @param columnTaggedValue The columnTaggedValue to set.
     */
    public void setColumnTaggedValue(String columnTaggedValue)
    {
        this.columnTaggedValue = StringUtils.trimToEmpty(columnTaggedValue);
    }

    /**
     * Sets the name of the table tagged value to use for storing the name of
     * the table.
     *
     * @param tableTaggedValue The tableTaggedValue to set.
     */
    public void setTableTaggedValue(String tableTaggedValue)
    {
        this.tableTaggedValue = StringUtils.trimToEmpty(tableTaggedValue);
    }

    
    public void setAttributeTaggedValues(String taggedValues) {
        if (StringUtils.isNotEmpty(taggedValues)) {
            StringTokenizer tokList = new StringTokenizer(taggedValues, ",");
            while (tokList.hasMoreTokens()) {
               String tok = tokList.nextToken();
               String parts[] = StringUtils.split(tok, "=");
               if (parts.length == 2) {
                   String tag = parts[0];
                   String value = parts[1];
                   attributeTaggedValues.put(tag, value);
               }
            } // while
        }
    }
    
    
    /**
     * Sets the version of XMI that will be produced.
     *
     * @param xmiVersion The xmiVersion to set.
     */
    public void setXmiVersion(String xmiVersion)
    {
        this.xmiVersion = xmiVersion;
    }

    /**
     * The package that is currently being processed.
     */
    private UmlPackage umlPackage;

    /**
     * The model thats currently being processed
     */
    private Model model;
    

    /**
     * Performs the actual translation of the Schema to the XMI and returns the
     * object model.
     */
    private final Object transform(final Connection connection)
        throws Exception
    {
        this.umlPackage = (UmlPackage)this.repository.getModel().getModel();

        final ModelManagementPackage modelManagementPackage = umlPackage.getModelManagement();

        final Collection models = modelManagementPackage.getModel().refAllOfType();
        if (models != null && !models.isEmpty())
        {
            // A given XMI file can contain multiple models.
            // Use the first model in the XMI file
            this.model = (Model)models.iterator().next();
        }
        else
        {
            this.model = modelManagementPackage.getModel().createModel();
        }

        // create the package on the model
        org.omg.uml.modelmanagement.UmlPackage leafPackage =
            this.getOrCreatePackage(
                umlPackage.getModelManagement(),
                model,
                this.packageName);
        this.createClasses(
            connection,
            umlPackage.getModelManagement().getCore(),
            leafPackage);

        return umlPackage;
    }

    /**
     * Gets or creates a package having the specified <code>packageName</code>
     * using the given <code>modelManagementPackage</code>, places it on the
     * <code>model</code> and returns the last leaf package.
     *
     * @param modelManagementPackage from which we retrieve the UmlPackageClass
     *        to create a UmlPackage.
     * @param modelPackage the root UmlPackage
     */
    protected org.omg.uml.modelmanagement.UmlPackage getOrCreatePackage(
        ModelManagementPackage modelManagementPackage,
        org.omg.uml.modelmanagement.UmlPackage modelPackage,
        String packageName)
    {
        packageName = StringUtils.trimToEmpty(packageName);
        if (StringUtils.isNotEmpty(packageName))
        {
            String[] packages = packageName.split(Schema2XMIGlobals.PACKAGE_SEPERATOR);
            if (packages != null && packages.length > 0)
            {
                for (int ctr = 0; ctr < packages.length; ctr++)
                {
                    Object umlPackage = ModelElementFinder.find(modelPackage, packages[ctr]);

                    if (umlPackage == null)
                    {
                        umlPackage =
                            modelManagementPackage.getUmlPackage().createUmlPackage(
                                packages[ctr], VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
                        modelPackage.getOwnedElement().add(umlPackage);
                    }
                    modelPackage = (org.omg.uml.modelmanagement.UmlPackage)umlPackage;
                }
            }
        }
        return modelPackage;
    }

    /**
     * Creates all classes from the tables found in the schema.
     *
     * @param connection the Connection used to retrieve the schema metadata.
     * @param corePackage the CorePackage instance we use to create the classes.
     * @param modelPackage the package which the classes are added.
     */
    protected void createClasses(
        Connection connection,
        CorePackage corePackage,
        org.omg.uml.modelmanagement.UmlPackage modelPackage)
        throws SQLException
    {
        DatabaseMetaData metadata = connection.getMetaData();
        ResultSet tableRs = metadata.getTables(
                null,
                this.schema,
                null,
                new String[] {"TABLE"});

        // loop through and create all classes and store then
        // in the classes Map keyed by table
        while (tableRs.next())
        {
            String tableName = tableRs.getString("TABLE_NAME");
            if (StringUtils.isNotBlank(this.tableNamePattern))
            {
                if (tableName.matches(this.tableNamePattern))
                {
                    UmlClass umlClass = this.createClass(modelPackage, metadata, corePackage, tableName);
                    this.classes.put(tableName, umlClass);
                }
            }
            else
            {
                UmlClass umlClass = this.createClass(modelPackage, metadata, corePackage, tableName);
                this.classes.put(tableName, umlClass);
            }
        }
        DbUtils.closeQuietly(tableRs);
        if (this.classes.isEmpty())
        {
            String schemaName = "";
            if (StringUtils.isNotEmpty(this.schema))
            {
                schemaName = " '" + this.schema + "' ";
            }
            StringBuffer warning = new StringBuffer("WARNING! No tables found in schema");
            warning.append(schemaName);
            if (StringUtils.isNotEmpty(this.tableNamePattern))
            {
                warning.append(" matching pattern --> '" + this.tableNamePattern + "'");
            }
            logger.warn(warning);
        }

        // add all attributes and associations to the modelPackage
        Iterator tableNameIt = this.classes.keySet().iterator();
        while (tableNameIt.hasNext())
        {
            String tableName = (String)tableNameIt.next();
            UmlClass umlClass = (UmlClass)classes.get(tableName);
            if (logger.isInfoEnabled())
            {
                logger.info("created class --> '" + umlClass.getName() + "'");
            }

            // create and add all associations to the package
            modelPackage.getOwnedElement().addAll(this.createAssociations(metadata, corePackage, tableName));

            // create and add all the attributes
            umlClass.getFeature().addAll(this.createAttributes(metadata, corePackage, tableName));

            modelPackage.getOwnedElement().add(umlClass);
        }
    }

    /**
     * Creates and returns a UmlClass with the given <code>name</code> using
     * the <code>corePackage</code> to create it.
     *
     * @param corePackage used to create the class.
     * @param tableName to tableName for which we'll create the appropriate
     *        class.
     * @return the UmlClass
     */
    protected UmlClass createClass(
        org.omg.uml.modelmanagement.UmlPackage modelPackage,
        DatabaseMetaData metadata,
        CorePackage corePackage,
        String tableName)
    {
        String className = SqlToModelNameFormatter.toClassName(tableName);
        UmlClass umlClass =
            corePackage.getUmlClass().createUmlClass(
                className, VisibilityKindEnum.VK_PUBLIC, false, false, false, false, false);

        umlClass.getStereotype().addAll(this.getOrCreateStereotypes(corePackage, this.classStereotypes, "Classifier"));

        if (StringUtils.isNotEmpty(this.tableTaggedValue))
        {
            // add the tagged value for the table name
            TaggedValue taggedValue = this.createTaggedValue(corePackage, this.tableTaggedValue, tableName);
            if (taggedValue != null)
            {
                umlClass.getTaggedValue().add(taggedValue);
            }
        }

        return umlClass;
    }

    /**
     * Creates and returns a collection of attributes from creating an attribute
     * from every column on the table having the give <code>tableName</code>.
     *
     * @param metadata the DatabaseMetaData from which to retrieve the columns.
     * @param corePackage used to create the class.
     * @param tableName the tableName for which to find columns.
     * @return the collection of new attributes.
     */
    protected Collection createAttributes(
        DatabaseMetaData metadata,
        CorePackage corePackage,
        String tableName)
        throws SQLException
    {
        final Collection attributes = new ArrayList();
        final ResultSet columnRs = metadata.getColumns(null, this.schema, tableName, null);
        final Collection primaryKeyColumns = this.getPrimaryKeyColumns(metadata, tableName);
        
        ResultSetMetaData colMeta = columnRs.getMetaData();
        int colCount = colMeta.getColumnCount();

        while (columnRs.next())
        {
            final String columnName = columnRs.getString("COLUMN_NAME");

            if (logger.isDebugEnabled()) {
                for (int c = 1; c <= colCount; c++) {
                    logger.debug("Meta column " + colMeta.getColumnName(c) + " = " + columnRs.getString(c));
                }
            }

            if (this.columnNamePattern == null || columnName.matches(this.columnNamePattern))
            {
                final String attributeName = SqlToModelNameFormatter.toAttributeName(columnName);
                if (logger.isInfoEnabled())
                {
                    logger.info("adding attribute --> '" + attributeName + "'");
                }

                // do NOT add foreign key columns as attributes (since
                // they are placed on association ends)
                if (!this.hasForeignKey(tableName, columnName))
                {
                    Classifier typeClass = null;

                    // first we try to find a mapping that maps to the
                    // database proprietary type
                    String typeName = columnRs.getString(this.getMetaColumnTypeName());
                    String colSize = columnRs.getString(this.getMetaColumnColumnSize());
                    String decPlaces = null;
                    if (StringUtils.isNotEmpty(this.getMetaColumnDecPlaces()))
                    {
                        decPlaces = columnRs.getString(this.getMetaColumnDecPlaces());
                    }

                    String type =
                        Schema2XMIUtils.constructTypeName(
                            typeName,
                            colSize, decPlaces);
                    logger.info("  -  searching for type mapping '" + type + "'");
                    if (typeMappings.containsFrom(type))
                    {
                        typeClass = this.getOrCreateDataType(corePackage, type);
                    }

                    // - See if we can find a type matching a mapping for a JDBC type 
                    //   (if we haven't found a database specific one)
                    if (typeClass == null)
                    {
                        type = JdbcTypeFinder.find(columnRs.getInt("DATA_TYPE"));
                        logger.info("  -  searching for type mapping '" + type + "'");
                        if (typeMappings.containsFrom(type))
                        {
                            typeClass = this.getOrCreateDataType(corePackage, type);
                        }
                        else
                        {
                            logger.warn("  !  no mapping found, type not added to '" + attributeName + "'");
                        }
                    }

                    boolean required = !this.isColumnNullable(metadata, tableName, columnName);

                    Attribute attribute =
                        corePackage.getAttribute().createAttribute(
                            attributeName,
                            VisibilityKindEnum.VK_PUBLIC,
                            false,
                            ScopeKindEnum.SK_INSTANCE,
                            this.createAttributeMultiplicity(
                                corePackage.getDataTypes(),
                                required),
                            ChangeableKindEnum.CK_CHANGEABLE,
                            ScopeKindEnum.SK_CLASSIFIER,
                            OrderingKindEnum.OK_UNORDERED,
                            null);
                    attribute.setType(typeClass);

                    if (StringUtils.isNotEmpty(this.columnTaggedValue))
                    {
                        // add the tagged value for the column name
                        TaggedValue taggedValue =
                            this.createTaggedValue(corePackage, this.columnTaggedValue, columnName);
                        if (taggedValue != null)
                        {
                            attribute.getTaggedValue().add(taggedValue);
                        }
                    }
                    
                    // Add the attribute specific tagged values (if any)...
                    if (attributeTaggedValues.size() > 0) 
                    {
                        Set keys = attributeTaggedValues.keySet();
                        Iterator iter = keys.iterator();
                        while (iter.hasNext())
                        {
                            String tag = (String)iter.next();
                            String value = (String)attributeTaggedValues.get(tag);
                            TaggedValue taggedValue =
                                this.createTaggedValue(corePackage, tag, value);
                            if (taggedValue != null)
                            {
                                attribute.getTaggedValue().add(taggedValue);
                            }
                        } // while
                    }
                    
                    if (primaryKeyColumns.contains(columnName))
                    {
                        attribute.getStereotype().addAll(
                            this.getOrCreateStereotypes(corePackage, this.identifierStereotypes, "Attribute"));
                    }
                    attributes.add(attribute);
                }
            }
        }
        DbUtils.closeQuietly(columnRs);
        return attributes;
    }

    
    public void setMetaColumnDecPlaces(String metaColumnDecPlaces) {
        this.metaColumnDecPlaces = metaColumnDecPlaces;
    }

    public String getMetaColumnDecPlaces() {
        return metaColumnDecPlaces;
    }

    public void setMetaColumnColumnSize(String metaColumnColumnSize) {
        this.metaColumnColumnSize = metaColumnColumnSize;
    }

    public String getMetaColumnColumnSize() {
        return metaColumnColumnSize;
    }

    public void setMetaColumnTypeName(String metaColumnTypeName) {
        this.metaColumnTypeName = metaColumnTypeName;
    }
    
    public String getMetaColumnTypeName() {
        return metaColumnTypeName;
    }

    /**
     * Gets or creates a new data type instance having the given fully qualified
     * <code>type</code> name.
     *
     * @param corePackage the core package
     * @param type the fully qualified type name.
     * @return the DataType
     */
    protected DataType getOrCreateDataType(
        CorePackage corePackage,
        String type)
    {
        type = this.typeMappings.getTo(type);
        Object datatype = ModelElementFinder.find(this.model, type);
        if (datatype == null || !DataType.class.isAssignableFrom(datatype.getClass()))
        {
            String[] names = type.split(Schema2XMIGlobals.PACKAGE_SEPERATOR);
            if (names != null && names.length > 0)
            {
                // the last name is the type name
                String typeName = names[names.length - 1];
                names[names.length - 1] = null;
                String packageName = StringUtils.join(names, Schema2XMIGlobals.PACKAGE_SEPERATOR);
                org.omg.uml.modelmanagement.UmlPackage umlPackage =
                    this.getOrCreatePackage(
                        this.umlPackage.getModelManagement(),
                        this.model,
                        packageName);
                if (umlPackage != null)
                {
                    datatype =
                        corePackage.getDataType().createDataType(
                            typeName, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);
                    umlPackage.getOwnedElement().add(datatype);
                }
            }
        }
        return (DataType)datatype;
    }

    /**
     * This method just checks to see if a column is null able or not, if so,
     * returns true, if not returns false.
     *
     * @param metadata the DatabaseMetaData instance used to retrieve the column
     *        information.
     * @param tableName the name of the table on which the column exists.
     * @param columnName the name of the column.
     * @param true/false on whether or not column is nullable.
     */
    protected boolean isColumnNullable(
        DatabaseMetaData metadata,
        String tableName,
        String columnName)
        throws SQLException
    {
        boolean nullable = true;
        ResultSet columnRs = metadata.getColumns(null, this.schema, tableName, columnName);
        while (columnRs.next())
        {
            nullable = columnRs.getInt("NULLABLE") != DatabaseMetaData.attributeNoNulls;
        }
        DbUtils.closeQuietly(columnRs);
        return nullable;
    }

    /**
     * Returns a collection of all primary key column names for the given
     * <code>tableName</code>.
     *
     * @param metadata
     * @param tableName
     * @return collection of primary key names.
     */
    protected Collection getPrimaryKeyColumns(
        DatabaseMetaData metadata,
        String tableName)
        throws SQLException
    {
        Collection primaryKeys = new HashSet();
        ResultSet primaryKeyRs = metadata.getPrimaryKeys(null, this.schema, tableName);
        while (primaryKeyRs.next())
        {
            primaryKeys.add(primaryKeyRs.getString("COLUMN_NAME"));
        }
        DbUtils.closeQuietly(primaryKeyRs);
        return primaryKeys;
    }

    /**
     * Creates and returns a collection of associations by determing foreign
     * tables to the table having the given <code>tableName</code>.
     *
     * @param metadata the DatabaseMetaData from which to retrieve the columns.
     * @param corePackage used to create the class.
     * @param tableName the tableName for which to find columns.
     * @return the collection of new attributes.
     */
    protected Collection createAssociations(
        DatabaseMetaData metadata,
        CorePackage corePackage,
        String tableName)
        throws SQLException
    {
        Collection primaryKeys = this.getPrimaryKeyColumns(metadata, tableName);
        Collection associations = new ArrayList();
        ResultSet columnRs = metadata.getImportedKeys(null, this.schema, tableName);
        while (columnRs.next())
        {
            // store the foreign key in the foreignKeys Map
            String fkColumnName = columnRs.getString("FKCOLUMN_NAME");
            this.addForeignKey(tableName, fkColumnName);

            // now create the association
            String foreignTableName = columnRs.getString("PKTABLE_NAME");
            UmlAssociation association =
                corePackage.getUmlAssociation().createUmlAssociation(
                    null, VisibilityKindEnum.VK_PUBLIC, false, false, false, false);

            // we set the upper range to 1 if the
            // they primary key of this table is the
            // foreign key of another table (by default
            // its set to a many multiplicity)
            int primaryUpper = -1;
            if (primaryKeys.contains(fkColumnName))
            {
                primaryUpper = 1;
            }

            String endName = null;

            // primary association
            AssociationEnd primaryEnd =
                corePackage.getAssociationEnd().createAssociationEnd(
                    endName,
                    VisibilityKindEnum.VK_PUBLIC,
                    false,
                    true,
                    OrderingKindEnum.OK_UNORDERED,
                    AggregationKindEnum.AK_NONE,
                    ScopeKindEnum.SK_INSTANCE,
                    this.createMultiplicity(
                        corePackage.getDataTypes(),
                        0,
                        primaryUpper),
                    ChangeableKindEnum.CK_CHANGEABLE);
            primaryEnd.setParticipant((Classifier)this.classes.get(tableName));
            association.getConnection().add(primaryEnd);

            boolean required = !this.isColumnNullable(metadata, tableName, fkColumnName);

            int foreignLower = 0;
            if (required)
            {
                foreignLower = 1;
            }

            int deleteRule = columnRs.getInt("DELETE_RULE");

            // determine if we should have composition for
            // the foreign association end depending on cascade delete
            AggregationKindEnum foreignAggregation = AggregationKindEnum.AK_NONE;
            if (deleteRule == DatabaseMetaData.importedKeyCascade)
            {
                foreignAggregation = AggregationKindEnum.AK_COMPOSITE;
            }

            // foriegn association
            AssociationEnd foreignEnd =
                corePackage.getAssociationEnd().createAssociationEnd(
                    endName,
                    VisibilityKindEnum.VK_PUBLIC,
                    false,
                    true,
                    OrderingKindEnum.OK_UNORDERED,
                    foreignAggregation,
                    ScopeKindEnum.SK_INSTANCE,
                    this.createMultiplicity(
                        corePackage.getDataTypes(),
                        foreignLower,
                        1),
                    ChangeableKindEnum.CK_CHANGEABLE);
            final Classifier foreignParticipant = (Classifier)this.classes.get(foreignTableName);
            if (foreignParticipant == null)
            {
                throw new SchemaTransformerException(
                    "The associated table '" + foreignTableName +
                    "' must be available in order to create the association");
            }
            foreignEnd.setParticipant(foreignParticipant);

            if (StringUtils.isNotEmpty(this.columnTaggedValue))
            {
                // add the tagged value for the foreign association end
                TaggedValue taggedValue = this.createTaggedValue(corePackage, this.columnTaggedValue, fkColumnName);
                if (taggedValue != null)
                {
                    foreignEnd.getTaggedValue().add(taggedValue);
                }
            }

            association.getConnection().add(foreignEnd);
            associations.add(association);

            if (logger.isInfoEnabled())
            {
                logger.info(
                    "adding association: '" + primaryEnd.getParticipant().getName() + " <--> " +
                    foreignEnd.getParticipant().getName() + "'");
            }
        }
        DbUtils.closeQuietly(columnRs);
        return associations;
    }

    /**
     * Creates a tagged value given the specfied <code>name</code>.
     *
     * @param name the name of the tagged value to create.
     * @param value the value to populate on the tagged value.
     * @return returns the new TaggedValue
     */
    protected TaggedValue createTaggedValue(
        CorePackage corePackage,
        String name,
        String value)
    {
        Collection values = new HashSet();
        values.add(value);
        TaggedValue taggedValue =
            corePackage.getTaggedValue().createTaggedValue(name, VisibilityKindEnum.VK_PUBLIC, false, values);

        // see if we can find the tag defintion and if so add that
        // as the type.
        Object tagDefinition = ModelElementFinder.find(this.umlPackage, name);
        if (tagDefinition != null && TagDefinition.class.isAssignableFrom(tagDefinition.getClass()))
        {
            taggedValue.setType((TagDefinition)tagDefinition);
        }
        return taggedValue;
    }

    /**
     * Gets or creates a stereotypes given the specfied comma separated list of
     * <code>names</code>. If any of the stereotypes can't be found, they
     * will be created.
     *
     * @param names comma separated list of stereotype names
     * @param baseClass the base class for which the stereotype applies.
     * @return Collection of Stereotypes
     */
    protected Collection getOrCreateStereotypes(
        CorePackage corePackage,
        String names,
        String baseClass)
    {
        Collection stereotypes = new HashSet();
        String[] stereotypeNames = null;
        if (names != null)
        {
            stereotypeNames = names.split(",");
        }
        if (stereotypeNames != null && stereotypeNames.length > 0)
        {
            for (int ctr = 0; ctr < stereotypeNames.length; ctr++)
            {
                String name = StringUtils.trimToEmpty(stereotypeNames[ctr]);

                // see if we can find the stereotype first
                Object stereotype = ModelElementFinder.find(this.umlPackage, name);
                if (stereotype == null || !Stereotype.class.isAssignableFrom(stereotype.getClass()))
                {
                    Collection baseClasses = new ArrayList();
                    baseClasses.add(baseClass);
                    stereotype =
                        corePackage.getStereotype().createStereotype(
                            name, VisibilityKindEnum.VK_PUBLIC, false, false, false, false, null, baseClasses);
                    this.model.getOwnedElement().add(stereotype);
                }
                stereotypes.add(stereotype);
            }
        }
        return stereotypes;
    }

    /**
     * Adds a foreign key column name to the <code>foreignKeys</code> Map. The
     * map stores a collection of foreign key names keyed by the given
     * <code>tableName</code>
     *
     * @param tableName the name of the table for which to store the keys.
     * @param columnName the name of the foreign key column name.
     */
    protected void addForeignKey(
        String tableName,
        String columnName)
    {
        if (StringUtils.isNotBlank(tableName) && StringUtils.isNotBlank(columnName))
        {
            Collection foreignKeys = (Collection)this.foreignKeys.get(tableName);
            if (foreignKeys == null)
            {
                foreignKeys = new HashSet();
            }
            foreignKeys.add(columnName);
            this.foreignKeys.put(tableName, foreignKeys);
        }
    }

    /**
     * Returns true if the table with the given <code>tableName</code> has a
     * foreign key with the specified <code>columnName</code>.
     *
     * @param tableName the name of the table to check for the foreign key
     * @param columnName the naem of the foreign key column.
     * @return true/false dependeing on whether or not the table has the foreign
     *         key with the given <code>columnName</code>.
     */
    protected boolean hasForeignKey(
        String tableName,
        String columnName)
    {
        boolean hasForeignKey = false;
        if (StringUtils.isNotBlank(tableName) && StringUtils.isNotBlank(columnName))
        {
            Collection foreignKeys = (Collection)this.foreignKeys.get(tableName);
            if (foreignKeys != null)
            {
                hasForeignKey = foreignKeys.contains(columnName);
            }
        }
        return hasForeignKey;
    }

    /**
     * Creates an attributes multiplicity, if <code>required</code> is true,
     * then multiplicity is set to 1, if <code>required</code> is false, then
     * multiplicity is set to 0..1.
     *
     * @param dataTypes used to create the Multiplicity
     * @param required whether or not the attribute is required therefore
     *        determining the multiplicity value created.
     * @return the new Multiplicity
     */
    protected Multiplicity createAttributeMultiplicity(
        DataTypesPackage dataTypes,
        boolean required)
    {
        Multiplicity mult = null;
        if (required)
        {
            mult = this.createMultiplicity(dataTypes, 1, 1);
        }
        else
        {
            mult = this.createMultiplicity(dataTypes, 0, 1);
        }
        return mult;
    }

    /**
     * Creates a multiplicity, from <code>lower</code> and <code>upper</code>
     * ranges.
     *
     * @param dataTypes used to create the Multiplicity
     * @param lower the lower range of the multiplicity
     * @param upper the upper range of the multiplicity
     * @return the new Multiplicity
     */
    protected Multiplicity createMultiplicity(
        DataTypesPackage dataTypes,
        int lower,
        int upper)
    {
        Multiplicity mult = dataTypes.getMultiplicity().createMultiplicity();
        MultiplicityRange range = dataTypes.getMultiplicityRange().createMultiplicityRange(lower, upper);
        mult.getRange().add(range);
        return mult;
    }

}