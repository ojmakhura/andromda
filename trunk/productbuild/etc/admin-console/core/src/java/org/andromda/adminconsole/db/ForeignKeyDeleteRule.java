package org.andromda.adminconsole.db;

import java.sql.DatabaseMetaData;

public final class ForeignKeyDeleteRule
{
    public final static ForeignKeyDeleteRule NOACTION = new ForeignKeyDeleteRule(DatabaseMetaData.importedKeyNoAction);
    public final static ForeignKeyDeleteRule CASCADE = new ForeignKeyDeleteRule(DatabaseMetaData.importedKeyCascade);
    public final static ForeignKeyDeleteRule SETNULL = new ForeignKeyDeleteRule(DatabaseMetaData.importedKeySetNull);
    public final static ForeignKeyDeleteRule SETDEFAULT = new ForeignKeyDeleteRule(DatabaseMetaData.importedKeySetDefault);

    private int value = 0;

    private ForeignKeyDeleteRule(int value)
    {
        this.value = value;
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof ForeignKeyDeleteRule)
        {
            return ((ForeignKeyDeleteRule) obj).value == this.value;
        }
        return false;
    }

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
                return new ForeignKeyDeleteRule(value);
        }
    }

    public int getValue()
    {
        return value;
    }
}
