package org.andromda.core.metafacade;

import java.util.HashMap;
import java.util.Map;

import org.andromda.core.common.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * A meta facade mapping class. This class is a child of
 * {@link MetafacadeMappings}.
 * 
 * @author Chad Brandon
 */
public class MetafacadeMapping
{
    private static Logger logger = Logger.getLogger(MetafacadeMapping.class);

    /**
     * The meta facade for which this mapping applies.
     */
    private Class metafacadeClass = null;

    /**
     * The meta model class to for which this mapping applies. The
     * <code>stereotypes</code> and this variable make up the identifying key
     * for this mappping.
     */
    private String metaobjectClassName = null;

    /**
     * Whether or not this mapping represents a <code>contextRoot</code>.
     */
    private boolean contextRoot = false;

    /**
     * The stereotype to which this mapping applies.
     */
    private String stereotype = null;

    /**
     * Used to hold references to language mapping classes.
     */
    private Map propertyReferences = new HashMap();

    /**
     * The parent mappings instance that owns this mapping.
     */
    private MetafacadeMappings mappings;

    /**
     * The key used to uniquely identify this mapping.
     */
    private String key;

    /**
     * The context to which this mapping applies.
     */
    private String context;

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
            String errMsg = "Error performing setMetafacadeClassName";
            logger.error(errMsg, th);
            throw new MetafacadeMappingsException(errMsg, th);
        }
    }

    /**
     * Gets the name of the metaobject class used for this mapping.
     * 
     * @return Returns the metaobjectClass.
     */
    protected String getMetaobjectClassName()
    {
        return this.metaobjectClassName;
    }

    /**
     * The name of the metaobject class to use for this mapping.
     * 
     * @param metaobjectClassName The metaobjectClassName to set.
     */
    public void setMetaobjectClassName(String metaobjectClassName)
    {
        try
        {
            this.metaobjectClassName = StringUtils
                .trimToEmpty(metaobjectClassName);
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing setMetafacadeClass";
            logger.error(errMsg, th);
            throw new MetafacadeMappingsException(errMsg, th);
        }
    }

    /**
     * <p>
     * Gets whether or not this mapping represents a <code>contextRoot</code>,
     * by default a mapping is <strong>NOT </strong> a contextRoot. You may want
     * to specify one when other metafacades need to be created within the
     * context of a metafacade.
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
     * Returns <code>true</code> if this mapping has a stereotype defined,
     * <code>false</code> otherwise.
     * 
     * @return true/false
     */
    boolean hasStereotype()
    {
        return StringUtils.isNotEmpty(this.stereotype);
    }

    /**
     * Returns <code>true</code> if this mapping has a context defined,
     * <code>false</code> otherwise.
     * 
     * @return true/false
     */
    boolean hasContext()
    {
        return StringUtils.isNotEmpty(this.context);
    }

    /**
     * Adds a <code>stereotype</code> to the stereotypes for which the
     * <code>metafacadeClass</code> should be instead.
     * 
     * @param stereotype
     */
    public void setStereotype(String stereotype)
    {
        this.stereotype = StringUtils.trimToEmpty(stereotype);
    }

    /**
     * Adds a mapping reference. This are used to populate metafacade impl
     * classes with mapping files, etc. If its added here as opposed to each
     * child MetafacadeMapping, then the reference will apply to all mappings.
     * 
     * @param reference the name of the reference.
     * @param defaultValue the default value of the property reference.
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
     * Sets the MetafacadeMappings to which this MetafacadeMapping belongs.
     * 
     * @param mappings
     */
    protected void setMetafacadeMappings(MetafacadeMappings mappings)
    {
        this.mappings = mappings;
    }

    /**
     * Returns the MetafacadeMappings to which this mapping belongs.
     * 
     * @return the MetafacadeParent.
     */
    protected MetafacadeMappings getMetafacadeMappings()
    {
        return this.mappings;
    }

    /**
     * Gets the unique key that identifies this mapping
     */
    protected String getKey()
    {
        if (StringUtils.isEmpty(this.key))
        {
            if (this.hasStereotype())
            {
                key = MetafacadeMappingsUtils.constructKey(
                    this.metaobjectClassName,
                    this.stereotype);
            }
            else
            {
                key = this.metaobjectClassName;
            }
        }
        return key;
    }

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
     * <p>
     * Gets the <code>context</code> of this mapping. The <code>context</code>
     * is the context in which the <code>metafacade</code> represented by this
     * mapping will be created.
     * </p>
     */
    String getContext()
    {
        return this.context;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return MetafacadeMappingsUtils.appendContext(super.toString(), this
            .getKey());
    }
}