package org.andromda.cartridges.database.metafacades;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.database.metafacades.Column.
 *
 * @see org.andromda.cartridges.database.metafacades.Column
 */
public class ColumnLogicImpl
       extends ColumnLogic
       implements org.andromda.cartridges.database.metafacades.Column
{
    private static DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    // ---------------- constructor -------------------------------

    public ColumnLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Column#getDummyValue(int)
     */
    public java.lang.String handleGetDummyValue(int index) 
    {
        String dummyValue = null;

        final String type = getType().getFullyQualifiedName(true);

        if ("datatype.String".equals(type))
        {
            dummyValue = '\'' + getName() + '-' + index + '\'';
        }
        else if ("datatype.boolean".equals(type))
        {
            dummyValue = String.valueOf(index % 2 == 0);
        }
        else if ("datatype.int".equals(type) || "datatype.Integer".equals(type) || "datatype.short".equalsIgnoreCase(type) || "datatype.long".equalsIgnoreCase(type))
        {
            dummyValue = String.valueOf(index);
        }
        else if ("datatype.float".equalsIgnoreCase(type) || "datatype.double".equalsIgnoreCase(type))
        {
            dummyValue = String.valueOf(index) + ".555";
        }
        else if ("datatype.char".equals(type) || "datatype.Character".equals(type))
        {
            dummyValue = '\'' + Character.toString((char)index) + '\'';
        }
        else if ("datatype.Date".equals(type) || "datatype.DateTime".equals(type))
        {
            dummyValue = '\'' + dateFormatter.format(new Date()) + '\'';
        }
        else
        {
            dummyValue = "error";
        }

        final String maxLengthString = getColumnLength();
        int maxLength = 0;
        int dummyValueLength = dummyValue.length();

        try
        {
            maxLength = Integer.parseInt(maxLengthString);
        }
        catch (Exception e)
        {
            maxLength = 0;
        }

        if (maxLength >0 && dummyValueLength > maxLength)
        {
            dummyValue = dummyValue.substring(0, maxLength);
        }

        return dummyValue;
    }

    /**
     * @see org.andromda.cartridges.database.metafacades.Column#getTable()
     */
    protected java.lang.Object handleGetTable()
    {
        return getOwner();
    }
}
