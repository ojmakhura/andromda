package org.andromda.adminconsole.db;

import java.sql.SQLException;
import java.util.List;

/**
 * A column that holds foreign keys
 */
public interface ForeignKeyColumn extends Column
{
    /**
     * @return the name of the table that is imported by this column
     */
    public String getImportedTableName();

    /**
     * @return the name of the primary column of the imported table
     */
    public String getImportedColumnName();

    /**
     * Fetches a specific value in the imported table, the value will be searched in the
     * imported column while the column value must match the given value.
     */
    public ForeignValue getForeignValue(Object value) throws SQLException;

    /**
     * Fetches a specific value in the imported table, the value will be searched in the column with
     * the argument name while the imported column value (mostly a primary key column) must match
     * the given value.
     *
     * @param columnName can be null, in this case the imported column name will be assumed
     */
    public ForeignValue getForeignValue(String columnName, Object value) throws SQLException;

    /**
     * Gets all values for the imported column.
     *
     * @return ForeignValue instances
     */
    public List getForeignValues() throws SQLException;

    /**
     * Gets all values for the column with the specified name from the imported table..
     *
     * @param columnName can be null, in this case the imported column name will be assumed
     * @return ForeignValue instances
     */
    public List getForeignValues(String columnName) throws SQLException;

    /**
     * This class represents a value targetted by a foreign key in this column
     */
    public final class ForeignValue
    {
        private Object primaryKey = null;
        private Object columnValue = null;

        public ForeignValue(Object primaryKey, Object columnValue)
        {
            this.primaryKey = primaryKey;
            this.columnValue = columnValue;
        }

        public Object getPrimaryKey()
        {
            return primaryKey;
        }

        public Object getColumnValue()
        {
            return columnValue;
        }

        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (!(o instanceof ForeignValue)) return false;

            final ForeignValue foreignValue = (ForeignValue) o;

            if (columnValue != null ? !columnValue.equals(foreignValue.columnValue) : foreignValue.columnValue != null) return false;
            if (primaryKey != null ? !primaryKey.equals(foreignValue.primaryKey) : foreignValue.primaryKey != null) return false;

            return true;
        }

        public int hashCode()
        {
            int result;
            result = (primaryKey != null ? primaryKey.hashCode() : 0);
            result = 29 * result + (columnValue != null ? columnValue.hashCode() : 0);
            return result;
        }
    }
}
