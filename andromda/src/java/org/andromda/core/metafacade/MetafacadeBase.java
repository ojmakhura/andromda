package org.andromda.core.metafacade;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Collection;

/**
 * Base class for all metaclass facades.
 */
public class MetafacadeBase
{
    private   Object metaObject;
    protected Logger logger;

    public MetafacadeBase(Object metaObject, String context)
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
     *
     * @throws ModelValidationException
     */
    public void validate() throws ModelValidationException
    {
    }

    /**
     * Returns a collection of facades for a collection
     * of metaobjects. Contacts the MetafacadeFactory to manufacture
     * the proper facades.
     * @see MetafacadeFactory
     *
     * @param metaobjects the objects to decorate
     * @return Collection of MetafacadeBase-derived objects
     */
    public Collection shieldedElements(Collection metaobjects)
    {
        if (metaobjects == null)
        {
            return null;   // a decorated null is still a null! :-)
        }
		Collection metafacades = MetafacadeFactory.getInstance().createMetafacades(
				metaobjects,
				this.getContext());
        if (StringUtils.isNotEmpty(this.context)) {
    		class MetafacadeContextTransformer implements Transformer {
    			public Object transform(Object object) {
    				MetafacadeBase metafacade = (MetafacadeBase)object;
    				// keep passing the context along from the
    				// very first one (i.e. the first metafacade)
                    
				    metafacade.setContext(getContext());
    				if (logger.isDebugEnabled())
    					logger.debug("set context as --> '"
    						+ metafacade.getContext()
    						+ "'");
                    
    				return metafacade;
    			}
    		}
    		CollectionUtils.transform(
    			metafacades,
    			new MetafacadeContextTransformer());
        }
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
	String getContext() {
		return this.context;
	}

	/**
	 * Sets the <code>context<code> for this metafacade
	 *
	 * @param context the context class to set
	 */
	private void setContext(String context) {
		this.context = StringUtils.trimToEmpty(context);
	}

    /**
     * Stores the property context for this Metafacade
     */
    private String propertyNamespace = null;

    /**
     * Gets the current property context for this metafacade.
     * This is the context in which properties for this metafacade
     * are stored.
     *
     * @return String
     */
    protected String getPropertyNamespace() {
    	if (StringUtils.isEmpty(this.propertyNamespace)) {
    		this.propertyNamespace = this.getContext() + ":property";
    	}
    	return this.propertyNamespace;
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
	String getNamespace() {
		return this.namespace;
	}

	/**
	 * Sets the namespace for this metafacade.
	 *
	 * @param namespace
	 */
	void setNamespace(String namespace) {
		this.namespace = namespace;
	}

    /**
     * Gets a configured property from the container.  Note
     * that the configured property must be registered first.
     *
     * @param property the property name
     * @return Object the configured property instance (mappings, etc)
     */
    protected Object getConfiguredProperty(String property) {
        return MetafacadeFactory.getInstance().getRegisteredProperty(
                this.getPropertyNamespace(),
                property);
    }

    /**
     * Registers a configured property with the container.
     *
     * @param property the name of the property.
     * @param value the value of the configured instance.
     */
    protected void registerConfiguredProperty(String property, Object value) {
        MetafacadeFactory.getInstance().registerProperty(
            this.getPropertyNamespace(),
            property,
            value);
    }

    /**
     * Returns one facade for a particular metaobject. Contacts
     * the MetafacadeFactory to manufacture the proper facade.
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
		if (metaObject != null) {
			metafacade =
				MetafacadeFactory.getInstance().createMetafacade(
					metaObject,
					this.getContext());
			// keep passing the context along from the
			// very first one (i.e. the first metafacade)
			if (StringUtils.isNotEmpty(this.context)) {
				metafacade.setContext(this.getContext());
				if (logger.isDebugEnabled())
					logger.debug("set context as --> '"
						+ metafacade.getContext()
						+ "'");
			}
		}
		return metafacade;
    }
    
    /**
     * Attempts to set the property with <code>name</code>
     * having the specified <code>value</code>
     * on this metafacade.
     */
    protected void setProperty(String name, Object value) {
        final String methodName = "MetafacadeBase.setProperty";
        ExceptionUtils.checkEmpty(methodName, "name", name);

        try
        {
            if (PropertyUtils.isWriteable(this, name)) {
                BeanUtils.setProperty(
                        this,
                        name,
                        value);
             }
        }
        catch (Exception ex)
        {
            String errMsg =
                "Error setting property '"
                    + name
                    + "' with value '" 
                    + value 
                    + "' on metafacade --> '"
                    + this
                    + "'";
            this.logger.error(errMsg, ex);
            //don't throw the exception
        }
    }

    /**
     * Package-local setter, called by facade factory.
     * Sets the logger to use inside the facade's code.
     * @param l the logger to set
     */
    void setLogger(Logger l)
    {
        logger = l;
    }
 
    /**
     * This method handles a validation error.
     * <p>
     * From an error cannot be recovered, if the user chooses to continue working with
     * the result he will most probably experience undefined behavior.
     *
     * @param error The error message
     */
    protected void validationError(String error)
    {
        System.out.println("[error] " + metaObject + " : " + error);
    }

    /**
     * This method handles a validation warning.
     * <p>
     * A warning denotes an issue that can be corrected by the facade itself, although
     * it is be advisable the user corrects this issue because of a more pleasing end-result.
     *
     * @param warning The warning message
     */
    protected void validationWarning(String warning)
    {
        System.out.println("[warning] " + metaObject + " : " + warning);
    }

}
