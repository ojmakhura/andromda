package org.andromda.metafacades.uml;

/**
 * Stores the datatypes used throughout the UML Metafacades.
 * 
 * @author Chad Brandon
 */
public class MetafacadeDataTypes
{  
    /**
     * Used to identify collection types in the model, any other type
     * that will be identified as a collection must specialize this type.
     */
    public static final String COLLECTION_TYPE_NAME = "datatype.Collection";
    
    /**
     * Used to identify a list type in the model, any other type
     * that will be identified as a list must specialize this type.
     */
    public static final String LIST_TYPE_NAME = "datatype.List";

    /**
     * Used to identify a set type in the model, any other type
     * that will be identified as a set must specialize this type.
     */
    public static final String SET_TYPE_NAME = "datatype.Set";

    /**
     * Used to identify date types in the model, any other type
     * that will be identified as a date must specialize this type.
     */
    public static final String DATE_TYPE_NAME = "datatype.Date";
   
    /**
     * Used to indentify a boolean type in the model, any other type
     * that will be identified as a booleon type must specialize this type.
     */
    public static final String BOOLEAN_TYPE_NAME = "datatype.boolean";

    /**
     * Used to indentify a file type in the model, any other type
     * that will be identified as a file type must specialize this type.
     */
    public static final String FILE_TYPE_NAME = "datatype.File";
    
    /**
     * Used to indentify a Map type in the model, any other type
     * that will be identified as a Map type must specialize this type.
     */
    public static final String MAP_TYPE_NAME = "datatype.Map";
}