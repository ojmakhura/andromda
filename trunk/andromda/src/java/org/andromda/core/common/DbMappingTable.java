package org.andromda.core.common;

import java.io.File;
import java.io.IOException;

/**
 * An interface for objects responsible for mapping types in the object model to JDBC and
 * SQL database types.
 * 
 * <p> The mappings might change based upon the database being used and/or
 * a set of project based peferences for how to map types to the database. <p>
 * 
 * @author Matthias Bohlen
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 *
 */
public interface DbMappingTable
{
   
	/**
	 * reads the mapping rules from an XML file.
     * 
	 * @param mappingConfig file for which to read the mapping rules
	 * @throws RepositoryReadException thrown if an error is encountered during parsing
	 * @throws IOException thrown if an IO error occurs while reading the file
	 */
    public void read(File mappingConfig)
        throws RepositoryReadException, IOException;
    
	/**
	* Returns the JDBC type for the given Java type.
    *
	* @param javaType name of the Java type (e.g. "java.util.Date")
	* @return String name of the JDBC type (e.g. "DATE")
	*/
	public String getJDBCType(String javaType);

	/**
	 * Returns the SQL type for the fiven Java type.
	 * See your database docs for this syntax.
	 * @param javaType name of the Java type (e.g. "java.math.BigDecimal")
	 * @param sqlFieldLength desired field length in the database table
	 * @return String the complete SQL field syntax (e.g. "DECIMAL(9)")
	 */
	public String getSQLType(String javaType, String desiredFieldLength);

}
