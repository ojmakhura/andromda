package org.andromda.adminconsole.db;

import java.sql.SQLException;
import java.util.List;
import java.io.Serializable;

public interface Table extends Serializable
{
    public String getName();

    public TableType getType();

    public int getColumnCount();

    public Column getColumn(String name);

    public Column[] getColumns();

    public String[] getColumnNames();

    public int getPrimaryKeyColumnCount();

    public PrimaryKeyColumn[] getPrimaryKeyColumns();

    public String[] getPrimaryKeyColumnNames();

    public int getForeignKeyColumnCount();

    public ForeignKeyColumn[] getForeignKeyColumns();

    public String[] getForeignKeyColumnNames();

    public int insertRow(RowData rowData) throws SQLException;

    public List findAllRows() throws SQLException;

    public List findRows(Criterion criterion) throws SQLException;

    public int updateRow(RowData rowData, Criterion criterion) throws SQLException;

    public int deleteRow(Criterion criterion) throws SQLException;

    public Database getDatabase();

    public Table[] getImportingTables();

    public int getImportingTablesCount();
}
