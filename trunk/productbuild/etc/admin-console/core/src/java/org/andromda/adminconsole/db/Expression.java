package org.andromda.adminconsole.db;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Creates criterions based on different types of expression such as logical comparisons.
 */
public class Expression
{
    /**
     * Builds a criterion in which A and B must both evaluate true.
     */
    public static Criterion and(Criterion left, Criterion right)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(left);
        buffer.append(" AND ");
        buffer.append(right);

        return new CriterionImpl(buffer.toString());
    }

    /**
     * Builds a criterion in which A or B must evaluate true.
     */
    public static Criterion or(Criterion left, Criterion right)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(left);
        buffer.append(" OR ");
        buffer.append(right);

        return new CriterionImpl(buffer.toString());
    }

    /**
     * Builds a criterion in which the column must hold a value similar to the argument value.
     */
    public static Criterion like(Column column, Object value)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(column.getName());
        buffer.append(" LIKE ");
        buffer.append('\'');
        buffer.append(StringEscapeUtils.escapeSql(String.valueOf(value)));
        buffer.append('\'');

        return new CriterionImpl(buffer.toString());
    }

    /**
     * Builds a criterion in which the column value must be absent.
     */
    public static Criterion isNull(Column column)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(column.getName());
        buffer.append(" IS NULL");

        return new CriterionImpl(buffer.toString());
    }

    /**
     * Builds a criterion in which the column value must not be absent.
     */
    public static Criterion notNull(Column column)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(column.getName());
        buffer.append(" IS NOT NULL");

        return new CriterionImpl(buffer.toString());
    }

    /**
     * Builds a criterion in which the column must hold the specified value.
     */
    public static Criterion equal(Column column, Object value)
    {
        return doLogical(column, value, "=");
    }

    /**
     * Builds a criterion in which the column must not hold the specified value.
     */
    public static Criterion notEqual(Column column, Object value)
    {
        return doLogical(column, value, "<>");
    }

    /**
     * Builds a criterion in which the column must hold a value greater than the one specified.
     */
    public static Criterion greaterThan(Column column, Object value)
    {
        return doLogical(column, value, ">");
    }

    /**
     * Builds a criterion in which the column must hold a value greater than or equal to the one specified.
     */
    public static Criterion greaterThanEqual(Column column, Object value)
    {
        return doLogical(column, value, ">=");
    }

    /**
     * Builds a criterion in which the column must hold a value less than the one specified.
     */
    public static Criterion lessThan(Column column, Object value)
    {
        return doLogical(column, value, "<");
    }

    /**
     * Builds a criterion in which the column must hold a value less than or equal to the one specified.
     */
    public static Criterion lessThanEqual(Column column, Object value)
    {
        return doLogical(column, value, "<=");
    }

    /**
     * Performs a logical operation on the specified column and value, the logical comparator/operator
     * is specified too.
     */
    private static Criterion doLogical(Column column, Object value, String comparator)
    {
        boolean string = String.class.equals(column.getType()) || Character.class.equals(column.getType());

        StringBuffer buffer = new StringBuffer();
        buffer.append(column.getName());
        buffer.append(comparator);
        if (string) buffer.append('\'');
        buffer.append(StringEscapeUtils.escapeSql(String.valueOf(value)));
        if (string) buffer.append('\'');

        return new CriterionImpl(buffer.toString());
    }

    /**
     * A simple criterion implementation.
     */
    public static class CriterionImpl implements Criterion
    {
        private String sqlString = null;

        public CriterionImpl(String sqlString)
        {
            this.sqlString = sqlString;
        }

        public String toSqlString()
        {
            return sqlString;
        }

        public String toString()
        {
            return toSqlString();
        }
    }
}
