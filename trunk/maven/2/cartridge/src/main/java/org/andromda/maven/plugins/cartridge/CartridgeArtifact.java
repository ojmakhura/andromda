package org.andromda.maven.plugins.cartridge;



/**
 * Represents a cartridge library to be included in a cartridge.
 *
 * @author Chad Brandon
 */
public class CartridgeArtifact
{
    private String artifactId;

    /**
     * Gets the artifactId of this cartridge library.
     *
     * @return Returns the artifactId.
     */
    public String getArtifactId()
    {
        return artifactId;
    }

    private String groupId;

    /**
     * Gets the groupId of this cartridge library.
     *
     * @return Returns the groupId.
     */
    public String getGroupId()
    {
        return groupId;
    }

    private String type;

    /**
     * Gets the type of this artifact.
     *
     * @return Returns the type.
     */
    public String getType()
    {
        return type;
    }

    private String path;

    /**
     * Gets the relative path the library will reside within the cartridge.
     *
     * @return Returns the path.
     */
    public String getPath()
    {
        return path;
    }
}