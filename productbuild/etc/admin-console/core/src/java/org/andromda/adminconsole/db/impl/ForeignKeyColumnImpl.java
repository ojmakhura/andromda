package org.andromda.adminconsole.db.impl;

import org.andromda.adminconsole.db.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ForeignKeyColumnImpl extends ColumnImpl implements ForeignKeyColumn
{
    private String foreignKeyName = null;
    private String primaryKeyName = null;
    private ForeignKeyDeleteRule deleteRule = null;
    private ForeignKeyUpdateRule updateRule = null;
    private PrimaryKeyColumn importedKeyColumn = null;

    public ForeignKeyColumnImpl(Table table, String name, int sqlType)
    {
        super(table, name, sqlType);
        this.loadMetaData();
    }

    public String getForeignKeyName()
    {
        return foreignKeyName;
    }

    public String getPrimaryKeyName()
    {
        return primaryKeyName;
    }

    public ForeignKeyDeleteRule getDeleteRule()
    {
        return deleteRule;
    }

    public ForeignKeyUpdateRule getUpdateRule()
    {
        return updateRule;
    }

    public PrimaryKeyColumn getImportedKeyColumn()
    {
        return importedKeyColumn;
    }

    private void loadMetaData()
    {
        try
        {
            ResultSet resultSet = null;

            // FOREIGN KEY NAME
            try
            {
                String name = getName();
                Table table = getTable();
                foreignKeyName = null;
                primaryKeyName = null;
                deleteRule = null;
                updateRule = null;
                importedKeyColumn = null;

                resultSet = getMetaData().getImportedKeys(getCatalog(), getSchema(), table.getName());
                while (resultSet.next())
                {
                    if (name.equals(resultSet.getString("FKCOLUMN_NAME")))
                    {
                        foreignKeyName = resultSet.getString("FK_NAME");
                        primaryKeyName = resultSet.getString("PK_NAME");
                        deleteRule = ForeignKeyDeleteRule.get(resultSet.getInt("DELETE_RULE"));
                        updateRule = ForeignKeyUpdateRule.get(resultSet.getInt("UPDATE_RULE"));

                        String pkTableName = resultSet.getString("PKTABLE_NAME");
                        String pkColumnName = resultSet.getString("PKCOLUMN_NAME");

                        Table pkTable = getTable().getDatabase().getPool().findTable(pkTableName);
                        if (pkTable == null)
                        {
                            pkTable = new TableImpl(getTable().getDatabase(), pkTableName);
                        }

                        Column targetColumn = getTable().getDatabase().getPool().findColumn(pkTableName, pkColumnName);
                        if (targetColumn == null)
                        {
                            ResultSet columnSet = getMetaData().getColumns(
                                    getCatalog(), getSchema(), pkTableName, pkColumnName);
                            if (columnSet.next())
                            {
                                int sqlType = columnSet.getInt("DATA_TYPE");
                                targetColumn = new PrimaryKeyColumnImpl(pkTable, pkColumnName, sqlType);
                            }
                            else
                            {
                                throw new IllegalStateException("An exported key column was found but the " +
                                        "column could not be loaded: "+pkColumnName);
                            }
                        }
                        importedKeyColumn = (PrimaryKeyColumnImpl) targetColumn;

                        break;
                    }
                }
                if (foreignKeyName == null)
                    throw new IllegalStateException("ForeignKey name could not be read, column not found: " + name);
                if (primaryKeyName == null)
                    throw new IllegalStateException("PrimaryKey name could not be read, column not found: " + name);
                if (deleteRule == null)
                    throw new IllegalStateException("DeleteRule could not be read, column not found: " + name);
                if (updateRule == null)
                    throw new IllegalStateException("UpdateRule could not be read, column not found: " + name);
                if (importedKeyColumn == null)
                    throw new IllegalStateException("ImportedColumn could not be read, column not found: " + name);
            }
            finally
            {
                close(resultSet);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Unable to refresh table: " + getName());
        }
    }

}
