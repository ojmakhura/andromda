package org.andromda.beautifier.core;

import static org.junit.Assert.assertEquals;
import de.plushnikov.doctorjim.ImportProcessor;
import de.plushnikov.doctorjim.javaparser.ParseException;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for JavaImportBeautifier. Duplicates the tests found on 
 * http://doctor-jim.googlecode.com/svn/trunk
 */
public class JavaImportBeautifierTest
{
    private ImportProcessor mProcessor;

    /**
     * 
     */
    @Before
    public void setUp()
    {
        mProcessor = new ImportProcessor();
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testOne() throws IOException, ParseException
    {
        testbeautification("Temp");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testTwo() throws IOException, ParseException
    {
        testbeautification("Temp2");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testPackageInfo() throws IOException, ParseException
    {
        testbeautification("package-info");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testIso() throws IOException, ParseException
    {
        testbeautification("Iso");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testDocumentation_Sample1() throws IOException, ParseException
    {
        testbeautification("Documentation_Sample1");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testFiveParametersLogger() throws IOException, ParseException
    {
        testbeautification("FiveParametersLogger");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testIFExtendsTwo() throws IOException, ParseException
    {
        testbeautification("IFExtendsTwo");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample1() throws IOException, ParseException
    {
        testbeautification("Sample01");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample2() throws IOException, ParseException
    {
        testbeautification("Sample02");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample3() throws IOException, ParseException
    {
        testbeautification("Sample03");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample4() throws IOException, ParseException
    {
        testbeautification("Sample04");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample5() throws IOException, ParseException
    {
        testbeautification("Sample05");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample6() throws IOException, ParseException
    {
        testbeautification("Sample06");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample7() throws IOException, ParseException
    {
        testbeautification("Sample07");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample8() throws IOException, ParseException
    {
        testbeautification("Sample08");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample9() throws IOException, ParseException
    {
        testbeautification("Sample09");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample10() throws IOException, ParseException
    {
        testbeautification("Sample10");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample11() throws IOException, ParseException
    {
        testbeautification("Sample11");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample12() throws IOException, ParseException
    {
        testbeautification("Sample12");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample13() throws IOException, ParseException
    {
        testbeautification("Sample13");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample14() throws IOException, ParseException
    {
        testbeautification("Sample14");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample15() throws IOException, ParseException
    {
        testbeautification("Sample15");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample16() throws IOException, ParseException
    {
        testbeautification("Sample16");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample17() throws IOException, ParseException
    {
        mProcessor.setStrict(false);
        testbeautification("Sample17");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample18() throws IOException, ParseException
    {
        mProcessor.setStrict(false);
        testbeautification("Sample18");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample20() throws IOException, ParseException
    {
        testbeautification("Sample20");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample21() throws IOException, ParseException
    {
        testbeautification("Sample21");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample22() throws IOException, ParseException
    {
        testbeautification("Sample22");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample23() throws IOException, ParseException
    {
        testbeautification("Sample23");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample24() throws IOException, ParseException
    {
        testbeautification("Sample24");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample25() throws IOException, ParseException
    {
        testbeautification("Sample25");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample26() throws IOException, ParseException
    {
        testbeautification("Sample26");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample27() throws IOException, ParseException
    {
        testbeautification("Sample27");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample28() throws IOException, ParseException
    {
        testbeautification("Sample28");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample29() throws IOException, ParseException
    {
        testbeautification("Sample29");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample30() throws IOException, ParseException
    {
        testbeautification("Sample30");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample31() throws IOException, ParseException
    {
        testbeautification("Sample31");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample32() throws IOException, ParseException
    {
        testbeautification("Sample32");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample33() throws IOException, ParseException
    {
        testbeautification("Sample33");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample34() throws IOException, ParseException
    {
        testbeautification("Sample34");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample35() throws IOException, ParseException
    {
        testbeautification("Sample35");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample36() throws IOException, ParseException
    {
        testbeautification("Sample36");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample37() throws IOException, ParseException
    {
        testbeautification("Sample37");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample38() throws IOException, ParseException
    {
        testbeautification("Sample38");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample39() throws IOException, ParseException
    {
        testbeautification("Sample39");
    }

    /**
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSample40() throws IOException, ParseException
    {
        testbeautification("Sample40");
    }

    /**
     * Execute beautification on the given file and compare result
     * 
     * @param pFilename
     *            filename to be used as input for beautification
     * @throws IOException
     * @throws ParseException
     */
    private void testbeautification(String pFilename) throws IOException, ParseException
    {
        String lInput = IOUtils.toString(this.getClass().getResourceAsStream(
                pFilename + ".java_input"));

        String lOutput = mProcessor.organizeImports(lInput);
        // Use to create expected output if necessary
        //File outputFile = new File("C:/workspaces/A34/andromda341/maven/beautifier/src/test/resources/org/andromda/beautifier/core/" + pFilename + ".java");
        //FileUtils.writeStringToFile(outputFile, lOutput);

        String lExpectedOutput = IOUtils.toString(this.getClass().getResourceAsStream(
                pFilename + ".java_output"));
        assertEquals(normalized(lExpectedOutput), normalized(lOutput));
    }

    private String normalized(String pString)
    {
        return pString.trim().replaceAll("\r\n", System.getProperty("line.separator"));
    }
}
