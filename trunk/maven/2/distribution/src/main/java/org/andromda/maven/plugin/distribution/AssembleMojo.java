package org.andromda.maven.plugin.distribution;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.andromda.core.common.ResourceUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

/**
 * A Mojo for assembling the AndroMDA distribution.
 * 
 * @goal assemble
 * @phase validate
 * @author Chad Brandon
 */
public class AssembleMojo
    extends AbstractMojo
{
    /**
     * The name of the distribution
     * 
     * @parameter expression="andromda"
     * @required
     */
    private String name;

    /**
     * Directory that resources are copied to during the build.
     * 
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private String workDirectory;

    /**
     * The maven project.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @description "the maven project to use"
     */
    private MavenProject project;

    /**
     * Artifact factory, needed to download source jars for inclusion in
     * classpath.
     * 
     * @component role="org.apache.maven.artifact.factory.ArtifactFactory"
     * @required
     * @readonly
     */
    private ArtifactFactory artifactFactory;

    /**
     * Artifact resolver, needed to download source jars for inclusion in
     * classpath.
     * 
     * @component role="org.apache.maven.artifact.resolver.ArtifactResolver"
     * @required
     * @readonly
     */
    private ArtifactResolver artifactResolver;

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

    /**
     * @component
     */
    private ArtifactMetadataSource artifactMetadataSource;

    /**
     * The pattern for AndroMDA specific artifacts (these are bundled in the
     * andromda specific directory).
     * 
     * @parameter
     */
    private String andromdaArtifactPattern = "org\\.andromda.*";

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            final File directory = this.getOutputDirectory();
            directory.mkdirs();
            final List dependencies = this.project.getDependencies();
            final Set artifacts = new LinkedHashSet();
            if (dependencies != null)
            {
                for (final Iterator iterator = dependencies.iterator(); iterator.hasNext();)
                {
                    final Dependency dependency = (Dependency)iterator.next();
                    final Artifact artifact = this.artifactFactory.createArtifact(
                        dependency.getGroupId(),
                        dependency.getArtifactId(),
                        dependency.getVersion(),
                        null,
                        dependency.getType());
                    this.artifactResolver.resolve(artifact, this.project
                        .getRemoteArtifactRepositories(), this.localRepository);
                    artifacts.add(artifact);
                }
            }

            final Artifact projectArtifact = artifactFactory.createArtifact(
                this.project.getGroupId(),
                this.project.getArtifactId(),
                this.project.getVersion(),
                null,
                this.project.getPackaging());

            final ArtifactResolutionResult result = artifactResolver.resolveTransitively(
                artifacts,
                projectArtifact,
                Collections.EMPTY_LIST,
                this.localRepository,
                this.artifactMetadataSource);
            artifacts.addAll(result.getArtifacts());
            final List artifactList = new ArrayList(artifacts);
            Collections.sort(artifactList, new ArtifactComparator());
            for (final Iterator iterator = artifactList.iterator(); iterator.hasNext();)
            {
                final Artifact artifact = (Artifact)iterator.next();
                final String relativePath = ResourceUtils.normalizePath(this.localRepository
                    .pathOf(artifact));
                File outputFile;
                final String artifactId = artifact.getId();
                if (artifactId.matches(this.andromdaArtifactPattern))
                {
                    outputFile = new File(this.getOutputDirectory() + "/andromda", relativePath);
                }
                else
                {
                    outputFile = new File(this.getOutputDirectory() + "/lib", relativePath);
                }
                this.getLog().info("bundling: " + artifactId);
                FileUtils.copyFile(artifact.getFile(), outputFile);
            }
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error assembling distribution", throwable);
        }
    }

    /**
     * Used to sort artifacts by <code>id</code>.
     */
    private final static class ArtifactComparator
        implements Comparator
    {
        private final Collator collator = Collator.getInstance();

        private ArtifactComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        public int compare(final Object objectA, final Object objectB)
        {
            final Artifact a = (Artifact)objectA;
            final Artifact b = (Artifact)objectB;
            return collator.compare(a.getId(), b.getId());
        }
    }

    /**
     * Gets the directory to which the output is written for the distribution.
     * 
     * @return the directory output distribution.
     */
    private File getOutputDirectory()
    {
        return new File(workDirectory + '/' + name + '-' + project.getVersion());
    }
}