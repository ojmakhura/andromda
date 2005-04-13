package org.andromda.cartridges.bpm4struts;

import junit.framework.TestCase;

public class Bpm4StrutsDateUtilsTest
        extends TestCase
{
    public Bpm4StrutsDateUtilsTest(String name)
    {
        super(name);
    }

    public void testJavaToPerlFormat() throws Exception
    {
        final String[][] fixture = new String[][]{new String[]{"yyyy/MM/dd", "%Y/%m/%d"},
                                                  new String[]{"dddd/MM/yyyy HH:mm:ss", "%A/%m/%Y %H:%M:%S"},
                                                  new String[]{"yy-MMMM-ddd", "%y-%B-%a"}};

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
//            System.out.println(strings[0]+" "+strings[1]+" --> "+Bpm4StrutsDateUtils.formatJavaToPerl(strings[0]));
            assertEquals(Bpm4StrutsDateUtils.formatJavaToPerl(strings[0]), strings[1]);
        }
    }

    public void testContainsTimeFormat() throws Exception
    {
        final Object[][] fixture = new Object[][]{new Object[]{"%Y/%m/%d", Boolean.FALSE},
                                                  new Object[]{"%A/%m/%Y %H:%M:%S", Boolean.TRUE},
                                                  new Object[]{"%y-%B-%a", Boolean.FALSE}};

        for (int i = 0; i < fixture.length; i++)
        {
            Object[] objects = fixture[i];
//            System.out.println(objects[0]+" "+objects[1]+" --> "+Bpm4StrutsDateUtils.containsTimeFormat((String)objects[0]));
            assertEquals(Boolean.valueOf(Bpm4StrutsDateUtils.containsTimeFormat((String)objects[0])), objects[1]);
        }
    }
}
