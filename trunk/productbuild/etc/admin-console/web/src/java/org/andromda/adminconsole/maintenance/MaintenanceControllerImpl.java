package org.andromda.adminconsole.maintenance;

import org.andromda.adminconsole.config.AdminConsoleConfigurator;
import org.andromda.adminconsole.config.xml.ColumnConfiguration;
import org.andromda.adminconsole.db.*;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
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

        Table table = (Table)metadataSession.getTables().get( form.getName() );
        if (table == null)
        {
            table = metadataSession.getCurrentTable();
            if (table == null)
                throw new IllegalArgumentException("Table could not be located: "+form.getName());
        }

        metadataSession.setCurrentTable( table );
        metadataSession.setCurrentTableData( table.findAllRows() );
    }

    /**
     * @see org.andromda.adminconsole.maintenance.MaintenanceController#loadTables
     */
    public final void loadTables(ActionMapping mapping, org.andromda.adminconsole.maintenance.LoadTablesForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        DatabaseLoginSession loginSession = getDatabaseLoginSession(request);
        AdminConsoleConfigurator configurator = loginSession.getConfigurator();

        Database database = DatabaseFactory.create(
                loginSession.getUrl(), loginSession.getSchema(),
                loginSession.getUser(), loginSession.getPassword());

        boolean allowUnconfiguredTables = configurator.isUnconfiguredTablesAvailable();
        List knownTableNames = loginSession.getConfigurator().getKnownTableNames();

        Table[] tables = database.getTables(new TableType[]{TableType.TABLE});

        Map tableMap = new LinkedHashMap();
        for (int i = 0; i < tables.length; i++)
        {
            Table table = tables[i];
            if (allowUnconfiguredTables || knownTableNames.contains(table.getName()))
            {
                tableMap.put(table.getName(), table);
            }
        }

        Object[] tableNames = tableMap.keySet().toArray();
        Arrays.sort(tableNames);

        MetaDataSession metadataSession = getMetaDataSession(request);
        metadataSession.setTables( tableMap );
        metadataSession.setTableNames( Arrays.asList(tableNames) );

        if (tableMap.size() > 0)
        {
            metadataSession.setCurrentTable( (Table)tableMap.values().iterator().next() );
        }
        else
        {
            throw new Exception("No tables could be found");
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

    public void loadConfigurator(ActionMapping mapping, LoadConfiguratorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        getDatabaseLoginSession(request).setConfigurator(new AdminConsoleConfigurator());
    }

    private final static String COOKIE_NAME = "admin.console";
    private final static String COOKIE_VALUE_SEPARATOR = "::::";

    public void loadPreferences(ActionMapping mapping, LoadPreferencesForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        Cookie[] cookies = request.getCookies();

        boolean cookieLoaded = false;
        if (cookies != null)
        {
            for (int i = 0; i < cookies.length && !cookieLoaded; i++)
            {
                Cookie cookie = cookies[i];
                if (COOKIE_NAME.equals(cookie.getName()))
                {
                    String value = cookie.getValue();
                    int separatorIndex = value.indexOf(COOKIE_VALUE_SEPARATOR);

                    if (separatorIndex != -1)
                    {
                        String user = value.substring(0, separatorIndex);
                        String url = value.substring(separatorIndex + COOKIE_VALUE_SEPARATOR.length());

                        getDatabaseLoginSession(request).setPreferredUrl(url);

                        form.setUser(user);
                        form.setUrl(url);
                    }
                    cookieLoaded = true;
                }
            }
        }
    }

    public void storePreferences(ActionMapping mapping, StorePreferencesForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        Cookie cookie = new Cookie(COOKIE_NAME, form.getUser() + COOKIE_VALUE_SEPARATOR + form.getUrl());
        cookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(cookie);
    }

    public void deleteRows(ActionMapping mapping, DeleteRowsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // the list of rows selected for deletion
        Object[] rowNumbers = form.getSelectedRowsAsArray();

        // was something selected for deletion ?
        if (rowNumbers == null || rowNumbers.length==0)
        {
            return;
        }

        // the current metadata
        MetaDataSession metaDataSession = getMetaDataSession(request);
        List tableData = metaDataSession.getCurrentTableData();

        // if the table has a primary key use it to identify the row, otherwise use all columns
        Table table = metaDataSession.getCurrentTable();
        final Column[] identityColumns =
                (table.getPrimaryKeyColumnCount() > 0) ? table.getPrimaryKeyColumns() : table.getColumns();

        // collect all selected rows in a list
        List selectedData = new ArrayList();
        for (int i = 0; i < rowNumbers.length; i++)
        {
            int rowNumber = Integer.parseInt((String)rowNumbers[i]);
            selectedData.add( tableData.get(rowNumber) );
        }

        for (int i = 0; i < selectedData.size(); i++)
        {
            RowData rowData = (RowData) selectedData.get(i);
            // find out how this row is uniquely identified in its table
            Criterion identityCriterion = createCriterion(identityColumns, rowData);
            // delete this row
            table.deleteRow(identityCriterion);
        }
    }

    public void updateRows(ActionMapping mapping, UpdateRowsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // the list of rows selected for update
        Object[] rowNumbers = form.getSelectedRowsAsArray();

        // was something selected for update ?
        if (rowNumbers == null || rowNumbers.length==0)
        {
            return;
        }

        // the current metadata
        MetaDataSession metaDataSession = getMetaDataSession(request);
        List tableData = metaDataSession.getCurrentTableData();

        // if the table has a primary key use it to identify the row, otherwise use all columns
        Table table = metaDataSession.getCurrentTable();
        final Column[] identityColumns =
                (table.getPrimaryKeyColumnCount() > 0) ? table.getPrimaryKeyColumns() : table.getColumns();

        // get the columns we might need to set
        Column[] tableColumns = table.getColumns();

        // this object represents the new data we will persist
        RowData newRowData = new RowData();

        // get the configurator to find out which column is updateable
        AdminConsoleConfigurator configurator = getDatabaseLoginSession(request).getConfigurator();

        // loop over the rows the user selected for update
        for (int i = 0; i < rowNumbers.length; i++)
        {
            int rowNumber = Integer.parseInt((String)rowNumbers[i]);
            RowData rowData = (RowData) tableData.get(rowNumber);

            for (int j = 0; j < tableColumns.length; j++)
            {
                Column column = tableColumns[j];
                ColumnConfiguration columnConfiguration = configurator.getConfiguration(column);

                // only record the parameter when it is allowed for update
                if (columnConfiguration.getUpdateable())
                {
                    String parameter = request.getParameter(rowNumber + ":" + column.getName());
                    newRowData.put(column.getName(), parameter); // @todo type conversion ???
                }
            }

            // create the criterion used to uniquely identify the selected row in its table
            Criterion identityCriterion = createCriterion(identityColumns, rowData);

            // update the old row by setting only the updateable fields
            table.updateRow(newRowData, identityCriterion);
        }

        metaDataSession.setCurrentTableData(table.findAllRows());
    }

    private Criterion createCriterion(Column[] columns, RowData rowData)
    {
        Criterion criterion = null;

        if (columns!=null && columns.length>0)
        {
            criterion = Expression.equal(columns[0], rowData.get(columns[0].getName()));

            for (int i = 1; i < columns.length; i++)
            {
                criterion = Expression.and( criterion, Expression.equal(columns[i], rowData.get(columns[i].getName())) );
            }
        }
        return criterion;
    }

    public void insertRow(ActionMapping mapping, InsertRowForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        RowData rowData = new RowData();

        Map parameterMap = request.getParameterMap();
        for (Iterator iterator = parameterMap.entrySet().iterator(); iterator.hasNext();)
        {
            Map.Entry parameterPair = (Map.Entry) iterator.next();
            String parameterName = (String)parameterPair.getKey();
            Object[] parameterValues = (Object[])parameterPair.getValue();
            rowData.put(parameterName, parameterValues[0]);  // minimum array length is 1
        }

        Table table = getMetaDataSession(request).getCurrentTable();

        if (table==null)
        {
            throw new Exception("Unable to insert row in table: current table not specified");
        }
        else
        {
            table.insertRow(rowData);
            getMetaDataSession(request).setCurrentTableData(table.findAllRows());
        }
    }

    public void searchTableData(ActionMapping mapping, SearchTableDataForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        MetaDataSession metaDataSession = getMetaDataSession(request);
        Table table = metaDataSession.getCurrentTable();

        if (table==null)
        {
            throw new Exception("Unable to find rows in table: current table not specified");
        }

        Map parameterMap = request.getParameterMap();

        if (parameterMap.size() == 0)
        {
            metaDataSession.setCurrentTableData( table.findAllRows() );
        }
        else
        {
            Criterion criterion = null;

            Column[] columns = table.getColumns();
            for (int i = 0; i < columns.length; i++)
            {
                Column column = columns[i];
                String parameter = request.getParameter(column.getName());

/*
                // if column is a foreign key column then we need to find its display name and search on its value
                if (column.isForeignKeyColumn())
                {
                    ForeignKeyColumn foreignKeyColumn = (ForeignKeyColumn) column;
                    foreignKeyColumn.getImportedKeyColumn().getTable().get
                    ColumnConfiguration configuration = getDatabaseLoginSession(request).getConfigurator().getConfiguration(column);
                    if (configuration.get)
                }
*/
                // only included those parameters that contain an actual value
                if (StringUtils.isNotBlank(parameter))
                {
                    Criterion lastCriterion =
                            (form.getExactMatches())
                                ? Expression.equal(column, parameter)
                                : Expression.like(column, '%' + parameter + '%');
                    criterion =  (criterion == null) ? lastCriterion : Expression.and(criterion, lastCriterion);
                }
            }

            List currentTableData = (criterion == null) ? table.findAllRows() : table.findRows(criterion);
            metaDataSession.setCurrentTableData( currentTableData );
        }
    }

    public void deleteUnreferencedRows(ActionMapping mapping, DeleteUnreferencedRowsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {

    }
}
