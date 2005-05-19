package org.andromda.core.cartridge;

import org.andromda.core.configuration.NamespaceProperties;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.text.MessageFormat;

/**
 * <p/>
 * This class implements the <code>&lt;resource&gt;</code> tag in a cartridge descriptor file and represents the base
 * cartridge resource element. </p>
 *
 * @author Chad Brandon
 */
public class Resource
{
    /**
     * Store the path to a cartridge resource.
     */
    private String path;

    /**
     * Stores whether or not the resource should be overwritten.
     */
    private boolean overwrite = false;

    /**
     * Stores whether or not the resource is required.
     */
    private boolean required = true;

    /**
     * Stores the output location logical name.
     */
    private String outlet;

    /**
     * Stores the cartridge that owns this resource.
     */
    private Cartridge cartridge;

    /**
     * Stores the output pattern for which the resource(s) should be written.
     */
    private String outputPattern;

    /**
     * Gets the logical location to which output from this resource will be written.
     *
     * @return Returns the outlet.
     */
    public String getOutlet()
    {
        return outlet;
    }

    /**
     * Sets the logical location to which output from this resource will be written.
     *
     * @param outlet The outlet to set.
     */
    public void setOutlet(final String outlet)
    {
        this.outlet = outlet;
    }

    /**
     * Returns the fully qualified name of the resource output to be written, this means: <ul> <li>the output pattern
     * has been translated</li> <li>the output directory name has been prepended</li> </ul>
     *
     * @param argument  any arguments to be inserted into the MessageFormat style messages.
     * @param directory the directory to which output will be written.
     * @return File absolute directory.
     */
    public File getOutputLocation(final Object[] arguments, final File directory)
    {
        File file = null;
        // clean any whitespace off the arguments
        if (directory != null && arguments != null && arguments.length > 0)
        {
            for (int ctr = 0; ctr < arguments.length; ctr++)
            {
                arguments[ctr] = StringUtils.trimToEmpty(String.valueOf(arguments[ctr]));
            }
            String outputFileName = MessageFormat.format(this.getOutputPattern(), arguments);

            file = new File(directory, outputFileName);
        }
        return file;
    }

    /**
     * Tells us whether output files produced by this resource should be overwritten if they already exist. Overwriting
     * can be turned on and off for entire cartridges by setting the <code>overwrite</code> property in a namespace.
     * This is useful for cartridge developers when they always want produced resources to be overwritten at first.
     *
     * @return Returns the overwrite.
     */
    public boolean isOverwrite()
    {
        Property property = Namespaces.instance().findNamespaceProperty(
            this.getCartridge().getName(), 
            NamespaceProperties.OVERWRITE,
            false);
        if (property != null)
        {
            this.overwrite = Boolean.valueOf(property.getValue()).booleanValue();
        }
        return this.overwrite;
    }

    /**
     * Sets whether output files produced by this resource should be overwritten if they already exist.
     *
     * @param overwrite The overwrite to set.
     */
    public void setOverwrite(final boolean overwrite)
    {
        this.overwrite = overwrite;
    }

    /**
     * Gets whether or not this template is required, so AndroMDA will NOT warn if a outlet is <code>not</code> defined
     * in a namespace. Otherwise AndroMDA always warns if the resource outlet isn't defined. By default resources are
     * required.
     *
     * @return Returns the required.
     */
    public boolean isRequired()
    {
        return required;
    }

    /**
     * Sets whether or not this template is required.
     *
     * @param required The required to set.
     */
    public void setRequired(final boolean optional)
    {
        this.required = optional;
    }

    /**
     * Gets the path to the cartridge resource.
     *
     * @return Returns the path.
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Sets the path to the cartridge resource.
     *
     * @param path The path to set.
     */
    public void setPath(final String path)
    {
        this.path = path;
    }

    /**
     * The cartridge that owns this template configuration.
     *
     * @return Returns the owning cartridge.
     */
    public Cartridge getCartridge()
    {
        return cartridge;
    }

    /**
     * Sets the Cartridge parent to which this Resource belongs.
     *
     * @param cartridge the parent Cartridge to set.
     */
    public void setCartridge(final Cartridge cartridge)
    {
        this.cartridge = cartridge;
    }

    /**
     * Sets the pattern that is used to build the name of the output file.
     *
     * @param outputPattern the pattern in java.text.MessageFormat syntax
     */
    public void setOutputPattern(final String outputPattern)
    {
        this.outputPattern = outputPattern;
    }

    /**
     * Gets the pattern that is used to build the name of the output file.
     *
     * @return String the pattern in java.text.MessageFormat syntax
     */
    public String getOutputPattern()
    {
        return StringUtils.trimToEmpty(this.outputPattern);
    }
}