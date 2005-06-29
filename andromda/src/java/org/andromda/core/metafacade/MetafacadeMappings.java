package org.andromda.core.metafacade;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Merger;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.configuration.Namespace;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.namespace.BaseNamespaceComponent;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * The Metafacade mapping class. Used to map <code>metafacade</code> objects to <code>metamodel</code> objects.
 *
 * @author Chad Brandon
 * @see MetafacadeMapping
 * @see org.andromda.core.common.XmlObjectFactory
 */
public class MetafacadeMappings
    extends BaseNamespaceComponent
{
    /**
     * Holds the references to the child MetafacadeMapping instances.
     */
    private final Collection mappings = new ArrayList();

    /**
     * Holds the namespace MetafacadeMappings. This are child MetafacadeMappings keyed by namespace name.
     */
    private final Map namespaceMetafacadeMappings = new HashMap();

    /**
     * The default meta facade to use when there isn't a mapping found.
     */
    private Class defaultMetafacadeClass = null;

    /**
     * Constructs a new instance of this class.
     *
     * @return MetafacadeMappings
     */
    public static MetafacadeMappings newInstance()
    {
        return new MetafacadeMappings();
    }

    /**
     * Returns a new configured instance of this MetafacadeMappings configured from the mappings configuration URI.
     *
     * @param mappingsUri the URI to the XML type mappings configuration file.
     * @return MetafacadeMappings the configured MetafacadeMappings instance.
     */
    protected static final MetafacadeMappings getInstance(final URL mappingsUri)
    {
        final String methodName = "MetafacadeMappings.getInstance";
        ExceptionUtils.checkNull(methodName, "mappingsUri", mappingsUri);
        final XmlObjectFactory factory = XmlObjectFactory.getInstance(MetafacadeMappings.class);
        MetafacadeMappings mappings = (MetafacadeMappings)factory.getObject(mappingsUri);

        // after we've gotten the initial instance we can merge the file
        // since we know the namespace
        String mappingsContents = ResourceUtils.getContents(mappingsUri);
        mappingsContents = Merger.instance().getMergedString(
                mappingsContents,
                mappings.getNamespace());
        mappings = (MetafacadeMappings)factory.getObject(mappingsContents);
        return mappings;
    }

    /**
     * Adds a MetafacadeMapping instance to the set of current mappings.
     *
     * @param mapping the MetafacadeMapping instance.
     */
    public void addMapping(final MetafacadeMapping mapping)
    {
        final String methodName = "MetafacadeMappings.addMapping";
        ExceptionUtils.checkNull(methodName, "mapping", mapping);
        final String mappingClassName = mapping.getMappingClassName();
        ExceptionUtils.checkEmpty(methodName, "mapping.mappingClassName", mappingClassName);
        ExceptionUtils.checkNull(
            methodName,
            "mapping.metafacadeClass",
            mapping.getMetafacadeClass());
        mapping.setMetafacadeMappings(this);

        // find any mappings that match, if they do we add the properties
        // from that mapping to the existing matched mapping (so we only
        // have one mapping containing properties that can be 'OR'ed together.
        final MetafacadeMapping foundMapping =
            this.findMapping(
                new Condition()
                {
                    public boolean evaluate(final Object object)
                    {
                        return mapping.match((MetafacadeMapping)object);
                    }
                });
        if (foundMapping != null)
        {
            foundMapping.addMappingPropertyGroup(mapping.getMappingProperties());
        }
        else
        {
            this.mappings.add(mapping);
            mappingsByMetafacadeClass.put(
                this.getMetafacadeInterface(mapping.getMetafacadeClass()),
                mapping);
        }
    }

    /**
     * Gets the class of the metafacade interface that belongs to the given <code>metafacadeClass</code>.
     *
     * @return the metafacade interface Class.
     */
    public Class getMetafacadeInterface(final Class metafacadeClass)
    {
        Class metafacadeInterface = null;
        if (metafacadeClass != null)
        {
            metafacadeInterface = metafacadeClass;
            final List interfaces = ClassUtils.getAllInterfaces(metafacadeClass);
            if (interfaces != null && !interfaces.isEmpty())
            {
                metafacadeInterface = (Class)interfaces.iterator().next();
            }
        }
        return metafacadeInterface;
    }

    /**
     * Stores mappings by the metafacade class so that we can retrieve the inherited metafacade classes.
     */
    private final Map mappingsByMetafacadeClass = new HashMap();

    /**
     * Copies all data from <code>mappings<code> to this instance.
     *
     * @param mappings the mappings to add
     */
    private final void copyMappings(final MetafacadeMappings mappings)
    {
        final String methodName = "MetafacadeMappings.copyMappings";
        ExceptionUtils.checkNull(methodName, "mappings", mappings);

        // the namespace is always the default namespace
        this.setNamespace(Namespaces.DEFAULT);

        for (final Iterator iterator = mappings.mappings.iterator(); iterator.hasNext();)
        {
            this.addMapping((MetafacadeMapping)iterator.next());
        }
        final Collection propertyReferences = mappings.getPropertyReferences();
        if (propertyReferences != null && !propertyReferences.isEmpty())
        {
            this.propertyReferences.addAll(propertyReferences);
        }
        this.defaultMetafacadeClass = mappings.defaultMetafacadeClass;
    }

    /**
     * Contains references to properties populated in the Namespaces.
     */
    private final Collection propertyReferences = new HashSet();

    /**
     * Gets all property references defined in this mappings
     * instance.
     *
     * @return the map of property references (names and values).
     */
    public Collection getPropertyReferences()
    {
        return this.propertyReferences;
    }

    /**
     * <p/> Retrieves the MetafacadeMapping belonging to the unique
     * <code>key</code> created from the <code>mappingObject</code>'s
     * class, <code>context</code> and given <code>stereotypes</code>. It's
     * <strong>IMPORTANT </strong> to note that contexts have a higher priority
     * than stereotypes. This allows us to retrieve mappings based on the
     * following combinations:
     * <ul>
     * <li>A single stereotype no context</li>
     * <li>A single stereotype with a context</li>
     * <li>metafacade properties no context</li>
     * <li>metafacade properties with a context</code>
     * <li>multiple stereotypes no context</li>
     * <li>multiple stereotypes with a context</li>
     * </ul>
     * </p>
     * <p/> NOTE: mapping properties are inherited from super metafacades.
     * </p>
     *
     * @param mappingObject an instance of the class to which the mapping
     *        applies.
     * @param stereotypes the stereotypes to check.
     * @param rootContext the context within the namespace for which the mapping
     *        applies (has 'root' in the name because of the fact that we also
     *        search the context inheritance hiearchy started with this 'root'
     *        context).
     * @return MetafacadeMapping (or null if none was found matching the
     *         criteria).
     */
    protected MetafacadeMapping getMapping(
        final Object mappingObject,
        final String context,
        final Collection stereotypes)
    {
        MetafacadeMapping mapping = this.getMapping(null, mappingObject, context, stereotypes);
        if (mapping == null)
        {
            final Collection hierarchy = this.getMappingObjectHierarchy(mappingObject);
            if (hierarchy != null && !hierarchy.isEmpty())
            {
                for (final Iterator iterator = hierarchy.iterator(); iterator.hasNext() && mapping == null;)
                {
                    mapping = this.getMapping((String)iterator.next(), mappingObject, context, stereotypes);
                }
            }
        }
        return mapping;
    }

    /**
     * The cache containing the hierachies for each mapping object so that
     * we don't need to retrieve more than once.
     */
    private final Map mappingObjectHierachyCache = new HashMap();

    /**
     * Retrieves the hiearchy of class names. of the given <code>mappingObject</code>.
     *
     * @param mappingObject the object from which to retrieve the hierarchy.
     * @return a list containing all inherited class names.
     */
    protected List getMappingObjectHierarchy(final Object mappingObject)
    {
        List hierarchy = (List)this.mappingObjectHierachyCache.get(mappingObject);
        if (hierarchy == null)
        {
            // we construct the mapping object name from the interface 
            // (using the implementation name pattern).
            final String pattern = this.getMetaclassPattern();
            if (StringUtils.isNotBlank(pattern))
            {
                hierarchy = new ArrayList();
                hierarchy.addAll(ClassUtils.getAllInterfaces(mappingObject.getClass()));
                if (hierarchy != null)
                {
                    for (final ListIterator iterator = hierarchy.listIterator(); iterator.hasNext();)
                    {
                        final String name = ((Class)iterator.next()).getName();
                        iterator.set(pattern != null ? pattern.replaceAll("\\{0\\}", name) : name);
                    }
                }
                this.mappingObjectHierachyCache.put(mappingObject, hierarchy);
            }
        }
        return hierarchy;
    }

    /**
     * <p/> Stores the mappings which are currently "in process" (within the
     * {@link #getMapping(Object, String, Collection)}. This means the mapping
     * is being processed by the {@link #getMapping(Object, String, Collection)}
     * operation. We store these "in process" mappings in order to keep track of
     * the mappings currently being evaluated so we avoid stack over flow errors
     * {@link #getMapping(Object, String, Collection)}when finding mappings
     * that are mapped to super metafacade properties.
     * </p>
     * <p/> Note: visibility is defined as <code>protected</code> in order to
     * improve inner class access performance.
     * </p>
     */
    protected final Collection inProcessMappings = new ArrayList();

    /**
     * <p/> Stores the metafacades which are currently "in process" (within the
     * {@link #getMapping(Object, String, Collection)}. This means the
     * metafacade being processed by the {@link #getMapping(Object, String,
     * Collection)}operation. We store these "in process" metafacades in order
     * to keep track of the metafacades currently being evaluated so we avoid
     * stack over flow errors {@link #getMapping(Object, String, Collection)}when
     * finding metafacades that are mapped to super metafacade properties.
     * </p>
     * <p/> Note: visibility is defined as <code>protected</code> in order to
     * improve inner class access performance.
     * </p>
     */
    protected final Collection inProcessMetafacades = new ArrayList();

    /**
     * <p/> Retrieves the MetafacadeMapping belonging to the unique
     * <code>key</code> created from the <code>mappingObject</code>'s
     * class, <code>context</code> and given <code>stereotypes</code>. It's
     * <strong>IMPORTANT </strong> to note that contexts have a higher priority
     * than stereotypes. This allows us to retrieve mappings based on the
     * following combinations:
     * <ul>
     * <li>A single stereotype no context</li>
     * <li>A single stereotype with a context</li>
     * <li>metafacade properties no context</li>
     * <li>metafacade properties with a context</li>
     * <li>multiple stereotypes no context</li>
     * <li>multiple stereotypes with a context</li>
     * </ul>
     * </p>
     * <p/> NOTE: mapping properties are inherited from super metafacades.
     * </p>
     *
     * @param mappingClassName the name of the mapping class to use instead of
     *        the actual class name taken from the <code>mappingObject</code>.
     *        If null then the class name from the <code>mappingObject</code>
     *        is used.
     * @param mappingObject an instance of the class to which the mapping
     *        applies.
     * @param stereotypes the stereotypes to check.
     * @param rootContext the context within the namespace for which the mapping
     *        applies (has 'root' in the name because of the fact that we also
     *        search the context inheritance hiearchy started with this 'root'
     *        context).
     * @return MetafacadeMapping (or null if none was found matching the
     *         criteria).
     */
    private final MetafacadeMapping getMapping(
        final String mappingClassName,
        final Object mappingObject,
        final String context,
        final Collection stereotypes)
    {
        final String metaclassName = mappingClassName != null ? mappingClassName : mappingObject.getClass().getName();

        // verfiy we can at least find the meta class, so we don't perform the rest of
        // the search for nothing
        final boolean validMetaclass =
            this.findMapping(
                new Condition()
                {
                    public boolean evaluate(final Object object)
                    {
                        return ((MetafacadeMapping)object).getMappingClassName().equals(metaclassName);
                    }
                }) != null;
        MetafacadeMapping mapping = null;
        if (validMetaclass)
        {
            final boolean emptyStereotypes = stereotypes == null || stereotypes.isEmpty();

            // - first try to find the mapping by context and stereotypes
            if (context != null && !emptyStereotypes)
            {
                mapping =
                    this.findMapping(
                        new Condition()
                        {
                            public boolean evaluate(final Object object)
                            {
                                boolean valid = false;
                                final MetafacadeMapping mapping = (MetafacadeMapping)object;
                                if (metaclassName.equals(mapping.getMappingClassName()) && mapping.hasContext() &&
                                    mapping.hasStereotypes() && !mapping.hasMappingProperties())
                                {
                                    valid =
                                        getContextHierarchy(context).contains(mapping.getContext()) &&
                                        stereotypes.containsAll(mapping.getStereotypes());
                                }
                                return valid;
                            }
                        });
            }

            // - check for context and metafacade properties
            if (mapping == null && context != null)
            {
                mapping =
                    this.findMapping(
                        new Condition()
                        {
                            public boolean evaluate(final Object object)
                            {
                                final MetafacadeMapping mapping = (MetafacadeMapping)object;
                                boolean valid = false;
                                if (metaclassName.equals(mapping.getMappingClassName()) && !mapping.hasStereotypes() &&
                                    mapping.hasContext() && mapping.hasMappingProperties() &&
                                    (inProcessMappings.isEmpty() || !inProcessMappings.contains(mapping)))
                                {
                                    if (getContextHierarchy(context).contains(mapping.getContext()))
                                    {
                                        inProcessMappings.add(mapping);
                                        final MetafacadeBase metafacade =
                                            MetafacadeFactory.getInstance().createMetafacade(mappingObject, mapping);
                                        inProcessMetafacades.add(metafacade);

                                        // reset the "in process" mappings
                                        inProcessMappings.clear();
                                        valid = MetafacadeUtils.propertiesValid(metafacade, mapping);
                                    }
                                }
                                return valid;
                            }
                        });
            }

            // - check just the context alone
            if (mapping == null && context != null)
            {
                mapping =
                    this.findMapping(
                        new Condition()
                        {
                            public boolean evaluate(final Object object)
                            {
                                boolean valid = false;
                                final MetafacadeMapping mapping = (MetafacadeMapping)object;
                                if (metaclassName.equals(mapping.getMappingClassName()) && mapping.hasContext() &&
                                    !mapping.hasStereotypes() && !mapping.hasMappingProperties())
                                {
                                    valid = getContextHierarchy(context).contains(mapping.getContext());
                                }
                                return valid;
                            }
                        });
            }

            // check only stereotypes
            if (mapping == null && !emptyStereotypes)
            {
                mapping =
                    this.findMapping(
                        new Condition()
                        {
                            public boolean evaluate(final Object object)
                            {
                                boolean valid = false;
                                final MetafacadeMapping mapping = (MetafacadeMapping)object;
                                if (metaclassName.equals(mapping.getMappingClassName()) && mapping.hasStereotypes() &&
                                    !mapping.hasContext() && !mapping.hasMappingProperties())
                                {
                                    valid = stereotypes.containsAll(mapping.getStereotypes());
                                }
                                return valid;
                            }
                        });
            }

            // - now check for metafacade properties
            if (mapping == null)
            {
                mapping =
                    this.findMapping(
                        new Condition()
                        {
                            public boolean evaluate(final Object object)
                            {
                                final MetafacadeMapping mapping = (MetafacadeMapping)object;
                                boolean valid = false;
                                if (metaclassName.equals(mapping.getMappingClassName()) && !mapping.hasStereotypes() &&
                                    !mapping.hasContext() && mapping.hasMappingProperties() &&
                                    (inProcessMappings.isEmpty() || !inProcessMappings.contains(mapping)))
                                {
                                    inProcessMappings.add(mapping);
                                    final MetafacadeBase metafacade =
                                        MetafacadeFactory.getInstance().createMetafacade(mappingObject, mapping);
                                    inProcessMetafacades.add(metafacade);

                                    // reset the "in process" mappings
                                    inProcessMappings.clear();
                                    valid = MetafacadeUtils.propertiesValid(metafacade, mapping);
                                }
                                return valid;
                            }
                        });
            }

            // - finally find the mapping with just the class
            if (mapping == null)
            {
                mapping =
                    this.findMapping(
                        new Condition()
                        {
                            public boolean evaluate(final Object object)
                            {
                                final MetafacadeMapping mapping = (MetafacadeMapping)object;
                                return metaclassName.equals(mapping.getMappingClassName()) && !mapping.hasContext() &&
                                !mapping.hasStereotypes() && !mapping.hasMappingProperties();
                            }
                        });
            }
        }

        // - if it's still null, try with the parent
        if (mapping == null && this.parent != null)
        {
            mapping = this.parent.getMapping(metaclassName, mappingObject, context, stereotypes);
        }

        // - reset the "in process" metafacades
        this.inProcessMetafacades.clear();
        return mapping;
    }

    /**
     * Finds the first mapping in the internal {@link #mappings} collection
     * that matches the given predicate.
     * @param predicate the condition
     * @return the found mapping instance
     */
    private final MetafacadeMapping findMapping(final Condition condition)
    {
        MetafacadeMapping found = null;
        for (final Iterator iterator = this.mappings.iterator(); iterator.hasNext();)
        {
            final MetafacadeMapping mapping = (MetafacadeMapping)iterator.next();
            if (condition.evaluate(mapping))
            {
                found = mapping;
                break;
            }
        }
        return found;
    }

    /**
     * Provides a means to evaluate whether or not a condition
     * is true.
     */
    static interface Condition
    {
        public boolean evaluate(final Object object);
    }

    /**
     * <p/>
     * Loads all property references into the given <code>mapping</code> inherited from any super metafacade of the
     * given mapping's metafacade. </p>
     *
     * @param mapping the MetafacadeMapping to which we'll add the inherited property references.
     * @param context the context from which the property references are inherited.
     * @return The MetafacadeMapping with all loaded property references.
     */
    private final void loadInheritedPropertyReferences(final MetafacadeMapping mapping)
    {
        if (mapping != null)
        {
            final Class[] interfaces = this.getInterfacesReversed(mapping.getMetafacadeClass().getName());
            if (interfaces != null && interfaces.length > 0)
            {
                for (int ctr = 0; ctr < interfaces.length; ctr++)
                {
                    final Class metafacadeClass = interfaces[ctr];
                    final MetafacadeMapping contextMapping =
                        (MetafacadeMapping)this.mappingsByMetafacadeClass.get(metafacadeClass);
                    if (contextMapping != null)
                    {
                        // add all property references
                        mapping.addPropertyReferences(contextMapping.getPropertyReferences());
                    }
                }
            }
        }
    }

    /**
     * The cache containing the hierachies for each context so that we don't need to retrieve more than once.
     */
    private final Map contextHierachyCache = new HashMap();

    /**
     * Retrieves all inherited contexts (including the root <code>context</code>) from the given <code>context</code>
     * and returns a list containing all of them.  Note that the visibilty of this operation is protected to improve
     * inner class access performance.
     *
     * @param context the root contexts
     * @return a list containing all inherited contexts
     */
    protected final List getContextHierarchy(final String context)
    {
        List contexts = (List)this.contextHierachyCache.get(context);
        if (contexts == null)
        {
            contexts = ClassUtils.getInterfaces(context);
            if (contexts != null)
            {
                for (final ListIterator iterator = contexts.listIterator(); iterator.hasNext();)
                {
                    iterator.set(((Class)iterator.next()).getName());
                }
            }
            this.contextHierachyCache.put(context, contexts);
        }
        return contexts;
    }

    /**
     * The cache of interfaces for the given className in reversed order.
     */
    private final Map reversedInterfaceArrayCache = new HashMap();

    /**
     * Gets the interfaces for the given <code>className</code> in reverse order.
     *
     * @param className the name of the class for which to retrieve the interfaces
     * @return the array containing the reversed interfaces.
     */
    private final Class[] getInterfacesReversed(final String className)
    {
        Class[] interfaces = (Class[])this.reversedInterfaceArrayCache.get(className);
        if (interfaces == null)
        {
            interfaces = ClassUtils.getInterfacesReversed(className);
            this.reversedInterfaceArrayCache.put(className, interfaces);
        }
        return interfaces;
    }

    /**
     * Adds a language mapping reference. This are used to populate metafacade impl classes with mapping files (such as
     * those that map from model types to Java, JDBC, SQL types, etc). If its added here as opposed to each child
     * MetafacadeMapping, then the reference will apply to all mappings.
     *
     * @param reference the name of the reference.
     */
    public void addPropertyReference(final String reference)
    {
        this.propertyReferences.add(reference);
    }

    /**
     * <p/> Attempts to get the MetafacadeMapping identified by the given
     * <code>mappingClass</code>,<code>context</code> and
     * <code>stereotypes<code>, from the mappings for the given <code>namespace</code>. If it can <strong>not</strong>
     * be found, it will search the default mappings and return that instead. </p>
     * <p/>
     * <strong>IMPORTANT:</strong> The <code>context</code> will take precedence over any <code>stereotypes</code> with
     * the mapping. </p>
     *
     * @param mappingClass the class name of the meta object for the mapping we are trying to find.
     * @param namespace the namespace (i.e. a cartridge, name, etc.)
     * @param context to which the mapping applies (note this takes precendence over stereotypes).
     * @param stereotypes collection of sterotype names.  We'll check to see if the mapping for the given
     *                    <code>mappingClass</code> is defined for it.
     */
    public MetafacadeMapping getMetafacadeMapping(
        final Object mappingObject,
        final String namespace,
        final String context,
        final Collection stereotypes)
    {
        final String methodName = "MetafacadeMappings.getMetafacadeMapping";
        if (this.getLogger().isDebugEnabled())
        {
            this.getLogger().debug(
                "performing '" + methodName + "' with mappingObject '" + mappingObject + "', stereotypes '" +
                stereotypes + "', namespace '" + namespace + "' and context '" + context + "'");
        }

        final MetafacadeMappings mappings = this.getNamespaceMappings(namespace);
        MetafacadeMapping mapping = null;

        // first try the namespace mappings
        if (mappings != null)
        {
            mapping = mappings.getMapping(mappingObject, context, stereotypes);
        }

        // if we've found a namespace mapping, try to get any shared mappings
        // that this namespace mapping may extend and copy over any property
        // references from the shared mapping to the namespace mapping.
        if (mapping != null)
        {
            final Collection propertyReferences = mapping.getPropertyReferences();
            final MetafacadeMapping defaultMapping = this.getMapping(mappingObject, context, stereotypes);
            if (defaultMapping != null)
            {
                Collection defaultPropertyReferences = defaultMapping.getPropertyReferences();
                MetafacadeImpls metafacadeClasses = MetafacadeImpls.instance();
                final Class metafacadeInterface =
                    metafacadeClasses.getMetafacadeClass(mapping.getMetafacadeClass().getName());
                final Class defaultMetafacadeInterface =
                    metafacadeClasses.getMetafacadeClass(defaultMapping.getMetafacadeClass().getName());
                if (defaultMetafacadeInterface.isAssignableFrom(metafacadeInterface))
                {
                    mapping.addPropertyReferences(defaultPropertyReferences);

                    // add the namespace property references back so
                    // that the default ones don't override the
                    // namespace specific ones.
                    mapping.addPropertyReferences(propertyReferences);
                }
            }
        }

        // if the namespace mappings weren't found, try the default
        if (mapping == null)
        {
            if (this.getLogger().isDebugEnabled())
            {
                this.getLogger().debug("namespace mapping not found --> finding default");
            }
            mapping = this.getMapping(mappingObject, context, stereotypes);
        }

        if (this.getLogger().isDebugEnabled())
        {
            this.getLogger().debug("found mapping --> '" + mapping + "'");
        }
        return mapping;
    }

    /**
     * Gets the MetafacadeMappings instance belonging to the <code>namespace</code>.
     *
     * @param namespace the namespace name to check.
     * @return the found MetafacadeMappings.
     */
    private final MetafacadeMappings getNamespaceMappings(final String namespace)
    {
        return (MetafacadeMappings)this.namespaceMetafacadeMappings.get(namespace);
    }

    /**
     * Stores the parent metafacade mappings (if any).
     */
    private MetafacadeMappings parent;

    /**
     * Adds a MetafacadeMappings instance to the namespace metafacade mappings of this instance.
     *
     * @param namespace the namespace name to which the <code>mappings</code> will belong.
     * @param mappings  the MetafacadeMappings instance to add.
     */
    private final void addNamespaceMappings(
        final String namespace,
        final MetafacadeMappings mappings)
    {
        if (mappings != null)
        {
            mappings.parent = this;
            this.namespaceMetafacadeMappings.put(namespace, mappings);
        }
    }

    /**
     * Initializes this mappings instance, which includes discovery of all metafacade
     * mappings instances on the classpath.
     */
    public void initialize()
    {
        MetafacadeImpls.instance().discoverMetafacadeImpls();
        this.initializeMetafacades();
    }

    /**
     * Registers all namespace properties in the shared
     * {@link MetafacadeFactory} instance.
     */
    final void registerAllProperties()
    {
        // register all namespace property references defined in the descriptors
        final Namespaces namespaces = Namespaces.instance();
        for (final Iterator iterator = namespaces.getNamespaces().iterator(); iterator.hasNext();)
        {
            final String mappingsNamespace = ((Namespace)iterator.next()).getName();

            // - add the default mappings
            final Collection mappings = new ArrayList(this.mappings);
            final MetafacadeMappings metafacadeMappings = this.getNamespaceMappings(mappingsNamespace);

            // - add all the references from the default namespace
            final Collection propertyReferences = new ArrayList(this.getPropertyReferences());

            // - if we have namespace mappings, add them
            if (metafacadeMappings != null)
            {
                mappings.addAll(metafacadeMappings.mappings);
                propertyReferences.addAll(metafacadeMappings.getPropertyReferences());
            }

            for (final Iterator mappingIterator = mappings.iterator(); mappingIterator.hasNext();)
            {
                final MetafacadeMapping mapping = (MetafacadeMapping)mappingIterator.next();
                final String metafacadeInterface =
                    MetafacadeImpls.instance().getMetafacadeClass(mapping.getMetafacadeClass().getName()).getName();

                // - first register the references defined globally in the descriptor for each interface
                //   in the hierarchy
                final Class[] interfaces = this.getInterfacesReversed(metafacadeInterface);
                for (int ctr = 0; ctr < interfaces.length; ctr++)
                {
                    this.registerProperties(
                        mappingsNamespace,
                        propertyReferences,
                        interfaces[ctr].getName());
                }

                // - next register the references defined only within each mapping
                // - remember to first load the inherited property references into the mapping
                this.loadInheritedPropertyReferences(mapping);
                this.registerProperties(
                    mappingsNamespace,
                    mapping.getPropertyReferences(),
                    metafacadeInterface);
            }
        }
    }

    /**
     * The name of the metaclass pattern.
     */
    private String metaclassPattern;

    /**
     * First attempts to retrieve the metaclass pattern from this
     * instance, and if not found, attempts to retrieve it from the
     * parent instance (since the parent instance should always have
     * been set at least once from a shared metafacades instance).
     *
     * @return the metaclass pattern.
     */
    private final String getMetaclassPattern()
    {
        if (this.metaclassPattern == null && this.parent != null)
        {
            this.metaclassPattern = this.parent.metaclassPattern;
        }
        return this.metaclassPattern;
    }

    /**
     * Sets the pattern of the metaclass implementations based on a
     * metaclass interface name.  This should only be set on
     * a metafacade mappings instances that is marked as {@link #isShared()}
     *
     * @param metaclassPattern the pattern for the meta classes.
     */
    public void setMetaclassPattern(final String metaclassPattern)
    {
        this.metaclassPattern = metaclassPattern;
    }

    /**
     * Initializes all the metafacade mapping instances.
     */
    private final void initializeMetafacades()
    {
        try
        {
            final Collection metafacades = ComponentContainer.instance().findComponentsOfType(MetafacadeMappings.class);
            final Namespaces namespaces = Namespaces.instance();
            for (final Iterator iterator = metafacades.iterator(); iterator.hasNext();)
            {
                final MetafacadeMappings mappings = (MetafacadeMappings)iterator.next();
                final String namespace = mappings.getNamespace();

                // - If we have 'shared' mappings or only a single set available, they are copied
                //   to this mappings instance.
                if (namespaces.isShared(namespace) || metafacades.size() == 1)
                {
                    // - copy over any 'shared' mappings to this root instance
                    this.copyMappings(mappings);

                    // - set the metaclass pattern from the 'shared' or single instance of metafacades
                    final String metaclassPattern = mappings.metaclassPattern;
                    if (metaclassPattern != null && metaclassPattern.trim().length() > 0)
                    {
                        this.setMetaclassPattern(mappings.metaclassPattern);
                    }
                }
                else
                {
                    // add all others as namespace mappings
                    this.addNamespaceMappings(namespace, mappings);
                }
            }
        }
        catch (final Throwable throwable)
        {
            throw new MetafacadeMappingsException(throwable);
        }
        if (this.getNamespace() == null || this.getNamespace().trim().length() == 0)
        {
            throw new MetafacadeMappingsException(
                "No shared metafacades found, please check your classpath, at least " +
                "one set of metafacades must be marked as 'shared'");
        }
        if (this.metaclassPattern == null || this.metaclassPattern.trim().length() == 0)
        {
            throw new MetafacadeMappingsException(
                "At least one set of metafacades marked as shared " +
                "must have the 'metaclassPattern' attribute defined");
        }
    }

    /**
     * Gets the defaultMetafacadeClass, first looks for it in the namespace mapping, if it can't find it it then takes
     * the default mappings, setting.
     *
     * @return Returns the defaultMetafacadeClass.
     */
    final Class getDefaultMetafacadeClass(final String namespace)
    {
        Class defaultMetafacadeClass = null;
        MetafacadeMappings mappings = this.getNamespaceMappings(namespace);
        if (mappings != null)
        {
            defaultMetafacadeClass = mappings.defaultMetafacadeClass;
        }
        if (defaultMetafacadeClass == null)
        {
            defaultMetafacadeClass = this.defaultMetafacadeClass;
        }
        return defaultMetafacadeClass;
    }

    /**
     * Sets the default metafacade class to use if no other is found
     * for the mapping class.
     *
     * @param defaultMetafacadeClass the default metafacade class.
     */
    public void setDefaultMetafacadeClass(final String defaultMetafacadeClass)
    {
        try
        {
            this.defaultMetafacadeClass = ClassUtils.loadClass(StringUtils.trimToEmpty(defaultMetafacadeClass));
        }
        catch (final Throwable throwable)
        {
            throw new MetafacadeMappingsException(throwable);
        }
    }

    /**
     * Registers the defined property references properties in the metafacade factory.
     *
     * @param propertyReferences the property references to register.
     * @param metafacadeName the name of the metafacade under which to register the properties.
     * @param reference the name of the property reference.
     */
    final void registerProperties(
        final String namespace,
        final Collection propertyReferences,
        final String metafacadeName)
    {
        final MetafacadeFactory factory = MetafacadeFactory.getInstance();
        for (final Iterator iterator = propertyReferences.iterator(); iterator.hasNext();)
        {
            final String reference = (String)iterator.next();
            final String value = Namespaces.instance().getPropertyValue(namespace, reference);
            if (value != null)
            {
                if (this.getLogger().isDebugEnabled())
                {
                    this.getLogger().debug(
                        "setting context property '" + reference + "' with value '" + value + "' for namespace '" +
                        namespace + "' on metafacade '" + metafacadeName + "'");
                }
            }
            factory.registerProperty(namespace, metafacadeName, reference, value);
        }
    }

    /**
     * Performs shutdown procedures for the factory. This should be called <strong>ONLY</code> when {@link
     * MetafacadeFactory#shutdown()}is called.
     */
    final void shutdown()
    {
        this.mappings.clear();
        this.inProcessMappings.clear();
        this.inProcessMetafacades.clear();
        this.namespaceMetafacadeMappings.clear();
        this.propertyReferences.clear();
        this.mappingObjectHierachyCache.clear();
        this.mappingsByMetafacadeClass.clear();
        this.contextHierachyCache.clear();
        this.reversedInterfaceArrayCache.clear();
    }

    /**
     * Returns the logger instance to be used for logging within this class.
     *
     * @return the plugin logger
     */
    private final Logger getLogger()
    {
        return AndroMDALogger.getNamespaceLogger(this.getNamespace());
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return super.toString() + "[" + this.getNamespace() + "]";
    }
}