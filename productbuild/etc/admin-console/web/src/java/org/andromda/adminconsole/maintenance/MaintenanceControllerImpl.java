package org.andromda.adminconsole.maintenance;

import org.andromda.adminconsole.config.AdminConsoleConfigurator;
import org.andromda.adminconsole.db.*;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
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

        if (tables.length > 0)
        {
            metadataSession.setCurrentTable( tables[0] );
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
        response.addCookie(cookie);
    }
}
