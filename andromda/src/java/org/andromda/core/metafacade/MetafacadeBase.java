package org.andromda.core.metafacade;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Base class for all metafacades.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 * @author Wouter Zoons
 */
public class MetafacadeBase
{
    private Object metaObject;
    protected Logger logger;
    private boolean hasBeenValidated = false;

    public MetafacadeBase(
        Object metaObject,
        String context)
    {
        this.metaObject = metaObject;
        this.context = context;
    }

    // ---------------- essential overrides -----------------------

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof MetafacadeBase)
        {
            MetafacadeBase that = (MetafacadeBase)obj;
            return this.metaObject.equals(that.metaObject);
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return metaObject.hashCode();
    }

    /**
     * Validates that this facade's meta object is in a valid state.
     * <p>
     * Classes that extend this base class may choose the override this method
     * to check whether it is in a valid state.
     * <p>
     * In the lifecycle of a model element facade it is validated only once.
     * 
     * @param validationMessages any messages generated during validation.
     */
    public final void validate(Collection validationMessages)
    {
        if (!this.hasBeenValidated)
        {
            this.hasBeenValidated = true;
            this.validateInvariants(validationMessages);
        }
    }

    /**
     * <p>
     * The logic of modeled OCL invariants from derived metafacades will be
     * generated into this method and validation messages created and collected
     * into the <code>messages</code> collection. This method is called by
     * validate #validate().
     * <p>
     * <p>
     * By default this method is empty.
     * </p>
     */
    public void validateInvariants(Collection messages)
    {}

    /**
     * A lifecycle method, providing the ability for sub classes to take any
     * action after the factory has completely initialized a metafacade, but
     * before it has been validated for completeness.
     */
    public void initialize()
    {}

    /**
     * Returns a collection of facades for a collection of metaobjects. Contacts
     * the MetafacadeFactory to manufacture the proper facades.
     * 
     * @see MetafacadeFactory
     * @param metaobjects the objects to decorate
     * @return Collection of MetafacadeBase-derived objects
     */
    public Collection shieldedElements(Collection metaobjects)
    {
        if (metaobjects == null)
        {
            return null; // a facaded null is still a null! :-)
        }

        Collection metafacades = MetafacadeFactory.getInstance()
            .createMetafacades(metaobjects, this.getContext());

        return metafacades;
    }

    /**
     * Stores the context for this metafacade
     */
    private String context = null;

    /**
     * Gets the context for this metafacade.
     * 
     * @return the context name.
     */
    String getContext()
    {
        return this.context;
    }

    /**
     * Sets the <code>context<code> for this metafacade.
     * This is used by the MetafacadeFactory to set the context 
     * during metafacade creation when a metafacade represents 
     * a <code>contextRoot</code>.
     * @param context the context class to set
     * @see MetafacadeMapping#isContextRoot()
     * @see MetafacadeFactory#internalCreateMetafacade(Object, String, Class)
     */
    void setContext(String context)
    {
        this.context = StringUtils.trimToEmpty(context);
    }

    /**
     * Stores the property context for this metafacade.
     */
    private String propertyNamespace = null;

    /**
     * Gets the current property context for this metafacade. This is the
     * context in which properties for this metafacade are stored.
     * 
     * @return the property namespace name
     */
    String getPropertyNamespace()
    {
        if (StringUtils.isEmpty(this.propertyNamespace))
        {
            this.propertyNamespace = this.constructPropertyNamespace(this
                .getContext());
        }
        return this.propertyNamespace;
    }

    /**
     * Constructs a property namespace name with the <code>context</code>
     * argument and the current <code>namespace</code> of this metafacade.
     * 
     * @param context the context name with which to construct the name.
     * @return the new property namespace name
     */
    private String constructPropertyNamespace(String context)
    {
        StringBuffer propertyNamespace = new StringBuffer();
        propertyNamespace.append(this.getNamespace());
        propertyNamespace.append(":");
        propertyNamespace.append(context);
        return propertyNamespace.toString();
    }

    /**
     * Stores the namespace for this metafacade
     */
    private String namespace = null;

    /**
     * Gets the current namespace for this metafacade
     * 
     * @return String
     */
    String getNamespace()
    {
        return this.namespace;
    }

    /**
     * Sets the namespace for this metafacade.
     * 
     * @param namespace
     */
    void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    /**
     * Returns true or false depending on whether the <code>property</code> is
     * registered or not.
     * 
     * @param property the name of the property to check.
     * @return true/false on whether or not its regisgterd.
     */
    protected boolean isConfiguredProperty(String property)
    {
        return MetafacadeFactory.getInstance().isPropertyRegistered(
            this.getPropertyNamespace(),
            property);
    }

    /**
     * Gets a configured property from the container. Note that the configured
     * property must be registered first.
     * 
     * @param property the property name
     * @return Object the configured property instance (mappings, etc)
     */
    protected Object getConfiguredProperty(String property)
    {
        return MetafacadeFactory.getInstance().getRegisteredProperty(
            this.getPropertyNamespace(),
            property);
    }

    /**
     * Returns one facade for a particular metaobject. Contacts the
     * MetafacadeFactory to manufacture the proper facade.
     * 
     * @see MetafacadeFactory
     * @param metaObject the object to decorate
     * @return MetafacadeBase the facade
     */
    public MetafacadeBase shieldedElement(Object metaObject)
    {
        if (metaObject instanceof MetafacadeBase)
        {
            return (MetafacadeBase)metaObject;
        }
        MetafacadeBase metafacade = null;
        if (metaObject != null)
        {
            metafacade = MetafacadeFactory.getInstance().createMetafacade(
                metaObject,
                this.getContext());
        }
        return metafacade;
    }

    /**
     * Attempts to set the property with <code>name</code> having the
     * specified <code>value</code> on this metafacade.
     */
    protected void setProperty(String name, Object value)
    {
        MetafacadeFactory.getInstance().registerProperty(
            this.getPropertyNamespace(),
            name,
            value);
    }

    /**
     * Package-local setter, called by facade factory. Sets the logger to use
     * inside the facade's code.
     * 
     * @param l the logger to set
     */
    void setLogger(Logger l)
    {
        logger = l;
    }
}