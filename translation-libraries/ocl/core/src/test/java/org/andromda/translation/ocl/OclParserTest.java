package org.andromda.translation.ocl;

import java.io.FileReader;
import java.io.PushbackReader;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.translation.ocl.analysis.DepthFirstAdapter;
import org.andromda.translation.ocl.lexer.Lexer;
import org.andromda.translation.ocl.node.Start;
import org.andromda.translation.ocl.parser.OclParser;

/**
 * Implements the JUnit test suite for {@link org.andromda.translation.ocl.parser.OclParser}
 *
 * @author Chad Brandon
 */
public class OclParserTest
        extends TestCase
{

    private String PACKAGE_DIR = ClassUtils.getPackageName(OclParserTest.class).replace('.', '/');

    /**
     * Location of a file containing valid OCL syntax.
     */
    private String VALID_SYNTAX = PACKAGE_DIR + "/valid-syntax.ocl";

    /**
     * Constructor for ModelFacadeTest.
     *
     * @param testName
     */
    public OclParserTest(String testName)
    {
        super(testName);
    }

    public void setUp() throws Exception
    {
        super.setUp();
        AndroMDALogger.initialize();
    }

    /**
     * Assembles test suite of all known tests
     *
     * @return non-null test suite
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(OclParserTest.class);
        return suite;
    }

    public void testValidExpressions()
    {
        try
        {
            URL url = ResourceUtils.getResource(VALID_SYNTAX);
            if (url == null)
            {
                TestCase.fail("Could not load resource '" + VALID_SYNTAX + '\'');
            }
            DepthFirstAdapter adapter = new DepthFirstAdapter();
            Lexer lexer = new Lexer(new PushbackReader(new FileReader(url.getFile())));
            OclParser parser = new OclParser(lexer);
            Start startNode = parser.parse();
            startNode.apply(adapter);
        }
        catch (Throwable th)
        {
            th.printStackTrace();
            TestCase.fail(th.toString());
        }
    }

    /**
     * Runs the test suite
     */
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }
}