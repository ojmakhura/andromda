package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.Profile;
import org.apache.commons.lang.StringUtils;

/**
 * A meta facade mapping class. This class is a child of
 * {@link MetafacadeMappings}.
 * 
 * @author Chad Brandon
 */
public class MetafacadeMapping
{
    /**
     * The meta facade for which this mapping applies.
     */
    private Class metafacadeClass = null;

    /**
     * Gets the metafacadeClass for this mapping.
     * 
     * @return Returns the metafacadeClass.
     */
    public Class getMetafacadeClass()
    {
        return metafacadeClass;
    }

    /**
     * Sets the metafacadeClassName for this mapping.
     * 
     * @param metafacadeClassName The name of the metafaacde class to set.
     */
    public void setMetafacadeClassName(String metafacadeClassName)
    {
        try
        {
            this.metafacadeClass = ClassUtils.loadClass(StringUtils
                .trimToEmpty(metafacadeClassName));
        }
        catch (Throwable th)
        {
            throw new MetafacadeMappingsException(th);
        }
    }

    /**
     * The name of the mapping class for which this mapping applies. 
     * The {@link #context}, {@link #stereotypes} and this 
     * name make up the identifying key for this mapping.
     */
    private String mappingClassName = null;

    /**
     * Gets the name of the metaobject class used for this mapping.
     * 
     * @return Returns the mappingClassName.
     */
    protected String getMappingClassName()
    {
        return this.mappingClassName;
    }

    /**
     * The name of the metaobject class to use for this mapping.
     * 
     * @param mappingClassName The mappingClassName to set.
     */
    public void setMappingClassName(String mappingClassName)
    {
        this.mappingClassName = StringUtils.trimToEmpty(mappingClassName);
    }

    /**
     * Whether or not this mapping represents a <code>contextRoot</code>.
     */
    private boolean contextRoot = false;

    /**
     * <p>
     * Gets whether or not this mapping represents a <code>contextRoot</code>,
     * by default a mapping is <strong>NOT </strong> a contextRoot. You'll want
     * to specify this as true when other metafacades need to be created within the
     * context of this metafacade.
     * </p>
     * 
     * @return Returns the contextRoot.
     */
    public boolean isContextRoot()
    {
        return contextRoot;
    }

    /**
     * Sets the name of the <code>contextRoot</code> for this mapping.
     * 
     * @param contextRoot The contextRoot to set.
     * @see #isContextRoot()
     */
    public void setContextRoot(boolean contextRoot)
    {
        this.contextRoot = contextRoot;
    }

    /**
     * The stereotypes to which this mapping applies (all stereotypes must be
     * present for this mapping to apply).
     */
    private final List stereotypes = new ArrayList();

    /**
     * Adds a <code>stereotype</code> to the stereotypes.
     * 
     * @param stereotype
     */
    public void addStereotype(String stereotype)
    {
        this.stereotypes.add(Profile.instance().get(stereotype));
    }
    
    /**
     * Gets the stereotypes which apply to this mapping.
     * 
     * @return the names of the stereotypes
     */
    List getStereotypes()
    {
        return this.stereotypes;
    }
    
    /**
     * Indicates whether or not this mapping has any stereotypes
     * defined.
     * @return true/false
     */
    boolean hasStereotypes()
    {
        return !this.stereotypes.isEmpty();
    }

    /**
     * Used to hold references to language mapping classes.
     */
    private final Map propertyReferences = new HashMap();

    /**
     * Adds a mapping property reference. These are used to populate metafacade
     * impl classes with mapping files, etc. The property reference applies to
     * the given mapping.
     * 
     * @param reference the name of the reference.
     * @param defaultValue the default value of the property reference.
     * @see (MetafacadeMappings#
     */
    public void addPropertyReference(String reference, String defaultValue)
    {
        this.propertyReferences.put(reference, defaultValue);
    }

    /**
     * Returns all mapping references for this MetafacadeMapping instance.
     */
    public Map getPropertyReferences()
    {
        return this.propertyReferences;
    }

    /**
     * Used to hold the properties that should apply to the mapping element.
     */
    private final Map mappingProperties = new HashMap();

    /**
     * Adds a mapping property. This are used to narrow the metafacade to which
     * the mapping can apply. The properties must exist and must evaluate to the
     * specified value if given for the mapping to match.
     * 
     * @param reference the name of the reference.
     * @param defaultValue the default value of the property reference.
     */
    public void addMappingProperty(String name, String value)
    {
        this.mappingProperties.put(name, value);
    }

    /**
     * Returns all mapping properties for this MetafacadeMapping instance.
     */
    public Map getMappingProperties()
    {
        return this.mappingProperties;
    }

    /**
     * Adds all <code>propertyReferences</code> to the property references
     * contained in this MetafacadeMapping instance.
     * 
     * @param propertyReferences the property references to add.
     */
    public void addPropertyReferences(Map propertyReferences)
    {
        if (propertyReferences != null)
        {
            this.propertyReferences.putAll(propertyReferences);
        }
    }

    /**
     * The key used to uniquely identify this mapping.
     */
    private String key;

    /**
     * Gets the unique key that identifies this mapping
     */
    protected String getKey()
    {
        if (StringUtils.isEmpty(this.key) && this.mappingClassName != null)
        {
            key = MetafacadeMappingsUtils.constructKey(
                this.mappingClassName,
                this.context,
                this.stereotypes);
        }
        return key;
    }

    /**
     * The context to which this mapping applies.
     */
    private String context;

    /**
     * Sets the context to which this mapping applies.
     * 
     * @param context The metafacade context name to set.
     */
    public void setContext(String context)
    {
        this.context = StringUtils.trimToEmpty(context);
    }
    
    /**
     * Gets the context to which this mapping applies.
     * 
     * @return the name of the context
     */
    String getContext()
    {
        return this.context;
    }
    
    /**
     * Indicates whether or not this mapping has a context.
     * 
     * @return true/false
     */
    boolean hasContext()
    {
        return StringUtils.isNotEmpty(this.context);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return MetafacadeMappingsUtils.constructKey(super.toString(), this
            .getKey())
            + ":" + this.getMetafacadeClass();
    }
}