package org.andromda.adminconsole.db;

import java.sql.SQLException;
import java.util.List;
import java.io.Serializable;

/**
 * Represents a table in a database
 */
public interface Table extends Serializable
{

    /**
     * @return this table's name
     */
    public String getName();

    /**
     * @return this table's type (such as view or table)
     */
    public TableType getType();

    /**
     * @return the number of columns this table is constructed of
     */
    public int getColumnCount();

    /**
     * @return the column with the argmument name, may be <code>null</code> if the column cannot be found
     */
    public Column getColumn(String name);

    /**
     * @return this table's columns
     */
    public Column[] getColumns();

    /**
     * @return the names of all columns of which this table is constructed
     */
    public String[] getColumnNames();

    /**
     * @return the number of primary key columns
     */
    public int getPrimaryKeyColumnCount();

    /**
     * @return the primary key columns
     */
    public PrimaryKeyColumn[] getPrimaryKeyColumns();

    /**
     * @return the names of the primary key columns
     */
    public String[] getPrimaryKeyColumnNames();

    /**
     * @return the number of foreign key columns
     */
    public int getForeignKeyColumnCount();

    /**
     * @return the foreign key columns
     */
    public ForeignKeyColumn[] getForeignKeyColumns();

    /**
     * @return the names of the foreign key columns
     */
    public String[] getForeignKeyColumnNames();

    /**
     * Inserts a new row into this database
     *
     * @return the number of rows inserted
     * @throws SQLException when there was a problem inserting the values into this table
     */
    public int insertRow(RowData rowData) throws SQLException;

    /**
     * @return a list of all rows in this table, returns RowData instances
     */
    public List findAllRows() throws SQLException;

    /**
     * @param maximum the maximum number of rows to return, zero denotes no upper limit
     * @return a list of all rows in this table, returns RowData instances, the list is restricted
     *  in size
     */
    public List findAllRows(int maximum) throws SQLException;

    /**
     * Finds rows based on the given criterion.
     *
     * @return the rows found based on the argument criterion
     */
    public List findRows(Criterion criterion) throws SQLException;

    /**
     * Finds rows based on the given criterion.
     *
     * @param maximum the maximum number of rows to return, zero denotes no upper limit
     * @return the rows found based on the argument criterion, the list is restricted
     *  in size
     */
    public List findRows(Criterion criterion, int maximum) throws SQLException;

    /**
     * Updates the rows with the given row data, only those rows that match the criterion are updated.
     *
     * @return the number of rows actually updated
     */
    public int updateRow(RowData rowData, Criterion criterion) throws SQLException;

    /**
     * Deletes rows based on the given criterion.
     *
     * @return the number of rows actually deleted
     */
    public int deleteRow(Criterion criterion) throws SQLException;

    /**
     * @return the database in which this table is defined
     */
    public Database getDatabase();

    /**
     * @return the names of the tables that have foreign key columns importing this table
     */
    public String[] getImportingTableNames();

    /**
     * @return the number of the tables that have foreign key columns importing this table
     */
    public int getImportingTablesCount();
}
