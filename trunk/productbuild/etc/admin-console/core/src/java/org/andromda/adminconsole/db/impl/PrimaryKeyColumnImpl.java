package org.andromda.adminconsole.db.impl;

import org.andromda.adminconsole.db.ForeignKeyColumn;
import org.andromda.adminconsole.db.PrimaryKeyColumn;
import org.andromda.adminconsole.db.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrimaryKeyColumnImpl extends ColumnImpl implements PrimaryKeyColumn
{
    private String primaryKeyName = null;
    private final List exportedKeyColumns = new ArrayList();

    public PrimaryKeyColumnImpl(Table table, String name, int sqlType)
    {
        super(table, name, sqlType);
        this.loadMetaData();
    }

    public String getPrimaryKeyName()
    {
        return primaryKeyName;
    }

    public ForeignKeyColumn[] getExportedKeyColumns()
    {
        return (ForeignKeyColumn[]) exportedKeyColumns.toArray(new ForeignKeyColumn[exportedKeyColumns.size()]);
    }

    protected void addExportedKeyColumn(ForeignKeyColumn column)
    {
        exportedKeyColumns.add(column);
    }

    private void loadMetaData()
    {
        ResultSet primaryKeys = null;

        // PRIMARY KEY NAME
        try
        {
            // get the primary keys
            primaryKeys = getMetaData().getPrimaryKeys(getCatalog(), getSchema(), getTable().getName());

            String name = getName();
            while (primaryKeys.next())
            {
                // if the column names match, get the primary key name
                String keyName = primaryKeys.getString("COLUMN_NAME");
                if (name.equals(keyName))
                {
                    primaryKeyName = primaryKeys.getString("PK_NAME");
                    // once we find one we can break from this loop
                    break;
                }
            }
        }
        catch (SQLException e)
        {
            StringBuffer message = new StringBuffer();
            message.append("Database error while loading metadata for table ");
            message.append(getTable().getName());
            message.append(" and column ");
            message.append(getName());
            throw new RuntimeException(message.toString(),e);
        }
        finally
        {
            close(primaryKeys);
        }
    }
}
