package org.andromda.adminconsole.db;

import java.sql.DatabaseMetaData;
import java.io.Serializable;

/**
 * A foreign key delete rule.
 */
public final class ForeignKeyUpdateRule implements Serializable
{
    /**
     * @see java.sql.DatabaseMetaData.importedKeyNoAction
     */
    public final static ForeignKeyUpdateRule NOACTION =
            new ForeignKeyUpdateRule(DatabaseMetaData.importedKeyNoAction);

    /**
     * @see java.sql.DatabaseMetaData.importedKeyCascade
     */
    public final static ForeignKeyUpdateRule CASCADE =
            new ForeignKeyUpdateRule(DatabaseMetaData.importedKeyCascade);

    /**
     * @see java.sql.DatabaseMetaData.importedKeySetNull
     */
    public final static ForeignKeyUpdateRule
            SETNULL = new ForeignKeyUpdateRule(DatabaseMetaData.importedKeySetNull);

    /**
     * @see java.sql.DatabaseMetaData.importedKeySetDefault
     */
    public final static ForeignKeyUpdateRule
            SETDEFAULT = new ForeignKeyUpdateRule(DatabaseMetaData.importedKeySetDefault);

    /**
     * This enumeration literal's internal value
     */
    private int value = 0;

    /**
     * Privately construct a new enumeration literal.
     *
     * @param value the internal value for this enumeration literal
     */
    private ForeignKeyUpdateRule(int value)
    {
        this.value = value;
    }

    /**
     * @return true if the argument object is also a ForeignKeyUpdateRule instance, and its
     *  value equals the value of this instance
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof ForeignKeyUpdateRule)
        {
            return ((ForeignKeyUpdateRule) obj).value == this.value;
        }
        return false;
    }

    /**
     * Given its value, returns the enumeration literal.
     */
    public static ForeignKeyUpdateRule get(int value)
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
                throw new RuntimeException("ForeignKeyUpdateRule.get: Argument enumeration literal value unknown");
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
