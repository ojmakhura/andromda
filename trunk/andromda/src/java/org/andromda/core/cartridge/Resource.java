package org.andromda.core.cartridge;

import org.andromda.core.common.Namespaces;
import org.andromda.core.common.Property;

/**
 * <p>
 * This class implements the <code>&lt;resource&gt;</code> tag in a cartridge
 * descriptor file and represents the base cartridge resoure element.
 * </p>
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
     * Gets the logical location to which output from this resource will be
     * written.
     * 
     * @return Returns the outlet.
     */
    public String getOutlet()
    {
        return outlet;
    }

    /**
     * Sets the logical location to which output from this resource will be
     * written.
     * 
     * @param outlet The outlet to set.
     */
    public void setOutlet(String outlet)
    {
        this.outlet = outlet;
    }

    /**
     * Tells us whether output files produced by this resource should be
     * overwritten if they already exist. Overwriting can be turned on and off
     * for entire cartridges by setting the <code>overwrite</code> property in
     * a namespace. This is useful for cartridge developers when they always
     * want produced resources to be overwritten at first.
     * 
     * @return Returns the overwrite.
     */
    public boolean isOverwrite()
    {
        Property property = Namespaces.instance().findNamespaceProperty(
            this.getCartridge().getName(),
            "overwrite",
            false);
        if (property != null)
        {
            this.overwrite = Boolean.valueOf(property.getValue())
                .booleanValue();
        }
        return this.overwrite;
    }

    /**
     * Sets whether output files produced by this resource should be overwritten
     * if they already exist.
     * 
     * @param overwrite The overwrite to set.
     */
    public void setOverwrite(boolean overwrite)
    {
        this.overwrite = overwrite;
    }

    /**
     * Gets whether or not this template is required, so AndroMDA will NOT warn
     * if a outlet is <code>not</code> defined in a namespace. Otherwise
     * AndroMDA always warns if the resource outlet isn't defined. By default
     * resources are required.
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
    public void setRequired(boolean optional)
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
    public void setPath(String path)
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
     * @param cartridge the parent Cartridge to set.
     */
    public void setCartridge(Cartridge cartridge)
    {
        this.cartridge = cartridge;
    }
}