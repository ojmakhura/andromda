package org.andromda.adminconsole.maintenance;

import org.andromda.adminconsole.config.AdminConsoleConfigurator;
import org.andromda.adminconsole.db.*;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Iterator;

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
        form.setTableData( table.findAllRows() );

        form.setNameValueList( getMetaDataSession(request).getTableNames().toArray() );
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

    public void loadConfigurator(ActionMapping mapping, LoadConfiguratorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        getDatabaseLoginSession(request).setConfigurator(new AdminConsoleConfigurator());
    }

    public void insertRow(ActionMapping mapping, InsertRowForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        RowData rowData = new RowData();

        Map parameterMap = request.getParameterMap();
        for (Iterator iterator = parameterMap.entrySet().iterator(); iterator.hasNext();)
        {
            Map.Entry parameterPair = (Map.Entry) iterator.next();
            Object parameterName = parameterPair.getKey();
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
        }
    }
}
