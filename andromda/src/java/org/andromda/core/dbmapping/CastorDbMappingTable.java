package org.andromda.core.dbmapping;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.andromda.core.common.DbMappingTable;
import org.andromda.core.common.RepositoryReadException;
import org.andromda.core.dbmapping.Mapping;
import org.andromda.core.dbmapping.Mappings;
import org.andromda.core.dbmapping.SqlType;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

/**
 * @author Anthony Mowers
 *
 */
public class CastorDbMappingTable implements DbMappingTable 
{
    private Map map = null;
    
	/**
	 * @see org.andromda.core.common.DbMappingTable#read(File)
	 */
	public void read(File mappingsFile)
		throws RepositoryReadException, IOException 
    {
        try {
            Reader reader = new FileReader(mappingsFile);
            initialize(Mappings.unmarshal(reader));
        } catch (ValidationException ve) 
        {
            throw new RepositoryReadException(
                "Validation error in TypeMappings file: " + mappingsFile,
                ve);
        } catch (MarshalException me) {
            throw new RepositoryReadException(
                "Marshalling error in TypeMappings file: " + mappingsFile,
                me);
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
        
        for (Enumeration i = mappings.enumerateMapping(); i.hasMoreElements(); )
        {
            Mapping mapping = (Mapping)i.nextElement();
            for (Enumeration j = mapping.enumerateType();j.hasMoreElements(); )
            {
                String type = (String)j.nextElement();
                map.put(type,mapping);
            }
        }
                
    }
}
