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
     * The base exception stereotype.  If a model element is 
     * stereotyped with this (or one of its sub stereotypes), 
     * then it is excepted that a cartridge will generate an exception
     */
    public static final String STEREOTYPE_EXCEPTION = "Exception";
    
    /**
     * Represents exceptions thrown during normal application processing
     * (such as business exceptions).  It extends the base exception stereotype.
     */
    public static final String STEREOTYPE_APPLICATION_EXCEPTION = "ApplicationException";
    
    /**
     * Represents unexpected exceptions that can occur during application
     * processing.  This that a caller isn't expected to handle.
     */
    public static final String STEREOTYPE_UNEXPECTED_EXCEPTION = "UnexpectedException";
    
    /**
     * Represents a reference to an exception model element. 
     * Model dependencies to unstereotyped exception model elements 
     * can be stereotyped with this.  This allows the user to create 
     * a custom exception class since the exception itself will not 
     * be generated but the references to it will be (i.e. the throws
     * clause within an operation).
     */
    public static final String STEREOTYPE_EXCEPTION_REF = "ExceptionRef";
	
	/* ----------------- Tagged Values -------------------- */
	
	/**
	 * Represents documentation stored as a tagged value
	 */
	public static final String TAGGEDVALUE_DOCUMENTATION = "documentation";
	
	/**
	 * Represents a SQL table name for entity persistence.
	 */
	public static final String TAGGEDVALUE_PERSISTENCE_TABLE = "@persistence.table";
	
	/**
	 * Represents a SQL table column name for entity persistence.
	 */
	public static final String TAGGEDVALUE_PERSISTENCE_COLUMN = "@persistence.column";
	
	/**
	 * Represents a SQL table column length
	 */
	public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH = "@persistence.column.length";
	
}
