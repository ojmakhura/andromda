package org.andromda.maven;

/**
 * Just contains some simple utilities used within 
 * the AndroMDA maven plugin.
 * 
 * @author Chad Brandon
 */
public class MavenPluginUtils {
    
    /**
     * Retrieves the artifactId from the passed in
     * <code>artifact</code> string formatted 
     * like <groupId>:<artifactId>.
     * @param artifact the string from which to retrieve
     *        the artifactId.
     * @return String
     */
    public String getArtifactId(String artifact) {
        String artifactId = "";
        if (artifact != null) {
            artifact = artifact.trim();
            int index = artifact.indexOf(':');
            if (index != -1) {
                artifactId = artifact.substring(index + 1, artifact.length());
            }
        }
        return artifactId;
    }
    
    /**
     * Retrieves the groupId from the passed in
     * <code>artifact</code> string formatted 
     * like <groupId>:<artifactId>.
     * @param artifact the string from which to retrieve
     *        the groupId.
     * @return String
     */
    public String getGroupId(String artifact) {
        String groupId = "";
        if (artifact != null) {
            artifact = artifact.trim();
            int index = artifact.indexOf(':');
            if (index != -1) {
                groupId = artifact.substring(0, index);
            }
        }
        return groupId;
    }
    
}
