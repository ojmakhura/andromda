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

    public ForeignKeyColumnImpl(Table table, String name)
    {
        super(table, name);
        this.refresh(true);
    }

    public ForeignKeyColumnImpl(Table table, String name, PrimaryKeyColumn importedKeyColumn)
    {
        super(table, name);
        this.importedKeyColumn = importedKeyColumn;
        this.refresh(false);
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

    private void refresh(boolean importedKey)
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

                resultSet = getMetaData().getImportedKeys(getCatalog(), getSchema(), table.getName());
                while (resultSet.next() && foreignKeyName == null)
                {
                    if (name.equals(resultSet.getString("FKCOLUMN_NAME")))
                    {
                        foreignKeyName = resultSet.getString("FK_NAME");
                    }
                }
                if (foreignKeyName == null)
                {
                    throw new IllegalStateException("ForeignKey name could not be read, column not found: " + name);
                }
            }
            finally
            { close(resultSet); }

            // PRIMARY KEY NAME
            try
            {
                String name = getName();
                Table table = getTable();
                primaryKeyName = null;

                resultSet = getMetaData().getImportedKeys(getCatalog(), getSchema(), table.getName());
                while (resultSet.next() && primaryKeyName == null)
                {
                    if (name.equals(resultSet.getString("FKCOLUMN_NAME")))
                    {
                        primaryKeyName = resultSet.getString("PK_NAME");
                    }
                }
                if (primaryKeyName == null)
                {
                    throw new IllegalStateException("PrimaryKey name could not be read, column not found: " + name);
                }
            }
            finally
            { close(resultSet); }

            // DELETE RULE
            try
            {
                String name = getName();
                Table table = getTable();
                deleteRule = null;

                resultSet = getMetaData().getImportedKeys(getCatalog(), getSchema(), table.getName());
                while (resultSet.next() && deleteRule == null)
                {
                    if (name.equals(resultSet.getString("FKCOLUMN_NAME")))
                    {
                        deleteRule = ForeignKeyDeleteRule.get(resultSet.getInt("DELETE_RULE"));
                    }
                }
                if (deleteRule == null)
                {
                    throw new IllegalStateException("DeleteRule could not be read, column not found: " + name);
                }
            }
            finally
            { close(resultSet); }

            // UPDATE RULE
            try
            {
                String name = getName();
                Table table = getTable();
                updateRule = null;

                resultSet = getMetaData().getImportedKeys(getCatalog(), getSchema(), table.getName());
                while (resultSet.next() && updateRule == null)
                {
                    if (name.equals(resultSet.getString("FKCOLUMN_NAME")))
                    {
                        updateRule = ForeignKeyUpdateRule.get(resultSet.getInt("UPDATE_RULE"));
                    }
                }
                if (updateRule == null)
                {
                    throw new IllegalStateException("UpdateRule could not be read, column not found: " + name);
                }
            }
            finally
            { close(resultSet); }

            // IMPORTED KEY COLUMNS
            if (importedKey)
            {
                try
                {
                    String name = getName();
                    importedKeyColumn = null;

                    resultSet = getMetaData().getImportedKeys(getCatalog(), getSchema(), getTable().getName());
                    while (resultSet.next() && importedKeyColumn == null)
                    {
                        if (name.equals(resultSet.getString("FKCOLUMN_NAME")))
                        {
                            String targetTableName = resultSet.getString("PKTABLE_NAME");
                            String targetTableColumnName = resultSet.getString("PKCOLUMN_NAME");

                            Table targetTable = getTable().getDatabase().getPool().findTable(targetTableName);
                            if (targetTable == null)
                            {
                                targetTable = new TableImpl(getTable().getDatabase(), targetTableName);
                            }

                            Column targetColumn = getTable().getDatabase().getPool().findColumn(targetTableName, targetTableColumnName);
                            if (targetColumn == null)
                            {
                                targetColumn = new PrimaryKeyColumnImpl(targetTable, targetTableColumnName);
                            }
                            importedKeyColumn = (PrimaryKeyColumnImpl) targetColumn;
                        }
                    }
                    if (importedKeyColumn == null)
                    {
                        throw new IllegalStateException("ImportedColumn could not be read, column not found: " + name);
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
