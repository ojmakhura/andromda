package org.andromda.adminconsole.maintenance;

import org.andromda.adminconsole.db.*;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @see org.andromda.adminconsole.maintenance.MaintenanceController
 */
public class MaintenanceControllerImpl extends MaintenanceController
{
    /**
     * @see org.andromda.adminconsole.maintenance.MaintenanceController#loadTableData
     */
    public final void loadTableData(ActionMapping mapping, org.andromda.adminconsole.maintenance.LoadTableDataForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        MetaDataSession metadataSession = getMetaDataSession(request);

        final Table table = (Table)metadataSession.getTables().get( form.getTable() );
        if (table == null)
        {
            throw new IllegalArgumentException("Table could not be located: "+form.getTable());
        }
        else
        {
            metadataSession.setCurrentTable( table );
            form.setTableData( table.findAllRows() );
        }

        form.setTableMetaData(table);
        form.setTableValueList( getMetaDataSession(request).getTableNames().toArray() );
    }

    /**
     * @see org.andromda.adminconsole.maintenance.MaintenanceController#loadTables
     */
    public final void loadTables(ActionMapping mapping, org.andromda.adminconsole.maintenance.LoadTablesForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        DatabaseLoginSession loginSession = getDatabaseLoginSession(request);

        Database database = DatabaseFactory.create(
                loginSession.getUrl(), loginSession.getSchema(),
                loginSession.getUser(), loginSession.getPassword());
        
        Table[] tables = database.getTables(new TableType[]{TableType.TABLE});

        Map tableMap = new LinkedHashMap();
        for (int i = 0; i < tables.length; i++)
        {
            Table table = tables[i];
            tableMap.put(table.getName(), table);
        }

        Object[] tableNames = tableMap.keySet().toArray();
        Arrays.sort(tableNames);

        MetaDataSession metadataSession = getMetaDataSession(request);
        metadataSession.setTables( tableMap );
        metadataSession.setTableNames( Arrays.asList(tableNames) );

        if (tables.length < 1)
        {
            throw new Exception("No tables could be found");
        }
        else
        {
            form.setTable( tables[0].getName() );
            metadataSession.setCurrentTable( tables[0] );
        }

    }

    public void registerLoginInformation(ActionMapping mapping, RegisterLoginInformationForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        DatabaseLoginSession loginSession = getDatabaseLoginSession(request);

        loginSession.setUrl( StringUtils.trim(form.getUrl()) );
        loginSession.setSchema( StringUtils.trimToNull(form.getSchema()) );
        loginSession.setUser( StringUtils.trimToEmpty(form.getUser()) );
        loginSession.setPassword( StringUtils.trimToEmpty(form.getPassword()) );
    }

    public void sortTableData(ActionMapping mapping, SortTableDataForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final String columnName = form.getColumnName();
        final Table table = getMetaDataSession(request).getCurrentTable();

        int columnIndex = findColumnIndex(table, columnName);

        List tableData = table.findAllRows();

        // only sort when we can actually compare values
        if (table.getColumns()[columnIndex].isComparable())
        {
            Comparator comparator = new RowDataComparator(columnIndex);
            Collections.sort(tableData, comparator);
        }

        form.setTableMetaData(table);
        form.setTableData(tableData);
        form.setTable(table.getName());
        form.setTableValueList( getMetaDataSession(request).getTableNames().toArray() );
    }

    private int findColumnIndex(Table table, String columnName)
    {
        String[] columnNames = table.getColumnNames();
        for (int i = 0; i < columnNames.length; i++)
        {
            String name = columnNames[i];
            if (columnName.equals(name))
            {
                return i;
            }
        }
        return 0;
    }

    private class RowDataComparator implements Comparator
    {
        private int columnIndex = 0;

        public RowDataComparator()
        {
            columnIndex = 0;
        }

        public RowDataComparator(int columnIndex)
        {
            this.columnIndex = columnIndex;
        }

        public int compare(Object o1, Object o2)
        {
            RowData rowData1 = (RowData)o1;
            RowData rowData2 = (RowData)o2;

            Comparable cell1 = (Comparable)rowData1.getCell(columnIndex);
            Comparable cell2 = (Comparable)rowData2.getCell(columnIndex);

            return cell1.compareTo(cell2);
        }
    }
}
