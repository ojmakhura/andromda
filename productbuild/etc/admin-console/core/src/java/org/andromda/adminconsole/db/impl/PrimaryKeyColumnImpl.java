package org.andromda.adminconsole.db.impl;

import org.andromda.adminconsole.db.Column;
import org.andromda.adminconsole.db.ForeignKeyColumn;
import org.andromda.adminconsole.db.PrimaryKeyColumn;
import org.andromda.adminconsole.db.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrimaryKeyColumnImpl extends ColumnImpl implements PrimaryKeyColumn
{
    private String primaryKeyName = null;
    private final List exportedKeyColumns = new ArrayList();

    public PrimaryKeyColumnImpl(Table table, String name)
    {
        super(table, name);
        this.refresh(true);
    }

    public PrimaryKeyColumnImpl(Table table, String name, ForeignKeyColumn[] exportedKeyColumns)
    {
        super(table, name);
        this.exportedKeyColumns.addAll(Arrays.asList(exportedKeyColumns));
        this.refresh(false);
    }

    public String getPrimaryKeyName()
    {
        return primaryKeyName;
    }

    public ForeignKeyColumn[] getExportedKeyColumns()
    {
        return (ForeignKeyColumn[]) exportedKeyColumns.toArray(new ForeignKeyColumn[exportedKeyColumns.size()]);
    }

    private void refresh(boolean exportedKeys)
    {
        try
        {
            ResultSet resultSet = null;

            // PRIMARY KEY NAME
            try
            {
                resultSet = getMetaData().getPrimaryKeys(getCatalog(), getSchema(), getTable().getName());

                String name = getName();
                while (resultSet.next())
                {
                    String keyName = resultSet.getString("COLUMN_NAME");
                    if (name.equals(keyName))
                    {
                        primaryKeyName = resultSet.getString("PK_NAME");
                        break;
                    }
                }
            }
            finally
            { close(resultSet); }

            // EXPORTED KEY COLUMNS
            if (exportedKeys)
            {
                try
                {
                    exportedKeyColumns.clear();

                    Table table = getTable();
                    resultSet = getMetaData().getExportedKeys(getCatalog(), getSchema(), table.getName());

                    String name = getName();
                    while (resultSet.next())
                    {
                        String keyName = resultSet.getString("PKCOLUMN_NAME");
                        if (name.equals(keyName))
                        {
                            String targetTableName = resultSet.getString("FKTABLE_NAME");
                            String targetTableColumnName = resultSet.getString("PKCOLUMN_NAME");

                            Table targetTable = getTable().getDatabase().getPool().findTable(targetTableName);
                            if (targetTable == null)
                            {
                                targetTable = new TableImpl(getTable().getDatabase(), targetTableName);
                            }

                            Column targetColumn = getTable().getDatabase().getPool().findColumn(targetTableName, targetTableColumnName);
                            if (targetColumn == null)
                            {
                                targetColumn = new ForeignKeyColumnImpl(targetTable, targetTableColumnName, this);
                            }
                            exportedKeyColumns.add(targetColumn);
                        }
                    }
                }
                finally
                { close(resultSet); }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Unable to refresh table: " + getName());
        }
    }

    public void refresh()
    {
        super.refresh();
        this.refresh(true);
    }
}
