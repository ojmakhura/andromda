package org.andromda.maven.plugin.distribution;


/**
 * Reprensets an artifact used to capture includes/excludes information.
 *
 * @author Chad Brandon
 */
public class ArtifactFilter
{
    /**
     * The artifact identier.
     */
    private String artifactId;

    /**
     * The group identifier
     */
    private String groupId;

    /**
     * Gets the artifact id.
     *
     * @return Returns the artifactId.
     */
    public String getArtifactId()
    {
        return artifactId;
    }

    /**
     * Sets the artifactId.
     *
     * @param artifactId The artifactId to set.
     */
    public void setArtifactId(String artifactId)
    {
        this.artifactId = artifactId;
    }

    /**
     * Gets the groupId.
     *
     * @return Returns the groupId.
     */
    public String getGroupId()
    {
        return groupId;
    }

    /**
     * Sets the groupId.
     *
     * @param groupId The groupId to set.
     */
    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }
}