package org.andromda.utils;

import junit.framework.TestCase;

/**
 *
 */
public class DateUtilsHelperTest
    extends TestCase
{
    /**
     * @param name
     */
    public DateUtilsHelperTest(String name)
    {
        super(name);
    }

    /**
     * @throws Exception
     */
    public void testJavaToPerlFormat() throws Exception
    {
        final String[][] fixture = new String[][]{new String[]{"yyyy/MM/dd", "%Y/%m/%d"},
                                                  new String[]{"dddd/MM/yyyy HH:mm:ss", "%A/%m/%Y %H:%M:%S"},
                                                  new String[]{"yy-MMMM-ddd", "%y-%B-%a"}};

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(DateUtilsHelper.formatJavaToPerl(strings[0]), strings[1]);
        }
    }

    /**
     * @throws Exception
     */
    public void testContainsTimeFormat() throws Exception
    {
        final Object[][] fixture = new Object[][]{new Object[]{"%Y/%m/%d", Boolean.FALSE},
                                                  new Object[]{"%A/%m/%Y %H:%M:%S", Boolean.TRUE},
                                                  new Object[]{"%y-%B-%a", Boolean.FALSE}};

        for (int i = 0; i < fixture.length; i++)
        {
            Object[] objects = fixture[i];
            assertEquals(Boolean.valueOf(DateUtilsHelper.containsTimeFormat((String)objects[0])), objects[1]);
        }
    }
}
