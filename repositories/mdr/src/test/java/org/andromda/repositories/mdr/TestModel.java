package org.andromda.repositories.mdr;

import java.net.URL;

import org.apache.log4j.Logger;

public class TestModel
{
    private static final Logger logger = Logger.getLogger(TestModel.class);

    public static URL getModel()
    {
        try
        {
            String testModel = "/TestModel.zuml";
            URL modelUri = TestModel.class.getResource(testModel);
            if (modelUri == null)
            {
                throw new RuntimeException("Could not load '" + testModel + "'");
            }
            if (modelUri != null)
            {
                String jarUrl = "jar:" + modelUri.toString()
                    + "!/TestModel.xmi";
                modelUri = new URL(jarUrl);
                if (modelUri == null)
                {
                    throw new RuntimeException("Could not load '" + jarUrl
                        + "'");
                }
            }
            return modelUri;
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing TestModel.getModel()";
            logger.error(errMsg, th);
            throw new RuntimeException(errMsg, th);
        }
    }
}