/*
 * Created on 24-Jan-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.andromda.core.common;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * 
 * @author martin
 *
 */
public class BuildInformation
{

    static final String BUILD_DATE;
    static final String BUILD_SYSTEM;
    static final String BUILD_JDK;
    static final String BUILD_BUILDER;
    
    static
    {
        final String buildPropertiesUri = "META-INF/andromda-build.properties";
        final String datePropertyName = "andromda.build.date";
        final String systemPropertyName = "andromda.build.system";
        final String jdkPropertyName = "andromda.build.jdk";
        final String builderPropertyName = "andromda.build.builder";
        try
        {
            URL versionUri = ResourceUtils.getResource(buildPropertiesUri);
            if (versionUri == null)
            {
                throw new IllegalStateException("BuildInformation:Could not load file --> '"
                    + buildPropertiesUri + "'");
            }
            Properties properties = new Properties();
            InputStream stream = versionUri.openStream();
            properties.load(stream);
            stream.close();
            stream = null;
            BUILD_DATE = properties.getProperty(datePropertyName);
            BUILD_SYSTEM = properties.getProperty(systemPropertyName);
            BUILD_JDK = properties.getProperty(jdkPropertyName);
            BUILD_BUILDER = properties.getProperty(builderPropertyName);
        }
        catch (IllegalStateException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            ExceptionRecorder.record( th );
            throw new IllegalStateException(th.getMessage());
        }
    }


    /**
     * @return Returns the Build BUILDER.
     */
    public static String getBUILD_BUILDER()
    {
        return BUILD_BUILDER;
    }
    /**
     * @return Returns the BUILD DATE.
     */
    public static String getBUILD_DATE()
    {
        return BUILD_DATE;
    }
    /**
     * @return Returns the BUILD JDK.
     */
    public static String getBUILD_JDK()
    {
        return BUILD_JDK;
    }
    /**
     * @return Returns the BUILD SYSTEM.
     */
    public static String getBUILD_SYSTEM()
    {
        return BUILD_SYSTEM;
    }
}
