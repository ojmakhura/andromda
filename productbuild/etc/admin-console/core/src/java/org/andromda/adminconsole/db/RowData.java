package org.andromda.adminconsole.db;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a row instance in a table, may also represent only a part of a row by specifying not
 * all columns.
 * <p/>
 * This class extends <code>java.util.LinkedHashMap</code> because we want to retrieve values by
 * column name, and allow the column name to be specified as a String.
 */
public class RowData extends LinkedHashMap implements Serializable
{
    /**
     * The data values for this row.
     */
    private Object[] cells = null;

    /**
     * Constructs a row without any values.
     */
    public RowData()
    {
        cells = new Object[0];
    }

    /**
     * Constructs a new row backing the argument map.
     *
     * @param cellMap, must not be <code>null</code>
     */
    public RowData(Map cellMap)
    {
        super(cellMap);
    }

    /**
     * Constructs a new row for the designated table, having the argument data values.
     * The cell values will be assigned to the table columns in the order they are encountered.
     */
    public RowData(Table table, Object[] cells)
    {
        this.cells = cells;
        String[] columnNames = table.getColumnNames();
        for (int i = 0; i < cells.length; i++)
        {
            this.put(columnNames[i], cells[i]);
        }
    }

    /**
     * Gets the value at the argument index.
     *
     * @throws ArrayIndexOutOfBoundsException when the index value is less than zero, or exceeds
     *  the number of data values
     */
    public Object getCell(int index)
    {
        return cells[index];
    }

    /**
     * @return all data values, may be <code>null</code>
     */
    public Object[] getCells()
    {
        return cells;
    }
}
