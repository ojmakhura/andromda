package org.andromda.utils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 */
public class DateUtilsHelperTest
{

    /**
     * @throws Exception
     */
    @Test
    public void testJavaToPerlFormat() throws Exception
    {
        final String[][] fixture = new String[][]{new String[]{"yyyy/MM/dd", "%Y/%m/%d"},
                                                  new String[]{"dddd/MM/yyyy HH:mm:ss", "%A/%m/%Y %H:%M:%S"},
                                                  new String[]{"yy-MMMM-ddd", "%y-%B-%a"}};

        for (String[] strings : fixture)
        {
            assertEquals(DateUtilsHelper.formatJavaToPerl(strings[0]), strings[1]);
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void testContainsTimeFormat() throws Exception
    {
        final Object[][] fixture = new Object[][]{new Object[]{"%Y/%m/%d", Boolean.FALSE},
                                                  new Object[]{"%A/%m/%Y %H:%M:%S", Boolean.TRUE},
                                                  new Object[]{"%y-%B-%a", Boolean.FALSE}};

        for (Object[] objects : fixture)
        {
            assertEquals(DateUtilsHelper.containsTimeFormat((String) objects[0]), objects[1]);
        }
    }
}
