package org.andromda.maven.plugin.andromdapp.eclipse;

import java.io.File;
import java.io.FileWriter;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.andromda.core.common.ResourceUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.PrettyPrintXMLWriter;
import org.codehaus.plexus.util.xml.XMLWriter;


/**
 * Writes the Eclipse .classpath files.
 *
 * @author Chad Brandon
 */
public class ClasspathWriter
    extends EclipseWriter
{
    public ClasspathWriter(final MavenProject project, final Log logger)
    {
        super(project, logger);
    }

    /**
     * Writes the .classpath files for Eclipse.
     */
    public void write(
        final List projects,
        final String repositoryVariableName,
        final ArtifactFactory artifactFactory,
        final ArtifactResolver artifactResolver,
        final ArtifactRepository localRepository)
        throws Exception
    {
        final String rootDirectory = ResourceUtils.normalizePath(this.project.getBasedir().toString());
        final File classpathFile = new File(rootDirectory, ".classpath");
        final FileWriter fileWriter = new FileWriter(classpathFile);
        final XMLWriter writer = new PrettyPrintXMLWriter(fileWriter);

        writer.startElement("classpath");

        final Set allArtifacts = new LinkedHashSet();
        for (final Iterator iterator = projects.iterator(); iterator.hasNext();)
        {
            final MavenProject project = (MavenProject)iterator.next();
            for (final Iterator sourceIterator = project.getCompileSourceRoots().iterator(); sourceIterator.hasNext();)
            {
                final String sourceRoot = ResourceUtils.normalizePath((String)sourceIterator.next());
                String sourceRootPath = StringUtils.replace(
                        sourceRoot,
                        rootDirectory,
                        "");
                if (sourceRootPath.startsWith("/"))
                {
                    sourceRootPath = sourceRootPath.substring(
                            1,
                            sourceRootPath.length());
                    writer.startElement("classpathentry");
                    writer.addAttribute(
                        "kind",
                        "src");
                    writer.addAttribute(
                        "path",
                        sourceRootPath);
                    writer.endElement();
                }
            }
            allArtifacts.addAll(project.createArtifacts(
                    artifactFactory,
                    null,
                    null));
        }

        // - remove the project artifacts
        for (final Iterator iterator = projects.iterator(); iterator.hasNext();)
        {
            final MavenProject project = (MavenProject)iterator.next();
            final Artifact projectArtifact = project.getArtifact();
            if (projectArtifact != null)
            {
                for (final Iterator artifactIterator = allArtifacts.iterator(); artifactIterator.hasNext();)
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
        for (final Iterator iterator = allArtifacts.iterator(); iterator.hasNext();)
        {
            final Artifact artifact = (Artifact)iterator.next();
            artifactResolver.resolve(
                artifact,
                this.project.getRemoteArtifactRepositories(),
                localRepository);
            if (Artifact.SCOPE_COMPILE.equals(artifact.getScope()))
            {
                final File artifactFile = artifact.getFile();
                final String path =
                    StringUtils.replace(
                        ResourceUtils.normalizePath(artifactFile.toString()),
                        ResourceUtils.normalizePath(localRepository.getBasedir()),
                        repositoryVariableName);
                writer.startElement("classpathentry");
                writer.addAttribute("kind", "var");
                writer.addAttribute(
                    "path",
                    path);
                writer.endElement();
            }
        }
        writer.startElement("classpathentry");
        writer.addAttribute(
            "con",
            "org.eclipse.jdt.launching.JRE_CONTAINER");
        writer.endElement();

        writer.endElement();

        logger.info("Classpath file written --> '" + classpathFile + "'");
        IOUtil.close(fileWriter);
    }
}