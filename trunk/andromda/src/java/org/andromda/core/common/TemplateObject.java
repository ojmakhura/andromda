package org.andromda.core.common;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;


/**
 * Contains the configuration of a template object which are objects that are made available to the cartridge
 * templates.
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
        if (StringUtils.isEmpty(name))
        {
            throw new TemplateObjectException(methodName + " - templateObject '" + this + "' has no name defined");
        }
        return name;
    }

    /**
     * Caches the template objects.
     */
    private final Map objectCache = new HashMap();

    /**
     * Returns the TemplateObject instance.
     *
     * @return TemplateObject
     */
    public Object getTemplateObject()
    {
        final String methodName = "TemplateObject.getTemplateObject";
        if (StringUtils.isEmpty(name))
        {
            throw new TemplateObjectException(methodName + " - templateObject '" + this + "' has no className defined");
        }
        Object templateObject = this.objectCache.get(className);
        try
        {
            if (templateObject == null)
            {
                final Class templateObjectClass = ClassUtils.loadClass(className);
                templateObject = templateObjectClass.newInstance();
                this.setProperties(templateObject);
                this.objectCache.put(className, templateObject);
            }
        }
        catch (Exception ex)
        {
            String errMsg = "Error performing " + methodName;
            throw new TemplateObjectException(errMsg, ex);
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
        for (final Iterator referenceIterator = this.propertyReferences.iterator(); referenceIterator.hasNext();)
        {
            final String reference = (String)referenceIterator.next();
            final Property property = Namespaces.instance().findNamespaceProperty(
                    this.getNamespace(),
                    reference);
            if (!property.isIgnore())
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug(
                        "setting property '" + name + "' with value '" + property.getValue() + "' on templateObject '" +
                        templateObject + "'");
                }
                try
                {
                    Introspector.instance().setProperty(
                        templateObject,
                        reference,
                        property.getValue());
                }
                catch (Exception ex)
                {
                    String errMsg =
                        "Error setting property '" + reference + "' with '" + property.getValue() +
                        "' on templateObject --> '" + templateObject + "'";
                    logger.warn(errMsg, ex);

                    // don't throw the exception
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
     * @param className
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
     * @param reference
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
     * @return URL
     */
    public URL getResource()
    {
        return resource;
    }

    /**
     * Sets the resource in which the templateObject was found.
     *
     * @param resource
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
     * @return Returns the namespace.
     */
    public String getNamespace()
    {
        return namespace;
    }

    /**
     * @param namespace The namespace to set.
     */
    public void setNamespace(final String namespace)
    {
        this.namespace = StringUtils.trimToEmpty(namespace);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}