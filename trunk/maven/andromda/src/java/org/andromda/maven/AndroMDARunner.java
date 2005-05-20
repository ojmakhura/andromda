package org.andromda.maven;

import org.andromda.core.AndroMDA;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.configuration.Configuration;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.expression.Expression;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.jelly.MavenJellyContext;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;


public class AndroMDARunner
{
    /**
     * The AndroMDA instance (which runs AndroMDA).
     */
    private AndroMDA andromda;

    /**
     * Sets the URI of the configuration.
     *
     * @param configurationUri the URI to the configuration.
     *
     * @throws Exception if an error ocurrs while setting
     */
    public void setConfigurationUri(final String configurationUri)
        throws Exception
    {
        final Configuration configuration =
            Configuration.getInstance(this.replaceProperties(ResourceUtils.getContents(new URL(configurationUri))));
        configuration.addMappingsSearchLocation(this.mappingsSearchLocation);
        andromda = AndroMDA.getInstance(configuration);
    }

    /**
     * The maven jelly context.
     */
    private MavenJellyContext context;

    /**
     * Sets the maven jelly context for this instance.
     */
    public void setContext(MavenJellyContext context)
    {
        this.context = context;
    }

    /**
     * Stores the location from which to search for mappings.
     */
    private String mappingsSearchLocation;

    /**
     * Sets the mappings search location (this is the location where AndroMDA loads
     * mapping files).
     *
     * @param mappingsSearchLocation the mappings search location.
     */
    public void setMappingsSearchLocation(final String mappingsSearchLocation)
    {
        this.mappingsSearchLocation = mappingsSearchLocation;
    }

    /**
     * Runs AndroMDA with the supplied
     * configuration file.
     */
    public void run()
    {
        if (andromda != null)
        {
            andromda.run();
        }
    }

    /**
     * Gets all property names.
     *
     * @return the property names.
     */
    private String[] getPropertyNames()
    {
        final Collection properties = new ArrayList();
        for (JellyContext context = this.context; context != null; context = context.getParent())
        {
            CollectionUtils.addAll(
                properties,
                context.getVariableNames());
        }
        return (String[])properties.toArray(new String[0]);
    }

    /**
     * Replaces all properties having the style
     * <code>${some.property}</code> with the value
     * of the specified property if there is one.
     *
     * @param fileContents the fileContents to perform replacement on.
     */
    protected String replaceProperties(String string)
        throws Exception
    {
        final String[] propertyNames = this.getPropertyNames();
        if (propertyNames != null && propertyNames.length > 0)
        {
            final int propertyNameNumber = propertyNames.length;
            for (int ctr = 0; ctr < propertyNameNumber; ctr++)
            {
                final String name = propertyNames[ctr];
                final String property = "${" + name + "}";
                final Object object = this.context.findVariable(name);
                String value = null;
                if (object instanceof Expression)
                {
                    value = ((Expression)object).getExpressionText();
                }
                else if (object instanceof String)
                {
                    value = (String)object;
                }
                if (value != null)
                {
                    string = StringUtils.replace(string, property, value);
                }
            }
        }
        return string;
    }
}