package org.andromda.adminconsole.db;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class RowData extends LinkedHashMap implements Serializable
{
    private Object[] cells = null;

    public RowData()
    {
        cells = new Object[0];
    }

    public RowData(Map cellMap)
    {
        super(cellMap);
    }

    public RowData(Table table, Object[] cells)
    {
        this.cells = cells;
        String[] columnNames = table.getColumnNames();
        for (int i = 0; i < cells.length; i++)
        {
            this.put(columnNames[i], cells[i]);
        }
    }

    public Object getCell(int index)
    {
        return cells[index];
    }

    public Object[] getCells()
    {
        return cells;
    }
}
