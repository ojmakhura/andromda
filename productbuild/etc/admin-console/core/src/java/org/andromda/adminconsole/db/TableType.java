package org.andromda.adminconsole.db;


public class TableType
{
    public final static TableType VIEW = new TableType("VIEW");
    public final static TableType TABLE = new TableType("TABLE");

    private String value = null;

    private TableType(String value)
    {
        this.value = value;
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof TableType)
        {
            return ((TableType) obj).value == this.value;
        }
        return false;
    }

    public static TableType get(String value)
    {
        if (VIEW.value.equals(value)) return VIEW;
        if (TABLE.value.equals(value)) return TABLE;
        return new TableType(value);
    }

    public String getValue()
    {
        return value;
    }
}
