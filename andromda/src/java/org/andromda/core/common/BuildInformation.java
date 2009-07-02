package org.andromda.core.common;

import java.io.InputStream;

import java.net.URL;

import java.util.Properties;


/**
 * This class provides statistics on how the build was performed.
 *
 * @author Martin West
 * @author Chad Brandon
 */
public class BuildInformation
{
    /**
     * The shared instance.
     */
    private static final BuildInformation instance = new BuildInformation();

    /**
     * Gets the shared instance of the BuildInformation.
     *
     * @return the shared BuildInformation instance.
     */
    public static BuildInformation instance()
    {
        return instance;
    }

    /**
     * Private default constructor of BuildInformation. This class is not intended to be instantiated.
     */
    private BuildInformation()
    {
        this.initialize();
    }

    /**
     * The build timestamp.
     */
    private String buildDate;

    /**
     * The build operating system and version.
     */
    private String buildSystem;

    /**
     * The JDK details used to build the system.
     */
    private String buildJdk;

    /**
     * The name of the user that built the system.
     */
    private String buildBuilder;

    /**
     * The version of the AndroMDA build.
     */
    private String buildVersion;

    private void initialize()
    {
        final String buildPropertiesUri = "META-INF/andromda-build.properties";
        final String versionPropertyName = "andromda.build.version";
        final String datePropertyName = "andromda.build.date";
        final String systemPropertyName = "andromda.build.system";
        final String jdkPropertyName = "andromda.build.jdk";
        final String builderPropertyName = "andromda.build.builder";
        final URL versionUri = ResourceUtils.getResource(buildPropertiesUri);
        try
        {
            if (versionUri == null)
            {
                throw new IllegalStateException(
                    "BuildInformation: could not load file --> '" + buildPropertiesUri + "'");
            }
            final Properties properties = new Properties();
            InputStream stream = versionUri.openStream();
            properties.load(stream);
            stream.close();

            this.buildDate = properties.getProperty(datePropertyName);
            this.buildSystem = properties.getProperty(systemPropertyName);
            this.buildJdk = properties.getProperty(jdkPropertyName);
            this.buildBuilder = properties.getProperty(builderPropertyName);
            this.buildVersion = properties.getProperty(versionPropertyName);
        }
        catch (final Throwable throwable)
        {
            ExceptionRecorder.instance().record(throwable);
            throw new IllegalStateException(throwable.getMessage());
        }
    }

    /**
     * Return the name of the operating system and version.
     *
     * @return Returns the build version.
     */
    public String getBuildVersion()
    {
        return this.buildVersion;
    }

    /**
     * Return the user name of the id which built the system.
     *
     * @return Returns the build builder.
     */
    public String getBuildBuilder()
    {
        return this.buildBuilder;
    }

    /**
     * Return the timestamp of the build.
     *
     * @return Returns the build date.
     */
    public String getBuildDate()
    {
        return this.buildDate;
    }

    /**
     * Return the vendor and jdk version.
     *
     * @return Returns the build jdk.
     */
    public String getBuildJdk()
    {
        return this.buildJdk;
    }

    /**
     * Return the name of the operating system and version.
     *
     * @return Returns the build system.
     */
    public String getBuildSystem()
    {
        return this.buildSystem;
    }
}