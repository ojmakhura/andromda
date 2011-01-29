package org.andromda.maven.plugin.andromdapp.eclipse;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;

/**
 * An 'OR' artifact filter.  That means if one of the artifact filters applies
 * include returns true.
 *
 * @author Chad Brandon
 */
public class OrArtifactFilter
    implements ArtifactFilter
{
    private final List<ArtifactFilter> filters = new ArrayList<ArtifactFilter>();

    /**
     * @see org.apache.maven.artifact.resolver.filter.ArtifactFilter#include(org.apache.maven.artifact.Artifact)
     */
    public boolean include(final Artifact artifact)
    {
        boolean include = false;
        for (final ArtifactFilter filter : this.filters)
        {
            if (filter.include(artifact))
            {
                include = true;
                break;
            }
        }
        return include;
    }

    /**
     * Adds the artifact filter to be applied.
     *
     * @param artifactFilter
     */
    public void add(final ArtifactFilter artifactFilter)
    {
        this.filters.add(artifactFilter);
    }
}