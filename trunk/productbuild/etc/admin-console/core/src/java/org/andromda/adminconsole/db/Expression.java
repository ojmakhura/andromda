package org.andromda.adminconsole.db;

import org.apache.commons.lang.StringEscapeUtils;

public class Expression
{
    public static Criterion and(Criterion left, Criterion right)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(left);
        buffer.append(" AND ");
        buffer.append(right);
        return new CriterionImpl(buffer.toString());
    }

    public static Criterion or(Criterion left, Criterion right)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(left);
        buffer.append(" OR ");
        buffer.append(right);
        return new CriterionImpl(buffer.toString());
    }

    public static Criterion like(Column column, Object value)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(column.getName());
        buffer.append(" LIKE ");
        buffer.append('\'');
        buffer.append(value);
        buffer.append('\'');

        return new CriterionImpl(StringEscapeUtils.escapeSql(buffer.toString()));
    }

    public static Criterion isNull(Column column)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(column.getName());
        buffer.append(" IS NULL");

        return new CriterionImpl(StringEscapeUtils.escapeSql(buffer.toString()));
    }

    public static Criterion notNull(Column column)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(column.getName());
        buffer.append(" IS NOT NULL");

        return new CriterionImpl(StringEscapeUtils.escapeSql(buffer.toString()));
    }

    public static Criterion equal(Column column, Object value)
    {
        return doLogical(column, value, "=");
    }

    public static Criterion notEqual(Column column, Object value)
    {
        return doLogical(column, value, "<>");
    }

    public static Criterion greaterThan(Column column, Object value)
    {
        return doLogical(column, value, ">");
    }

    public static Criterion greaterThanEqual(Column column, Object value)
    {
        return doLogical(column, value, ">=");
    }

    public static Criterion lessThan(Column column, Object value)
    {
        return doLogical(column, value, "<");
    }

    public static Criterion lessThanEqual(Column column, Object value)
    {
        return doLogical(column, value, "<=");
    }

    private static Criterion doLogical(Column column, Object value, String comparator)
    {
        boolean string = String.class.equals(column.getType());

        StringBuffer buffer = new StringBuffer();
        buffer.append(column.getName());
        buffer.append(comparator);
        if (string) buffer.append('\'');
        buffer.append(value);
        if (string) buffer.append('\'');

        return new CriterionImpl(StringEscapeUtils.escapeSql(buffer.toString()));
    }

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
    }
}
