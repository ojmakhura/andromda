package org.andromda.utils.beautifier.core;

/**
 * Copyright 2008 hybrid labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for JavaImportBeautifier
 */
public class JavaImportBeautifierTest
{

    private JavaImportBeautifierImpl beautifier;

    @Before
    public void setUp() throws Exception
    {
        beautifier = new JavaImportBeautifierImpl();
        beautifier.setFormat(false);
        beautifier.setOrganizeImports(true);
        beautifier.setStrict(true);
    }

    @Test
    public void testSample1() throws Exception
    {
        testIndexedSample("01", false, false);
    }

    @Test
    public void testSample1Formatted() throws Exception
    {
        beautifier.setFormat(true);

        final String input = loadTestFile("Sample01.java_input");
        String output = beautifier.beautify(input);

        final String expectedOutput = loadTestFile("Sample01.java_output_formatted");
        compare(expectedOutput, output);
    }

    @Test
    public void testSample2() throws Exception
    {
        testIndexedSample("02", false, false);
    }

    @Test
    public void testSample3() throws Exception
    {
        testIndexedSample("03", false, false);
    }

    @Test
    public void testSample03Formatted() throws Exception
    {
        beautifier.setFormat(true);

        final String input = loadTestFile("Sample03.java_input");
        String output = beautifier.beautify(input);

        final String expectedOutput = loadTestFile("Sample03.java_output_formatted");
        compare(expectedOutput, output);
    }

    @Test
    public void testSample4() throws Exception
    {
        testIndexedSample("04", false);
    }

    @Test
    public void testSample5() throws Exception
    {
        testIndexedSample("05", false);
    }

    @Test
    public void testSample6() throws Exception
    {
        testIndexedSample("06", false);
    }

    @Test
    public void testSample7() throws Exception
    {
        testIndexedSample("07", true);
    }

    @Test
    public void testSample8() throws Exception
    {
        testIndexedSample("08", true);
    }

    @Test
    public void testSample9() throws Exception
    {
        testIndexedSample("09", true);
    }

    @Test
    public void testSample10() throws Exception
    {
        testIndexedSample("10", true);
    }

    @Test
    public void testSample11() throws Exception
    {
        testIndexedSample("11", true);
    }

    @Test
    public void testSample12() throws Exception
    {
        testIndexedSample("12", true);
    }

    @Test
    public void testSample13() throws Exception
    {
        testIndexedSample("13", true);
    }

    @Test
    public void testSample14() throws Exception
    {
        testIndexedSample("14", false);
    }

    @Test
    public void testSample15() throws Exception
    {
        testIndexedSample("15", false, false);
    }

    @Test
    public void testSample16() throws Exception
    {
        testIndexedSample("16", false, false);
    }

    @Test
    public void testSample17() throws Exception
    {
        beautifier.setStrict(false);
        testIndexedSample("17", false, false);
    }

    @Test
    public void testSample18() throws Exception
    {
        beautifier.setStrict(false);
        testIndexedSample("18", false, false);
    }

    @Test
    public void testSample20() throws Exception
    {
        testIndexedSample("20", false);
    }

    @Test
    public void testSample21() throws Exception
    {
        testIndexedSample("21", false);
    }

    @Test
    public void testSample22() throws Exception
    {
        testIndexedSample("22", false);
    }

    @Test
    public void testSample23() throws Exception
    {
        testIndexedSample("23", false);
    }

    @Test
    public void testSample24() throws Exception
    {
        testIndexedSample("24", false);
    }

    @Test
    public void testSample25() throws Exception
    {
        testIndexedSample("25");
    }

    @Test
    public void testSample26() throws Exception
    {
        testIndexedSample("26");
    }

    @Test
    public void testSample27() throws Exception
    {
        testIndexedSample("27");
    }

    @Test
    public void testSample28() throws Exception
    {
        testIndexedSample("28");
    }

    @Test
    public void testSample29() throws Exception
    {
        testIndexedSample("29");
    }

    @Test
    public void testSample30() throws Exception
    {
        testIndexedSample("30");
    }

    @Test
    public void testSample31() throws Exception
    {
        testIndexedSample("31");
    }

    @Test
    public void testSample32() throws Exception
    {
        testIndexedSample("32");
    }

    @Test
    public void testSample33() throws Exception
    {
        testIndexedSample("33");
    }

    @Test
    public void testSample34() throws Exception
    {
        testIndexedSample("34");
    }

    @Test
    public void testSample35() throws Exception
    {
        testIndexedSample("35");
    }

/*  //TODO FIX Test
    @Test
    public void testSample36() throws Exception
    {
        testIndexedSample("36");
    }
*/
    @Test
    public void testSample37() throws Exception
    {
        testIndexedSample("37");
    }

    private void testIndexedSample(String sampleIndex) throws Exception
    {
        testIndexedSample(sampleIndex, true, true);
    }

    private void testIndexedSample(String sampleIndex, boolean useConventionFile) throws Exception
    {
        testIndexedSample(sampleIndex, useConventionFile, true);
    }

    private void testIndexedSample(String sampleIndex, boolean useConventionFile, boolean format) throws Exception
    {
        beautifier.setFormat(format);

        if (useConventionFile)
        {
            beautifier.setConventionFilePath("test-convention.xml");
        }

        final String input = loadTestFile("Sample" + sampleIndex + ".java_input");
        String output = beautifier.beautify(input);
        final String expectedOutput = loadTestFile("Sample" + sampleIndex + ".java_output");

        compare(expectedOutput, output);
    }

    private String loadTestFile(String name) throws Exception
    {
        return IOUtils.toString(this.getClass().getResourceAsStream(name));
    }

    private void compare(String expected, String actual)
    {
        assertEquals(normalized(expected), normalized(actual));
    }

    private String normalized(String seq)
    {
        return seq.trim().replaceAll("\r\n", System.getProperty("line.separator"));
    }

}
