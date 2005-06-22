package org.andromda.core.common;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.configuration.Namespaces;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;


/**
 * Contains the configuration of a template object which are objects that are
 * made available to the cartridge templates.
 *
 * @author Chad Brandon
 */
public class TemplateObject
{
    private static Logger logger = Logger.getLogger(TemplateObject.class);

    /**
     * The name of this template object made available to the
     * template.
     */
    private String name;

    /**
     * Gets the current name of this TemplateObject.
     *
     * @return String
     */
    public String getName()
    {
        final String methodName = "TemplateObject.getName";
        if (StringUtils.isEmpty(this.name))
        {
            throw new TemplateObjectException(methodName + " - templateObject '" + this + "' has no name defined");
        }
        return this.name;
    }

    /**
     * Caches the template objects.
     */
    private final Map objectCache = new HashMap();

    /**
     * Returns the actuall object instance described by this
     * template object.
     *
     * @return the actual object instance.
     */
    public Object getObject()
    {
        final String methodName = "TemplateObject.getTemplateObject";
        if (StringUtils.isEmpty(this.className))
        {
            throw new TemplateObjectException(methodName + " - templateObject '" + this + "' has no className defined");
        }
        Object templateObject = this.objectCache.get(this.className);
        try
        {
            if (templateObject == null)
            {
                final Class templateObjectClass = ClassUtils.loadClass(this.className);
                templateObject = templateObjectClass.newInstance();
                this.objectCache.put(this.className, templateObject);
            }

            // - we want to set the properties each time we retrieve (in case they've changed)
            this.setProperties(templateObject);
        }
        catch (final Throwable throwable)
        {
            throw new TemplateObjectException(throwable);
        }
        return templateObject;
    }

    /**
     * Sets all the nested properties on the templateObject object.
     *
     * @param templateObject
     */
    protected void setProperties(final Object templateObject)
    {
        for (final Iterator iterator = propertyReferences.iterator(); iterator.hasNext();)
        {
            final String reference = (String)iterator.next();
            String value = Namespaces.instance().getPropertyValue(
                    this.getNamespace(),
                    reference);
            if (value != null)
            {
                if (this.getLogger().isDebugEnabled())
                {
                    this.getLogger().debug(
                        "populating template object '" + this.name + "' property '" + reference + "' with value '" +
                        value + "' for namespace '" + namespace + "'");
                }
                try
                {
                    Introspector.instance().setProperty(templateObject, reference, value);
                }
                catch (final Exception exception)
                {
                    // - don't throw the exception
                    final String message =
                        "Error setting property '" + reference + "' with '" + value + "' on templateObject --> '" +
                        templateObject + "'";
                    logger.warn(message);
                }
            }
        }
    }

    /**
     * Sets the name of the template object (this name will be what the template class is stored under in the template)
     *
     * @param name the name of the template object.
     */
    public void setName(final String name)
    {
        this.name = StringUtils.trimToEmpty(name);
    }

    /**
     * The name of the class for this template object.
     */
    private String className;

    /**
     * Sets the class of the transformation object.
     *
     * @param className the name of the template object class.
     */
    public void setClassName(final String className)
    {
        final String methodName = "TemplateObject.setTransformationClass";
        ExceptionUtils.checkEmpty(methodName, "className", className);
        this.className = className;
    }

    /**
     * The property references that configure this template object.
     */
    private final Collection propertyReferences = new ArrayList();

    /**
     * Adds a templateObject property reference (used to customize templateObjects). Property references are used to
     * populate bean like properties of template objects.
     *
     * @param reference the name of the property reference.
     * @param defaultValue the default value of the property reference.
     */
    public void addPropertyReference(final String reference)
    {
        this.propertyReferences.add(reference);
    }

    /**
     * The resource in which the template object was found.
     */
    private URL resource;

    /**
     * The resource in which the templateObject was found.
     *
     * @return the resource as a URL.
     */
    public URL getResource()
    {
        return resource;
    }

    /**
     * Sets the resource in which the templateObject was defined.
     *
     * @param resource the resource on which this template object was defined.
     */
    public void setResource(final URL resource)
    {
        this.resource = resource;
    }

    /**
     * The namespace to which this template object belongs.
     */
    private String namespace;

    /**
     * Gets the namespace to which this template object belongs.
     *
     * @return Returns the namespace.
     */
    public String getNamespace()
    {
        return namespace;
    }

    /**
     * Sets the namespace to which this template object belongs.
     *
     * @param namespace The namespace to set.
     */
    public void setNamespace(final String namespace)
    {
        this.namespace = StringUtils.trimToEmpty(namespace);
    }

    /**
     * Gets the namespace logger (the logger under which output for this
     * template object should be written).
     *
     * @return the logger instance.
     */
    protected Logger getLogger()
    {
        return AndroMDALogger.getNamespaceLogger(this.namespace);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}