package org.andromda.cartridges.bpm4struts;

import junit.framework.TestCase;

import java.util.Arrays;

public class Bpm4StrutsUtilsTest
        extends TestCase
{
    public Bpm4StrutsUtilsTest(String name)
    {
        super(name);
    }


    public void testParseValidatorArgs() throws Exception
    {
        final Object[][] fixture = new Object[][]{new Object[]{"myValidator", new Object[0]}, new Object[]{
            "myValidator(myVar=myArg)", new Object[]{"myArg"}}, new Object[]{
                "myValidator(myVar=myArg,myOtherVar=myOtherArg)", new Object[]{"myArg", "myOtherArg"}}};

        for (int i = 0; i < fixture.length; i++)
        {
            Object[] objects = fixture[i];
            assertTrue(Arrays.equals(Bpm4StrutsUtils.parseValidatorArgs((String)objects[0]).toArray(),
                    (Object[])objects[1]));
        }
    }

    public void testParseValidatorVars() throws Exception
    {
        final Object[][] fixture = new Object[][]{new Object[]{"myValidator", new Object[0]}, new Object[]{
            "myValidator(myVar=myArg)", new Object[]{"myVar"}}, new Object[]{
                "myValidator(myVar=myArg,myOtherVar=myOtherArg)", new Object[]{"myVar", "myOtherVar"}}};

        for (int i = 0; i < fixture.length; i++)
        {
            Object[] objects = fixture[i];
            assertTrue(Arrays.equals(Bpm4StrutsUtils.parseValidatorVars((String)objects[0]).toArray(),
                    (Object[])objects[1]));
        }
    }


    public void testParseValidatorName() throws Exception
    {
        final String[][] fixture = new String[][]{new String[]{"myValidator", "myValidator"}, new String[]{
            "myValidator(myVar=myArg)", "myValidator"}, new String[]{"myValidator(myVar=myArg,myOtherVar=myOtherArg)",
                                                                     "myValidator"}};

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(Bpm4StrutsUtils.parseValidatorName(strings[0]), strings[1]);
        }
    }

    public void testToWebFileName() throws Exception
    {
        final String[][] fixture = new String[][]{
            new String[]{"singleword", "singleword"},
            new String[]{"two words", "two-words"},
            new String[]{" stuff   with whitespace  ", "stuff-with-whitespace"}};

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(Bpm4StrutsUtils.toWebFileName(strings[0]), strings[1]);
        }
    }
}
