package org.andromda.adminconsole.db;

import java.io.Serializable;

/**
 * Represents a column in a database.
 *
 * @author Wouter Zoons
 */
public interface Column extends Serializable
{
    /**
     * @return the name of this column.
     */
    public String getName();

    /**
     * @return true if this column is allowed to have no value assigned to it
     */
    public boolean isNullable();

    /**
     * @return this type of values this column holds
     */
    public Class getType();

    /**
     * @return the sql type for this column
     */
    public int getSqlType();

    /**
     * @return the maximum size a value can have in this column
     */
    public int getSize();

    /**
     * @return the metadata remarks for this column
     */
    public String getRemarks();

    /**
     * @return the position of this column in its table, compared to the other columns (if any)
     *  starts at zero (0)
     */
    public int getOrdinalPosition();

    /**
     * @return this column's table, can never be null
     */
    public Table getTable();

    /**
     * @return true if this column can only holds two different values
     */
    public boolean isBooleanType();

    /**
     * @return true if the values this column can hold are of a numeric type
     */
    public boolean isNumericType();

    /**
     * @return true if the values this column can hold are constructed out of characters
     */
    public boolean isStringType();

    /**
     * @return true if this column holds foreign keys, importing another table
     */
    public boolean isForeignKeyColumn();

    /**
     * @return true if the column holds the primary keys for this table
     */
    public boolean isPrimaryKeyColumn();
}
