package org.andromda.adminconsole.db;

import java.sql.DatabaseMetaData;
import java.io.Serializable;

/**
 * A foreign key delete rule.
 */
public final class ForeignKeyDeleteRule implements Serializable
{
    /**
     * @see java.sql.DatabaseMetaData.importedKeyNoAction
     */
    public final static ForeignKeyDeleteRule NOACTION =
            new ForeignKeyDeleteRule(DatabaseMetaData.importedKeyNoAction);

    /**
     * @see java.sql.DatabaseMetaData.importedKeyCascade
     */
    public final static ForeignKeyDeleteRule CASCADE =
            new ForeignKeyDeleteRule(DatabaseMetaData.importedKeyCascade);

    /**
     * @see java.sql.DatabaseMetaData.importedKeySetNull
     */
    public final static ForeignKeyDeleteRule SETNULL =
            new ForeignKeyDeleteRule(DatabaseMetaData.importedKeySetNull);

    /**
     * @see java.sql.DatabaseMetaData.importedKeySetDefault
     */
    public final static ForeignKeyDeleteRule SETDEFAULT =
            new ForeignKeyDeleteRule(DatabaseMetaData.importedKeySetDefault);

    /**
     * This enumeration literal's internal value
     */
    private final int value;

    /**
     * Privately construct a new enumeration literal.
     *
     * @param value the internal value for this enumeration literal
     */
    private ForeignKeyDeleteRule(int value)
    {
        this.value = value;
    }

    /**
     * @return true if the argument object is also a ForeignKeyDeleteRule instance, and its
     *  value equals the value of this instance
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof ForeignKeyDeleteRule)
        {
            return ((ForeignKeyDeleteRule) obj).value == this.value;
        }
        return false;
    }

    /**
     * Given its value, returns the enumeration literal.
     */
    public static ForeignKeyDeleteRule get(int value)
    {
        switch (value)
        {
            case DatabaseMetaData.importedKeyNoAction:
                return NOACTION;
            case DatabaseMetaData.importedKeyCascade:
                return CASCADE;
            case DatabaseMetaData.importedKeySetNull:
                return SETNULL;
            case DatabaseMetaData.importedKeySetDefault:
                return SETDEFAULT;
            default:
                throw new RuntimeException("ForeignKeyDeleteRule.get: Argument enumeration literal value unknown");
        }
    }

    /**
     * @return this enumeration literal's internal value
     */
    public int getValue()
    {
        return value;
    }
}
