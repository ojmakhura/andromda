package org.andromda.maven.plugin.distribution;

import java.io.File;
import java.io.IOException;

import java.text.Collator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.profiles.DefaultProfileManager;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;


/**
 * A Mojo for assembling a distribution.
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
     * @parameter
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
     * The archiver.
     *
     * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#jar}"
     * @required
     */
    private JarArchiver binArchiver;

    /**
     * @component
     */
    private ArtifactMetadataSource artifactMetadataSource;

    /**
     * The maven archiver to use.
     *
     * @parameter
     */
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

    /**
     * Used to contruct Maven project instances from POMs.
     *
     * @component
     */
    private MavenProjectBuilder projectBuilder;

    /**
     * @parameter expression="${session}"
     */
    private MavenSession session;

    /**
     * Defines the POMs who's artifacts will be included in the distribution.
     *
     * @parameter
     */
    private String[] projectIncludes = new String[0];

    /**
     * Defines the POMs who's artifacts will be excluded from the distribution.
     *
     * @parameter
     */
    private String[] projectExcludes = new String[0];

    /**
     * The directory from which the search for POMs starts.
     *
     * @parameter
     */
    private String projectBaseDirectory;

    /**
     * The directory in which project artifacts are bundled.
     *
     * @parameter
     */
    private String artifactDirectory;

    /**
     * The directory in which dependencies of project artifacts are bundled.
     *
     * @parameter
     */
    private String dependencyDirectory;

    /**
     * The artifacts that can be excluded from the distribution.
     *
     * @parameter
     */
    private ArtifactFilter[] artifactExcludes;

    /**
     * The locations to include when creating the distribution.
     *
     * @parameter
     */
    private Location[] locations;

    /**
     * All artifacts that are collected and bundled.
     */
    private final Set allArtifacts = new LinkedHashSet();

    /**
     * The forward slash character.
     */
    private static final String FORWARD_SLASH = "/";

    /**
     * The extension to give the distribution.
     *
     * @parameter expression="zip"
     * @required
     */
    private String extension;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        this.allArtifacts.clear();
        try
        {
            final File distribution = new File(this.workDirectory, this.name + '.' + this.extension);
            this.getLog().info("Building distribution " + distribution);
            final File directory = this.getDistributionDirectory();
            directory.mkdirs();
            final List artifactList = new ArrayList();
            final List projects = this.collectProjects();
            if (!projects.isEmpty() && this.projectBaseDirectory != null && this.projectBaseDirectory.trim().length() > 0)
            {
                final Set artifacts = new LinkedHashSet();
                for (final Iterator iterator = projects.iterator(); iterator.hasNext();)
                {
                    final MavenProject project = (MavenProject)iterator.next();
                    final Artifact artifact =
                        this.artifactFactory.createArtifact(
                            project.getGroupId(),
                            project.getArtifactId(),
                            project.getVersion(),
                            null,
                            project.getPackaging());
                    final String artifactPath = this.localRepository.pathOf(artifact);
                    final String artifactFileName = artifactPath.replaceAll(
                            ".*(\\\\|/+)",
                            "");
                    final String repositoryDirectoryPath =
                        artifactPath.substring(
                            0,
                            artifactPath.indexOf(artifactFileName));
                    final Build build = project.getBuild();
                    final File workDirectory = new File(build.getDirectory());
                    if (workDirectory.exists())
                    {
                        final File distributionDirectory =
                            new File(new File(
                                    directory,
                                    this.artifactDirectory), repositoryDirectoryPath);
                        final String finalName = build.getFinalName();
                        final String[] names = workDirectory.list();
                        if (names != null)
                        {
                            final int numberOfArtifacts = names.length;
                            for (int ctr = 0; ctr < numberOfArtifacts; ctr++)
                            {
                                final String name = names[ctr];
                                if (name.indexOf(finalName) != -1 && !name.equals(finalName))
                                {
                                    final File distributionFile = new File(distributionDirectory, name);
                                    this.bundleFile(
                                        artifact,
                                        new File(
                                            workDirectory,
                                            name),
                                        distributionFile);
                                }
                            }
                        }

                        final File repositoryPom =
                            this.constructPom(
                                new File(
                                    this.localRepository.getBasedir(),
                                    repositoryDirectoryPath),
                                artifact);
                        final File distributionPom = this.constructPom(
                                distributionDirectory,
                                artifact);
                        this.bundleFile(
                            artifact,
                            repositoryPom,
                            distributionPom);
                    }
                    else
                    {
                        this.artifactResolver.resolve(
                            artifact,
                            this.project.getRemoteArtifactRepositories(),
                            this.localRepository);
                    }
                    artifacts.addAll(project.createArtifacts(
                            artifactFactory,
                            null,
                            null));
                }

                final ArtifactResolutionResult result =
                    this.artifactResolver.resolveTransitively(
                        artifacts,
                        this.project.getArtifact(),
                        this.project.getRemoteArtifactRepositories(),
                        this.localRepository,
                        this.artifactMetadataSource);

                artifacts.addAll(result.getArtifacts());

                // - remove the project artifacts
                for (final Iterator iterator = projects.iterator(); iterator.hasNext();)
                {
                    final MavenProject project = (MavenProject)iterator.next();
                    final Artifact projectArtifact = project.getArtifact();
                    if (projectArtifact != null)
                    {
                        for (final Iterator artifactIterator = artifacts.iterator(); artifactIterator.hasNext();)
                        {
                            final Artifact artifact = (Artifact)artifactIterator.next();
                            final String projectId = projectArtifact.getArtifactId();
                            final String projectGroupId = projectArtifact.getGroupId();
                            final String artifactId = artifact.getArtifactId();
                            final String groupId = artifact.getGroupId();
                            if (artifactId.equals(projectId) && groupId.equals(projectGroupId))
                            {
                                artifactIterator.remove();
                            }
                        }
                    }
                }

                // - bundle the dependant artifacts
                for (final Iterator iterator = artifacts.iterator(); iterator.hasNext();)
                {
                    final Artifact artifact = (Artifact)iterator.next();
                    this.bundleArtifact(
                        new File(
                            directory,
                            this.dependencyDirectory),
                        artifact);
                }

                artifactList.addAll(this.allArtifacts);

                Collections.sort(
                    artifactList,
                    new ArtifactComparator());
                for (final Iterator iterator = artifactList.iterator(); iterator.hasNext();)
                {
                    this.getLog().info("bundled: " + ((Artifact)iterator.next()).getId());
                }
                this.getLog().info("Bundled " + artifactList.size() + " artifacts");
            }

            int bundledFilesCount = 0;

            // - now include all paths found in the given locations
            if (this.locations != null)
            {
                final int numberOfLocations = this.locations.length;
                for (int ctr = 0; ctr < numberOfLocations; ctr++)
                {
                    final Location location = this.locations[ctr];
                    final List paths = location.getPaths();
                    if (paths != null)
                    {
                        for (final Iterator iterator = paths.iterator(); iterator.hasNext();)
                        {
                            final String path = (String)iterator.next();
                            final File file = location.getFile(path);
                            File destination = null;
                            final String outputPath = location.getOuputPath();
                            if (outputPath != null && outputPath.trim().length() > 0)
                            {
                                final File outputPathFile = new File(
                                        this.getDistributionDirectory(),
                                        outputPath);

                                // - directories must end with a slash
                                if (outputPath.endsWith(FORWARD_SLASH))
                                {
                                    destination = new File(
                                            outputPathFile,
                                            path);
                                }
                                else
                                {
                                    destination = outputPathFile;
                                }
                            }
                            else
                            {
                                destination = new File(
                                        this.getDistributionDirectory(),
                                        path);
                            }
                            if (destination != null)
                            {
                                FileUtils.copyFile(
                                    file,
                                    destination);
                                if (this.getLog().isDebugEnabled())
                                    this.getLog().debug("bundled: " + destination);
                                bundledFilesCount++;
                            }
                        }
                    }
                }
            }

            this.getLog().info("Bundled " + bundledFilesCount + " files");

            final MavenArchiver archiver = new MavenArchiver();
            archiver.setArchiver(this.binArchiver);
            archiver.setOutputFile(distribution);

            archiver.getArchiver().addDirectory(
                directory,
                new String[] {"**/*"},
                null);

            // - create archive
            archiver.createArchive(
                this.project,
                this.archive);
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error assembling distribution", throwable);
        }
    }

    /**
     * Bundles the file from the given <code>artifact</code> into the given
     * <code>destinationDirectory</code>.
     *
     * @param destinationDirectory the directory to which the artifact is
     *        bundled.
     * @param artifact the artifact to bundle.
     * @throws IOException
     */
    private void bundleArtifact(
        final File destinationDirectory,
        final Artifact artifact)
        throws IOException
    {
        File artifactFile = artifact.getFile();
        if (artifactFile == null)
        {
            artifactFile = new File(
                    this.localRepository.getBasedir(),
                    this.localRepository.pathOf(artifact));
        }
        final String artifactPath = this.localRepository.pathOf(artifact);
        final String artifactFileName = artifactPath.replaceAll(
                ".*(\\\\|/+)",
                "");
        final String repositoryDirectoryPath = artifactPath.substring(
                0,
                artifactPath.indexOf(artifactFileName));
        final File dependencyDirectory = new File(destinationDirectory, repositoryDirectoryPath);
        this.bundleFile(
            artifact,
            artifactFile,
            new File(
                destinationDirectory,
                repositoryDirectoryPath + '/' + artifactFile.getName()));
        final File repositoryPom =
            this.constructPom(
                new File(
                    this.localRepository.getBasedir(),
                    repositoryDirectoryPath),
                artifact);

        if (repositoryPom.exists())
        {
            final File distributionPom = this.constructPom(
                    dependencyDirectory,
                    artifact);
            this.bundleFile(
                artifact,
                repositoryPom,
                distributionPom);
        }
    }

    /**
     * Constructs the POM file given the <code>directory</code> and the
     * <code>artifact</code>.
     *
     * @param directory the directory.
     * @param artifact the artifact.
     * @return the POM file.
     */
    private final File constructPom(
        final File directory,
        final Artifact artifact)
    {
        return new File(
            directory,
            artifact.getArtifactId() + '-' + artifact.getVersion() + '.' + POM_TYPE);
    }

    /**
     * Copies the given <code>file</code> to the given
     * <code>destination</code>.
     *
     * @param artifact the artifact that is being bundled.
     * @param file the file to bundle.
     * @param destination the destination to which we'll bundle.
     * @throws IOException
     */
    private void bundleFile(
        final Artifact artifact,
        final File file,
        final File destination)
        throws IOException
    {
        boolean writable = true;
        if (this.artifactExcludes != null)
        {
            final String artifactGroupId = artifact.getGroupId();
            final String artifactArtifactId = artifact.getArtifactId();
            final int numberOfArtifactExcludes = this.artifactExcludes.length;
            for (int ctr = 0; ctr < numberOfArtifactExcludes; ctr++)
            {
                final ArtifactFilter artifactExclude = this.artifactExcludes[ctr];
                if (artifactExclude != null)
                {
                    final String groupId = artifactExclude.getGroupId();
                    final String artifactId = artifactExclude.getArtifactId();
                    final boolean groupIdPresent = groupId != null;
                    final boolean artifactIdPresent = artifactId != null;
                    if (groupIdPresent)
                    {
                        writable = !artifactGroupId.matches(groupId);
                        if (!writable && artifactIdPresent)
                        {
                            writable = !artifactArtifactId.matches(artifactId);
                        }
                    }
                    else if (artifactIdPresent)
                    {
                        writable = !artifactGroupId.matches(artifactId);
                    }
                }
            }
        }
        if (writable)
        {
            this.allArtifacts.add(artifact);
            FileUtils.copyFile(
                file,
                destination);
        }
        else
        {
            if (this.getLog().isDebugEnabled())
            {
                this.getLog().debug("Excluding: " + artifact.getId());
            }
        }
    }

    /**
     * Retrieves all the POMs for the given project.
     *
     * @return all poms found.
     * @throws MojoExecutionException
     */
    private List getPoms()
    {
        List poms = new ArrayList();
        if (this.projectBaseDirectory != null && this.projectBaseDirectory.trim().length() > 0)
        {
            final DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir(this.projectBaseDirectory);
            scanner.setIncludes(this.projectIncludes);
            scanner.setExcludes(this.projectExcludes);
            scanner.scan();
            for (int ctr = 0; ctr < scanner.getIncludedFiles().length; ctr++)
            {
                final File pom = new File(this.projectBaseDirectory, scanner.getIncludedFiles()[ctr]);
                if (pom.exists())
                {
                    poms.add(pom);
                }
            }
        }
        return poms;
    }

    /**
     * Collects all projects from all POMs within the current project.
     *
     * @return all collection Maven project instances.
     * @throws MojoExecutionException
     */
    private MavenProject buildProject(final File pom)
        throws MojoExecutionException
    {
        try
        {
            final MavenProject project =
                this.projectBuilder.build(
                    pom,
                    this.session.getLocalRepository(),
                    new DefaultProfileManager(this.session.getContainer()));
            if (this.getLog().isDebugEnabled())
            {
                this.getLog().debug("Processing project " + project.getId());
            }
            return project;
        }
        catch (ProjectBuildingException exception)
        {
            throw new MojoExecutionException("Error loading " + pom, exception);
        }
    }

    /**
     * Collects all projects from all POMs within the current project.
     *
     * @return all collection Maven project instances.
     * @throws MojoExecutionException
     * @throws MojoExecutionException
     */
    private List collectProjects()
        throws MojoExecutionException
    {
        final List projects = new ArrayList();
        final List poms = this.getPoms();
        for (ListIterator iterator = poms.listIterator(); iterator.hasNext();)
        {
            final File pom = (File)iterator.next();
            projects.add(this.buildProject(pom));
        }
        return projects;
    }

    /**
     * The POM artifact type.
     */
    private static final String POM_TYPE = "pom";

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

        public int compare(
            final Object objectA,
            final Object objectB)
        {
            final Artifact a = (Artifact)objectA;
            final Artifact b = (Artifact)objectB;
            return collator.compare(
                a.getId(),
                b.getId());
        }
    }

    /**
     * Gets the directory to which the output is written for the binary
     * distribution.
     *
     * @return the directory output distribution.
     */
    private File getDistributionDirectory()
    {
        return new File(this.workDirectory + '/' + this.name);
    }
}