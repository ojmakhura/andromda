package org.andromda.repositories.emf.uml2;

import java.net.URL;

import org.apache.log4j.Logger;
import org.junit.Ignore;

@Ignore//ignore "Test*" for JUnit
public class TestModel
{
    private static final Logger logger = Logger.getLogger(TestModel.class);

    public static URL getModel()
    {
        try
        {
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