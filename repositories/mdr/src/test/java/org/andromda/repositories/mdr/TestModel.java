package org.andromda.repositories.mdr;

import java.net.URL;

import org.apache.log4j.Logger;

public class TestModel
{
    private static final Logger logger = Logger.getLogger(TestModel.class);
    
    public static URL MODEL_URI = null;
    
    static {
        try {
            MODEL_URI = TestModel.class.getResource("/TestModel.zargo");
            if (MODEL_URI != null) {
                MODEL_URI = new URL("jar:" + MODEL_URI.toString() + "!/TestModel.xmi");
            }
        } catch (Throwable th) {
        	logger.error(th);
        }
    }
}
