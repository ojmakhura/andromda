package org.andromda.adminconsole.db;

import java.sql.DatabaseMetaData;
import java.io.Serializable;

public final class ForeignKeyUpdateRule implements Serializable
{
    public final static ForeignKeyUpdateRule NOACTION = new ForeignKeyUpdateRule(0);
    public final static ForeignKeyUpdateRule CASCADE = new ForeignKeyUpdateRule(1);
    public final static ForeignKeyUpdateRule SETNULL = new ForeignKeyUpdateRule(2);
    public final static ForeignKeyUpdateRule SETDEFAULT = new ForeignKeyUpdateRule(3);

    private int value = 0;

    private ForeignKeyUpdateRule(int value)
    {
        this.value = value;
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof ForeignKeyUpdateRule)
        {
            return ((ForeignKeyUpdateRule) obj).value == this.value;
        }
        return false;
    }

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
                return new ForeignKeyUpdateRule(value);
        }
    }

    public int getValue()
    {
        return value;
    }
}
