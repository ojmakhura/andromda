package org.andromda.maven.doxia.module.xdoc;

/*
 * Based on The Apache Software Foundation XdocModule
 */

import org.apache.maven.doxia.site.module.AbstractSiteModule;

/**
 * @version $Id: AndromdadocSiteModule.java,v 1.1.2.5 2008-03-01 14:37:21 vancek Exp $
 * 
 * Based taken from Apache Foundation Doxia Project.
 * 
 * @plexus.component role="org.apache.maven.doxia.site.module.SiteModule" role-hint="andromdadoc"
 */
public class AndromdadocSiteModule
    extends AbstractSiteModule
{
    public String getSourceDirectory()
    {
        return "axdoc";
    }

    public String getExtension()
    {
        return "xml";
    }

    public String getParserId()
    {
        return "andromdadoc";
    }
}
