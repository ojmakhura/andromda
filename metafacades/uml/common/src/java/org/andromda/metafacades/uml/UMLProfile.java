package org.andromda.metafacades.uml;

/**
 * Contains the common UML AndroMDA profile.  That is, it contains
 * elements "common" to all AndroMDA components (tagged values, and stereotypes).
 * 
 * @author Chad Brandon
 */
public class UMLProfile {

	/* ----------------- Stereotypes -------------------- */
	
	/**
	 * Represents a persistent entity.
	 */
	public static final String STEREOTYPE_ENTITY = "Entity";
	
	/**
	 * Represents a finder method on an entity.
	 */
	public static final String STEREOTYPE_FINDER_METHOD = "FinderMethod";
	
	/**
	 * Represents the primary key of an entity.
	 */
	public static final String STEREOTYPE_IDENTIFIER = "PrimaryKey";
	
	/**
	 * Represents a service.
	 */
	public static final String STEREOTYPE_SERVICE = "Service";
    
    /**
     * Represents an exception.
     */
    public static final String STEREOTYPE_EXCEPTION = "Exception";
	
	/* ----------------- Tagged Values -------------------- */
	
	/**
	 * Represents documentation stored as a tagged value
	 */
	public static final String TAGGEDVALUE_DOCUMENTATION = "documentation";
	
	/**
	 * Represents a JDBC type definition in the model.
	 */
	public static final String TAGGEDVALUE_PERSISTENCE_JDBCTYPE = "andromda.persistence.JDBCType";
	
	/**
	 * Represents a SQL type definition in the model.
	 */
	public static final String TAGGEDVALUE_PERSISTENCE_SQLTYPE = "andromda.persistence.SQLType";
	
	/**
	 * Represents a SQL table name for entity persistence.
	 */
	public static final String TAGGEDVALUE_PERSISTENCE_TABLE = "andromda.persistence.table";
	
	/**
	 * Represents a SQL table column name for entity persistence.
	 */
	public static final String TAGGEDVALUE_PERSISTENCE_COLUMN = "andromda.persistence.column";
	
	/**
	 * Represents a SQL table column length
	 */
	public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH = "andromda.persistence.column.length";
	
}
