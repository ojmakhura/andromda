package org.andromda.core.common;

import junit.framework.TestCase;

public class DateUtilsHelperTest extends TestCase
{
    public DateUtilsHelperTest(String name)
    {
        super(name);
    }

    public void testJavaToPerlFormat() throws Exception
    {
        final String[][] fixture = new String[][]
        {
            new String[] { "yyyy/MM/dd", "%Y/%m/%d" },
            new String[] { "yy-MMMM-ddd", "%y-%B-%d" }
        };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            System.out.println(strings[0] + " " + strings[1] + " " + DateUtilsHelper.formatJavaToPerl(strings[0]));
            assertEquals(DateUtilsHelper.formatJavaToPerl(strings[0]), strings[1]);
        }
    }
}
