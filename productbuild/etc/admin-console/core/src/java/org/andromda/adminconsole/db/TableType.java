package org.andromda.adminconsole.db;

import java.io.Serializable;

/**
 * Denotes a table's type.
 */
public class TableType implements Serializable
{
    /**
     * Denotes a table to be a view.
     */
    public final static TableType VIEW = new TableType("VIEW");

    /**
     * Denotes a table to be a real table.
     */
    public final static TableType TABLE = new TableType("TABLE");

    /**
     * This enumeration literal's internal value.
     */
    private final String value;

    /**
     * Constructs a new table type for the specified value.
     */
    private TableType(String value)
    {
        this.value = value;
    }

    /**
     * @return true if the argument object is also a TableType instance, and its
     *  value equals the value of this instance
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof TableType)
        {
            return ((TableType) obj).value == this.value;
        }
        return false;
    }

    /**
     * Given its value, returns the enumeration literal.
     */
    public static TableType get(String value)
    {
        if (VIEW.value.equals(value)) return VIEW;
        if (TABLE.value.equals(value)) return TABLE;
        throw new RuntimeException("TableType.get: Argument enumeration literal value unknown");
    }

    /**
     * @return this enumeration literal's internal value
     */
    public String getValue()
    {
        return value;
    }
}
