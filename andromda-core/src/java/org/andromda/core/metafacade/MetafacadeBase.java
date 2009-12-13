package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Base class for all metafacades.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 * @author Wouter Zoons
 * @author Bob Fields
 */
public class MetafacadeBase implements Comparable
{
    /**
     * The meta object which this metafacade wraps.
     */
    private Object metaObject;

    /**
     * Constructs a new instance of this class with the given <code>metaObject</code>
     * and <code>context</code>.  The metaObject is the meta model element which
     * this metafacade insulates. The <code>context</code> is the name of the
     * context for this metafacade instance.
     *
     * @param metaObjectIn the meta object.
     * @param contextIn the context of this meta object.
     */
    public MetafacadeBase(
        final Object metaObjectIn,
        final String contextIn)
    {
        this.metaObject = metaObjectIn;
        this.context = contextIn;
    }

    /**
     * Retrieves the <code>owner</code> of this metafacade (for example: an operation owns its parameters, a class owns
     * its attributes).
     * <p/>
     * By default <code>null</code> is returned, however this method is overridden by subclasses which have a
     * <code>parent</code> or <code>owner</code>. This is used to give the model validation messages more context as to
     * where the validation error occurred. </p>
     *
     * @return the owner of this metafacade.
     */
    public Object getValidationOwner()
    {
        return null;
    }

    /**
     * Retrieves the <code>name</code> of this metafacade used within the validation messages.
     * <p/>
     * By default <code>null</code> is returned, however this method is overridden by subclasses model elements that do
     * have a name. </p>
     *
     * @return the owner of this metafacade.
     */
    public String getValidationName()
    {
        return null;
    }

    /**
     * Stores whether or not this metafacade has
     * been initialized.
     */
    private boolean initialized;

    /**
     * Sets the flag indicating this metafacade has been initialized.
     */
    final void setInitialized()
    {
        this.initialized = true;
    }

    /**
     * Indicates if this metafacade has been initialized.
     *
     * @return true/false
     */
    final boolean isInitialized()
    {
        return this.initialized;
    }

    /**
     * Validates that this facade's meta object is in a valid state.
     * <p/>
     * Validate is called during metafacade creation by the factory. In the lifecycle of a metafacade it is validated
     * only once, this is enforced by the caching within the metafacade factory.
     *
     * @param validationMessages any messages generated during validation.
     */
    public final void validate(final Collection<ModelValidationMessage> validationMessages)
    {
        this.validateInvariants(validationMessages);
    }

    /**
     * <p/>
     * The logic of modeled OCL invariants from derived metafacades will be generated into this method and validation
     * messages created and collected into the <code>messages</code> collection. This method is called by {@link #validate(Collection validationMessages)}
     * <p/>
     * By default this method is empty. </p>
     * @param messages Collection of org.andromda.core.metafacade.ModelValidationMessage
     */
    public void validateInvariants(final Collection<ModelValidationMessage> messages)
    {
        // By default this does nothing
    }

    /**
     * A lifecycle method, providing the ability for sub classes to take any action after the factory has completely
     * initialized a metafacade, but before it has been validated for completeness.
     */
    public void initialize()
    {
        // By default this does nothing
    }

    /**
     * Returns one facade for a particular metaObject. Contacts the MetafacadeFactory to manufacture the proper
     * metafacade. In certain cases <code>metaObject</code> can also be a metafacade instance; in that case the actual
     * meta model element is retrieved from the metafacade and a metafacade is constructed from that.
     *
     * @param metaObjectIn the underlying meta model element. A metafacade is created for each.
     * @return MetafacadeBase the facade
     * @see MetafacadeFactory
     */
    protected MetafacadeBase shieldedElement(final Object metaObjectIn)
    {
        MetafacadeBase metafacade = null;
        if (metaObjectIn != null)
        {
            final String contextIn = this.getContext();
            metafacade = MetafacadeFactory.getInstance().createMetafacade(
                    metaObjectIn,
                    contextIn);

            // - The metafacade we've just got may have been found in the cache. 
            //   If so, it can have an arbitrary context (because it's cached).
            //   We now need to set the context once again, so that all
            //   other metafacade mappings based on the context work as expected.
            if(metafacade != null) 
            {
                metafacade.resetMetafacadeContext(contextIn);
            }
        }
        return metafacade;
    }

    /**
     * Returns a collection of facades for a collection of metaobjects. Contacts the MetafacadeFactory to manufacture
     * the proper facades.
     *
     * @param metaobjects the objects to decorate
     * @return Collection of MetafacadeBase-derived objects
     * @see MetafacadeFactory
     */
    protected Collection shieldedElements(final Collection metaobjects)
    {
        final List metafacades = new ArrayList();
        if (metaobjects != null)
        {
            metafacades.addAll(metaobjects);
            for (final ListIterator iterator = metafacades.listIterator(); iterator.hasNext();)
            {
                iterator.set(this.shieldedElement(iterator.next()));
            }
        }
        return metafacades;
    }

    /**
     * Stores the context for this metafacade
     */
    private String context;

    /**
     * Gets the context for this metafacade.
     *
     * @return the context name.
     */
    final String getContext()
    {
        String contextIn = this.context;
        if (StringUtils.isBlank(contextIn))
        {
            contextIn = this.getName();
        }
        return contextIn;
    }

    /**
     * Sets the context for this metafacade. This is used to pass the context along from a metafacade specializing this
     * metafacade (since we use delegate inheritance between shared and non-shared metafacades), as well as to pass the
     * context to a metafacade being created within another.
     *
     * @param contextIn the metafacade interface name representing the context.
     * @see MetafacadeMapping#isContextRoot()
     */
    public void setMetafacadeContext(final String contextIn)
    {
        this.context = contextIn;
    }

    /**
     * Resets the metafacade context after the metafacade was retrieved from the metafacade cache.
     * DO NOT CALL THIS METHOD BY HAND, it is reserved for use in the MetafacadeFactory.
     * @see org.andromda.core.metafacade.MetafacadeFactory 
     * @param contextIn the context defined by MetafacadeFactory
     */
    public void resetMetafacadeContext(String contextIn)
    {
        throw new IllegalStateException("Method resetMetafacadeContext() must be overridden by concrete metafacade class (" + this.getClass().getName() + ")! Please re-generate your metafacades using the new andromda-meta cartridge.");
    }

    /**
     * Stores the namespace for this metafacade
     */
    private String namespace;

    /**
     * Gets the current namespace for this metafacade
     *
     * @return String
     */
    final String getNamespace()
    {
        return this.namespace;
    }

    /**
     * Sets the namespace for this metafacade.
     *
     * @param namespaceIn
     */
    final void setNamespace(final String namespaceIn)
    {
        this.namespace = namespaceIn;
    }

    /**
     * Returns true or false depending on whether the <code>property</code> is registered or not.
     *
     * @param property the name of the property to check.
     * @return true/false on whether or not its registered.
     */
    protected boolean isConfiguredProperty(final String property)
    {
        return MetafacadeFactory.getInstance().isPropertyRegistered(
            this,
            property);
    }

    /**
     * Gets a configured property from the container. Note that the configured property must be registered first.
     *
     * @param property the property name
     * @return Object the configured property instance (mappings, etc)
     */
    protected Object getConfiguredProperty(final String property)
    {
        return MetafacadeFactory.getInstance().getRegisteredProperty(
            this,
            property);
    }

    /**
     * Attempts to set the property with <code>name</code> having the specified <code>value</code> on this metafacade.
     * @param nameIn 
     * @param value 
     */
    protected void setProperty(
        final String nameIn,
        final Object value)
    {
        MetafacadeFactory.getInstance().registerProperty(
            this.getName(),
            nameIn,
            value);
    }

    /**
     * Gets the current meta model object for this metafacade. This is used from {@link MetafacadeFactory} when
     * attempting to construct a metafacade from a metafacade. This allows us to get the meta object for this metafacade
     * so that the meta object can be used instead.
     *
     * @return the underlying model's meta object instance.
     */
    public final Object getMetaObject()
    {
        return this.metaObject;
    }

    /**
     * The metafacade logger instance.
     */
    protected Logger logger;

    /**
     * Package-local setter, called by facade factory. Sets the logger to use inside the facade's code.
     *
     * @param loggerIn the logger to set
     */
    final void setLogger(final Logger loggerIn)
    {
        this.logger = loggerIn;
    }

    /**
     * The flag indicating whether or not this metafacade is a context root.
     */
    protected boolean contextRoot;

    /**
     * Sets whether or not this metafacade represents a contextRoot. If it does represent a context root, then {@link
     * #getMetafacadeContext()}returns the metafacade interface for this metafacade, otherwise the regular
     * <code>context</code> is returned.
     *
     * @param contextRootIn
     */
    final void setContextRoot(final boolean contextRootIn)
    {
        this.contextRoot = contextRootIn;
    }

    /**
     * Gets the <code>context</code> for this metafacade. This is either the <code>contextRoot</code> (if one exists),
     * or the regular <code>context</code>.
     *
     * @return the metafacade's context.
     */
    public String getMetafacadeContext()
    {
        String metafacadeContext = this.getContext();
        if (this.contextRoot)
        {
            metafacadeContext = this.getName();
        }
        return metafacadeContext;
    }

    /**
     * Stores the name of the interface for this metafacade
     */
    private String name;

    /**
     * Gets the name for this metafacade.
     *
     * @return the metafacade's name.
     */
    final String getName()
    {
        if (this.name == null)
        {
            this.name = MetafacadeImpls.instance().getMetafacadeClass(this.getClass().getName()).getName();
        }
        return this.name;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object object)
    {
        boolean equals = false;
        if (object instanceof MetafacadeBase)
        {
            MetafacadeBase that = (MetafacadeBase)object;
            equals = this.metaObject.equals(that.metaObject);
        }
        return equals;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return this.metaObject.hashCode();
    }

    /**
     * In order to speed up the check for this property (which will happen many times), we cache it :-)
     */
    private Boolean metafacadePropertyCachingEnabled;

    /**
     * A check to verify whether or not to make use of metafacade property caching. This method check if the {@link
     * MetafacadeProperties#ENABLE_METAFACADE_PROPERTY_CACHING} namespace property has been set, if this is not the case
     * then the caching will be enabled by default.
     * @return this.metafacadePropertyCachingEnabled.booleanValue()
     */
    public final boolean isMetafacadePropertyCachingEnabled()
    {
        if (this.metafacadePropertyCachingEnabled == null)
        {
            final String enableCache =
                (String)this.getConfiguredProperty(MetafacadeProperties.ENABLE_METAFACADE_PROPERTY_CACHING);
            this.metafacadePropertyCachingEnabled = Boolean.valueOf(enableCache);
        }
        return this.metafacadePropertyCachingEnabled;
    }
    
    /**
     * The instance of this class as the appropriate metafacade instance.
     */
    private MetafacadeBase THIS;

    /**
     * The metafacade instance of <code>this</code>.  This should be used when
     * you'd need to check if <code>this</code> was an instance of a given metafacade.
     * For example: <code>THIS() instanceof SomeMetafacade</code>.
     *
     * This <strong>MUST</strong> be used instead of <em>this<em> in order to access the correct
     * metafacade instance in the hierarchy (since we use delegate inheritance).
     * @return this.shieldedElement(this.metaObject)
     */
    protected final MetafacadeBase THIS()
    {
        return this.THIS == null ? this.THIS = this.shieldedElement(this.metaObject) : this.THIS;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return super.toString() + '[' + this.name + ": " + this.metaObject.getClass().getName() + ']';
    }

    /**
     * Allow sorting and use in TreeSet. ValidationName is overridden in descendants.
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(Object object)
    {
        if (object==null || !(object instanceof MetafacadeBase))
        {
            return -1;
        }
        return ((MetafacadeBase)object).getValidationName().compareTo(this.getValidationName());
    }
}