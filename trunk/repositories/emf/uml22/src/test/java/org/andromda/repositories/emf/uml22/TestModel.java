package org.andromda.repositories.emf.uml22;

import java.net.URL;
import org.apache.log4j.Logger;
import org.junit.Ignore;

/**
 * @author Bob
 *
 */
@Ignore//ignore "Test*" for JUnit
public class TestModel
{
    private static final Logger logger = Logger.getLogger(TestModel.class);

    /**
     * @return TestModel.class.getResource("/Test-Model.emx")
     */
    public static URL getModel()
    {
        try
        {
            // UML2 Standard profile older version from RSA7 no longer works for reading stereotypes
            //String testModel = "/Test-Model.emx";
            String testModel = "/Test-Model.uml";
            URL modelUri = TestModel.class.getResource(testModel);
            if (modelUri == null)
            {
                throw new RuntimeException("Could not load '" + testModel + '\'');
            }
            return modelUri;
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing TestModel.getModel()";
            logger.error(
                errMsg,
                th);
            throw new RuntimeException(errMsg, th);
        }
    }

    /**
     * @return TestModel.class.getResource("/Test-Model.emx")
     */
    public static URL getRSAModel()
    {
        try
        {
            // UML2 Standard profile older version from RSA7 no longer works for reading stereotypes
            String testModel = "/Test-Model.emx";
            URL modelUri = TestModel.class.getResource(testModel);
            if (modelUri == null)
            {
                throw new RuntimeException("Could not load '" + testModel + '\'');
            }
            return modelUri;
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing TestModel.getModel()";
            logger.error(
                errMsg,
                th);
            throw new RuntimeException(errMsg, th);
        }
    }
}
