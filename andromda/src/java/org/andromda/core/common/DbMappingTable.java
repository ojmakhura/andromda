package org.andromda.core.common;

import java.io.File;
import java.io.IOException;

/**
 * Maps java types to JDBC and SQL data types.
 * 
 * <p>The mappings might change based upon the database being used and
 * a set of project based peferences for the mapping.</p>
 * 
 * @author Matthias Bohlen
 * @author Anthony Mowers
 *
 */
public interface DbMappingTable
{
   
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
