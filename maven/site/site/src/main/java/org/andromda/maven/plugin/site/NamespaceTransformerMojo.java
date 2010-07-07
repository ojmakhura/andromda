package org.andromda.maven.plugin.site;

import java.io.File;
import java.net.URL;
import org.andromda.core.common.ResourceUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Used to perform the transformation of the namespace XSL document to namespace.xml xdoc format
 * within the site plugin.
 *
 * @phase pre-site
 * @goal namespace-xsl
 * @description runs AndroMDA site namespace xsl transformation
 * @author Vance Karimi
 */
public class NamespaceTransformerMojo
    extends AbstractMojo
{
    /**
     * The name of the project injected from pom.xml
     * 
     * @parameter default-value="${project.name}"
     */
    private String projectName;
    
    /**
     * Path to the project namespace.xml
     * 
     * @parameter expression="${basedir}/src/main/resources/META-INF/andromda/namespace.xml"
     */
    private File namespaceDocumentPath;
    
    /**
     * Path to the project namespace transformation XSL
     */
    private static final String NAMESPACE_TRANSFORMATION_URI = "META-INF/xsl/namespace.xsl";
    
    /** Not used
     * @parameter expression="${basedir}/src/main/resources/META-INF/xsl/namespace.xsl"
     */
    @SuppressWarnings("unused")
    private File namespaceTransformationPath;
    
    /**
     * Path to the project namespace document output
     * 
     * @parameter expression="${basedir}/src/site/xdoc/namespace.xml"
     */
    private File namespaceOutputPath;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;
    
    /**
     * XSL Transformer
     */
    private XslTransformer xslTransformer;
    
    
    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        this.getLog().info("-----------------------------------------------------------------------------");
        this.getLog().info("  A n d r o M D A   S i t e   N a m e s p a c e   T r a n s f o r m a t i o n");
        this.getLog().info("-----------------------------------------------------------------------------");

        if (xslTransformer == null)
        {
            xslTransformer = new XslTransformer(projectName);
        }
        
        this.getLog().info("Transforming namespace " + this.namespaceDocumentPath);
        
        try
        {
            final File namespaceDocumentFile = this.namespaceDocumentPath;
            if (namespaceDocumentFile.exists() && namespaceDocumentFile.isFile())
            {
                final URL namespaceTransformationUri = ResourceUtils.getResource(NAMESPACE_TRANSFORMATION_URI);
                xslTransformer.transform(namespaceDocumentPath.getAbsolutePath(), namespaceTransformationUri, namespaceOutputPath.getAbsolutePath());
            }
            
            this.getLog().info("Transformed namesapce " + this.namespaceOutputPath);
            this.getLog().info("TRANSFORMING NAMESPACE SUCCESSFUL");
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occured creating namespace site document '" +
                this.project.getArtifactId() + '\'',
                ExceptionUtils.getRootCause(throwable));
        }
    }

}
