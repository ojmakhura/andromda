package org.andromda.maven.plugin.site;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import org.andromda.core.common.ResourceUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.InputSource;

/**
 * Used to perform the transformation of the profile XSL document to profile.xml xdoc format
 * within the site plugin.
 *
 * @phase pre-site
 * @goal profile-xsl
 * @description runs AndroMDA site profile xsl transformation
 * @author Vance Karimi
 */
public class ProfileTransformerMojo
    extends AbstractMojo
{
    /**
     * The name of the project injected from pom.xml
     * 
     * @parameter default-value="${project.name}"
     */
    private String projectName;
    
    /**
     * Path to the project profile.xml
     * 
     * @parameter expression="${basedir}/src/main/resources/META-INF/andromda/profile.xml"
     */
    private String profileDocumentPath;
    
    /**
     * Path to the project profile transformation XSL
     */
    private static final String PROFILE_TRANSFORMATION_URI = "META-INF/xsl/profile.xsl";
    
    /**
     * @parameter expression="${basedir}/src/main/resources/META-INF/xsl/profile.xsl"
     */
    private String profileTransformationPath;
    
    /**
     * Path to the project profile document output
     * 
     * @parameter expression="${basedir}/src/site/xdoc/profile.xml"
     */
    private String profileOutputPath;

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
        this.getLog().info("    A n d r o M D A   S i t e   P r o f i l e   T r a n s f o r m a t i o n  ");
        this.getLog().info("-----------------------------------------------------------------------------");

        if (xslTransformer == null)
        {
            xslTransformer = new XslTransformer(projectName);
        }
        
        this.getLog().info("Transforming profile " + this.profileDocumentPath);
        
        try
        {
            final File profileDocumentFile = new File(this.profileDocumentPath);
            if (profileDocumentFile.exists() && profileDocumentFile.isFile())
            {
                final URL profileTransformationUrl = ResourceUtils.getResource(PROFILE_TRANSFORMATION_URI);
                xslTransformer.transform(profileDocumentPath, profileTransformationUrl, profileOutputPath);
            }
            
            this.getLog().info("Transformation result " + this.profileOutputPath);
            this.getLog().info("TRANSFORMING PROFILE SUCCESSFUL");
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occured creating profile site document '" +
                this.project.getArtifactId() + '\'',
                ExceptionUtils.getRootCause(throwable));
        }
    }
}
