package org.andromda.core.metafacade;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Merger;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.ResourceFinder;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * The Metafacade mapping class. Used to map <code>metafacade</code> objects
 * to <code>metamodel</code> objects.
 * 
 * @see MetafacadeMapping
 * @see org.andromda.core.common.XmlObjectFactory
 * @author Chad Brandon
 */
public class MetafacadeMappings
{
    private Logger logger = Logger.getLogger(MetafacadeMappings.class);

    /**
     * Contains the mappings XML used for mapping Metafacades.
     */
    private static final String METAFACADES_URI = "META-INF/andromda-metafacades.xml";

    /**
     * Holds the references to the child MetafacadeMapping instances.
     */
    private final Map mappings = new HashMap();

    /**
     * Holds the namespace MetafacadeMappings. This are child MetafacadeMappings
     * keyed by namespace name.
     */
    private final Map namespaceMetafacadeMappings = new HashMap();

    /**
     * Holds the resource path from which this MetafacadeMappings object was
     * loaded.
     */
    private URL resource;

    /**
     * Contains references to properties populated in the Namespaces.
     */
    private final Map propertyReferences = new HashMap();

    /**
     * Property references keyed by namespace, these are populated on the first
     * call to getPropertyReferences below.
     */
    private final Map namespacePropertyReferences = new HashMap();

    /**
     * The shared static instance.
     */
    private static final MetafacadeMappings instance = new MetafacadeMappings();

    /**
     * The default meta facade to use when there isn't a mapping found.
     */
    private Class defaultMetafacadeClass = null;

    /**
     * Gets the shared instance.
     * 
     * @return MetafacadeMappings
     */
    public static MetafacadeMappings instance()
    {
        return instance;
    }

    /**
     * Returns a new configured instance of this MetafacadeMappings configured
     * from the mappings configuration URI.
     * 
     * @param mappingsUri the URI to the XML type mappings configuration file.
     * @return MetafacadeMappings the configured MetafacadeMappings instance.
     */
    protected static MetafacadeMappings getInstance(URL mappingsUri)
    {
        final String methodName = "MetafacadeMappings.getInstance";
        ExceptionUtils.checkNull(methodName, "mappingsUri", mappingsUri);
        XmlObjectFactory factory = XmlObjectFactory
            .getInstance(MetafacadeMappings.class);
        MetafacadeMappings mappings = (MetafacadeMappings)factory
            .getObject(mappingsUri);
        // after we've gotten the initial instance we can merge the file
        // since we know the namespace
        String mappingsContents = ResourceUtils.getContents(mappingsUri);
        mappingsContents = Merger.instance().getMergedString(
            mappingsContents,
            mappings.getNamespace());
        mappings = (MetafacadeMappings)factory.getObject(mappingsContents);
        mappings.resource = mappingsUri;
        return mappings;
    }

    /**
     * The namespace to which this MetafacadeMappings instance applies.
     */
    private String namespace = null;

    /**
     * @return Returns the namespace.
     */
    public String getNamespace()
    {
        final String methodName = "MetafacadeMappings.getNamespace";
        ExceptionUtils.checkEmpty(methodName, "namespace", this.namespace);
        return this.namespace;
    }

    /**
     * @param namespace The namepace to set.
     */
    public void setNamespace(String namespace)
    {
        this.namespace = StringUtils.trimToEmpty(namespace);
    }

    /**
     * Whether or not these mappings are shared across all namespaces.
     */
    private boolean shared = false;

    /**
     * Gets whether or not this set of <code>metafacade</code> mappings is
     * shared across all namespaces. By default mappings are <strong>NOT
     * </strong> shared.
     * 
     * @return Returns the shared.
     */
    public boolean isShared()
    {
        return shared;
    }

    /**
     * Sets whether or not this set of <code>metafacade</code> mappings is
     * shared across all namespaces.
     * 
     * @param shared The shared to set.
     */
    public void setShared(boolean shared)
    {
        this.shared = shared;
    }

    /**
     * Adds a MetafacadeMapping instance to the set of current mappings.
     * 
     * @param mapping the MetafacadeMapping instance.
     */
    public void addMapping(MetafacadeMapping mapping)
    {
        final String methodName = "MetafacadeMappings.addMapping";
        ExceptionUtils.checkNull(methodName, "mapping", mapping);

        String mappingClassName = mapping.getMappingClassName();
        ExceptionUtils.checkEmpty(
            methodName,
            "mapping.mappingClassName",
            mappingClassName);
        ExceptionUtils.checkNull(methodName, "mapping.metafacadeClass", mapping
            .getMetafacadeClass());
        
        this.mappings.put(mapping.getKey(), mapping);
        mappingsByMetafacadeClass.put(MetafacadeImpls.instance().getMetafacadeClass(mapping
            .getMetafacadeClass().getName()), mapping);
    }

    /**
     * Stores mappings by the metafacade class so that we
     * can retrieve the inherited metafacade classes.
     */
    private final Map mappingsByMetafacadeClass = new HashMap();

    /**
     * Copies all data from <code>mappings<code> to this
     * instance.
     * 
     * @param mappings the mappings to add
     */
    private void copyMappings(MetafacadeMappings mappings)
    {
        final String methodName = "MetafacadeMappings.copyMappings";
        ExceptionUtils.checkNull(methodName, "mappings", mappings);
        // the namespace is always the default namespace
        this.setNamespace(Namespaces.DEFAULT);
        Iterator keyIt = mappings.mappings.keySet().iterator();
        while (keyIt.hasNext())
        {
           this.addMapping((MetafacadeMapping)mappings.mappings.get(keyIt.next()));
        }
        Map propertyRefs = mappings.propertyReferences;
        if (propertyRefs != null && !propertyRefs.isEmpty())
        {
            this.propertyReferences.putAll(propertyRefs);
        }
        this.defaultMetafacadeClass = mappings.defaultMetafacadeClass;
    }

    /**
     * <p>
     * Retrieves the MetafacadeMapping belonging to the unique <code>key</code>
     * created from the <code>mappingClass</code>, <code>context</code> and given
     * <code>stereotypes</code>. It's <strong>IMPORTANT</strong> to note that
     * contexts have a higher priority than stereotypes.
     * This allows us to retrieve mappings based on
     * the following combinations:
     * <ul>
     * <li>A single stereotype no context</li>
     * <li>A single stereotype with a context</li>
     * <li>multiple stereotypes no context</li>
     * <li>multiple stereotypes with a context</li>
     * </ul>
     * </p>
     * <p>
     * NOTE: mapping properties are inherited from super metafacades.
     * </p>
     * 
     * @param mappingClass the class name of the meta model object.
     * @param stereotypes the stereotypes to check.
     * @param rootContext the context within the namespace for which the mapping
     *        applies (has 'root' in the name because of the fact that we also
     *        search the context inheritance hiearchy started with this 'root'
     *        context).
     * @return MetafacadeMapping
     */
    protected MetafacadeMapping getMapping(
        final String mappingClass,
        final String rootContext,
        final Collection stereotypes)
    {
        MetafacadeMapping mapping = null;
        List contextHierarchy = this.getContextHierarchy(rootContext);
        Iterator contextIterator = contextHierarchy.iterator();
        while (contextIterator.hasNext() && mapping == null)
        {
            String context = String.valueOf(contextIterator.next());           
            // first: try the context with all the combination of all 
            // stereotypes, we start with all stereotypes and go backwards
            if (stereotypes != null)
            {
                for (int ctr = stereotypes.size(); ctr > 0 && mapping == null; ctr--)
                {
                    String contextStereotypesKey = MetafacadeMappingsUtils.constructKey(
                        mappingClass, 
                        context, 
                        stereotypes);
                    mapping = (MetafacadeMapping)this.getMapping(contextStereotypesKey);
                }              
                // second: try the context with each single stereotype 
                // (because of the fact we're constructing a key by 
                // sorting the stereotypes and not every single stereotype 
                // will be checked because of the sorting).
                if (mapping == null)
                {
                    Iterator stereotypeIterator = stereotypes.iterator();
                    while (stereotypeIterator.hasNext() && mapping == null)
                    {
                        String stereotype = String.valueOf(stereotypeIterator.next());
                        String contextStereotypeKey = MetafacadeMappingsUtils.constructKey(
                            mappingClass, 
                            context, 
                            stereotype);
                        mapping = (MetafacadeMapping)this.getMapping(contextStereotypeKey);
                    }                   
                }
            }
            if (mapping == null)
            {
                // third: try a single context mapping (no stereotypes)
                String contextKey = MetafacadeMappingsUtils.constructKey(mappingClass, context);
                mapping = (MetafacadeMapping)this.getMapping(contextKey);
            }
        }        
        // now we try to retrieve any mappings that are mapped only to one or more stereotypes
        if (mapping == null && stereotypes != null)
        {
            // fourth: try the context with all the combination of all stereotypes,
            // we start with all stereotypes and go backwards
            for (int ctr = stereotypes.size(); ctr > 0 && mapping == null; ctr--)
            {
                String stereotypesKey = MetafacadeMappingsUtils.constructKey(
                    mappingClass, 
                    stereotypes);
                mapping = (MetafacadeMapping)this.getMapping(stereotypesKey);
            }
            // fifth: try each single stereotype (because of the fact 
            // we're constructing a key by sorting the stereotypes and 
            // not every single stereotype will be checked because of the sorting).
            if (mapping == null)
            {
                Iterator stereotypeIterator = stereotypes.iterator();
                while (stereotypeIterator.hasNext() && mapping == null)
                {
                    String stereotype = String.valueOf(stereotypeIterator.next());
                    String stereotypeKey = MetafacadeMappingsUtils.constructKey(mappingClass, stereotype);
                    mapping = (MetafacadeMapping)this.getMapping(stereotypeKey);
                }                   
            }
        }
        // finally: we check for a mapping with just the mappingClass 
        // as the key
        if (mapping == null)
        {  
            mapping = (MetafacadeMapping)this.getMapping(mappingClass);
        }
        mapping = this.loadInheritedPropertyReferences(mapping, rootContext);
        return mapping;
    }

    /**
     * Searches both the current instance and the parent (if any) for the
     * mapping having the given <code>key</code>.
     * 
     * @param key the unique mapping key for a metafacade mapping.
     */
    private Object getMapping(String key)
    {
        Object mapping = this.mappings.get(key);
        if (mapping == null && this.parent != null)
        {
            mapping = parent.mappings.get(key);
        }
        return mapping;
    }

    /**
     * <p>
     * Loads all property references into the given <code>mapping</code> 
     * inherited from any super metafacade of the given mapping's metafacade.
     * </p>
     * 
     * @param mapping the MetafacadeMapping to which we'll add the inherited
     *        property references.
     * @param context the <code>context</code> to begin with.
     * @return The MetafacadeMapping found having the correct
     *         <code>context</code>.
     */
    private MetafacadeMapping loadInheritedPropertyReferences(
        MetafacadeMapping mapping,
        String context)
    {
        if (mapping != null)
        {
            Class[] interfaces = (Class[])this.getInterfaces(context).toArray(
                new Class[0]);
            if (interfaces != null && interfaces.length > 0)
            {
                CollectionUtils.reverseArray(interfaces);
                for (int ctr = 0; ctr < interfaces.length; ctr++)
                {
                    Class metafacadeClass = interfaces[ctr];
                    MetafacadeMapping contextMapping = (MetafacadeMapping)this.mappingsByMetafacadeClass
                        .get(metafacadeClass);
                    if (contextMapping != null)
                    {
                        // add all property references
                        mapping.addPropertyReferences(contextMapping
                            .getPropertyReferences());
                    }
                }
            }
        }
        return mapping;
    }

    /**
     * Retrieves all inherited contexts (including the root <code>context</code>)
     * from the given <code>context</code> and returns a list containing all
     * of them.
     * 
     * @param context the root contexts
     * @return a list containing all inherited contexts
     */
    private List getContextHierarchy(String context)
    {
        List contexts = this.getInterfaces(context);
        if (contexts != null)
        {
            CollectionUtils.transform(contexts, new Transformer()
            {
                public Object transform(Object object)
                {
                    return ((Class)object).getName();
                }
            });
        }
        return contexts;
    }

    /**
     * Retrieves all interfaces for the given <code>className</code>
     * (including the interface for <code>className</code> itself).
     * 
     * @param context the root context
     * @return a list containing all context interfaces ordered from the root
     *         down.
     */
    private List getInterfaces(String className)
    {
        List interfaces = new ArrayList();
        if (StringUtils.isNotEmpty(className))
        {
            Class contextClass = ClassUtils.loadClass(className);
            interfaces.addAll(ClassUtils.getAllInterfaces(contextClass));
            interfaces.add(0, contextClass);
        }
        return interfaces;
    }

    /**
     * Gets the resource that configured this instance.
     * 
     * @return URL to the resource.
     */
    protected URL getResource()
    {
        return this.resource;
    }

    /**
     * Adds a language mapping reference. This are used to populate metafacade
     * impl classes with mapping files (such as those that map from model types
     * to Java, JDBC, SQL types, etc). If its added here as opposed to each
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
     * Returns all property references for this MetafacadeMappings by
     * <code>namespace</code> (these include all default mapping references as
     * well).
     * 
     * @param namespace the namespace to search
     */
    public Map getPropertyReferences(String namespace)
    {
        Map propertyReferences = (Map)namespacePropertyReferences.get(namespace);
        if (propertyReferences == null)
        {
            // first load the property references from
            // the mappings
            propertyReferences = new HashMap();
            propertyReferences.putAll(this.propertyReferences);
            MetafacadeMappings metafacades = this
                .getNamespaceMappings(namespace);
            if (metafacades != null)
            {
                propertyReferences.putAll(metafacades.propertyReferences);
            }
            this.namespacePropertyReferences.put(namespace, propertyReferences);
        }

        return propertyReferences;
    }

    /**
     * <p>
     * Attempts to get the MetafacadeMapping identified by the given
     * <code>mappingClass</code>, <code>context</code> and <code>stereotypes<code>, 
     * from the mappings for the given <code>namespace</code>. If it 
     * can <strong>not</strong> be found, it will search the default 
     * mappings and return that instead. 
     * </p>
     * <p>
     * <strong>IMPORTANT:</strong> The <code>context</code> will take precedence
     * over any <code>stereotypes</code> with the mapping.
     * </p>
     * 
     * @param mappingClass the class name of the meta object for the mapping
     *        we are trying to find.
     * @param namespace the namespace (i.e. a cartridge, name, etc.)
     * @param context to which the mapping applies (note this takes precendence over
     *        stereotypes).
     * @param stereotypes collection of sterotype names.  We'll check to see if 
     *        the mapping for the given <code>mappingClass</code> is defined for it.
     */
    public MetafacadeMapping getMetafacadeMapping(
        String mappingClass,
        String namespace,
        String context,
        Collection stereotypes)
    {
        final String methodName = "MetafacadeMappings.getMetafacadeMapping";
        if (logger.isDebugEnabled())
            logger.debug("performing '" + methodName + "' with mappingClass '"
                + mappingClass + "', stereotypes '" + stereotypes
                + "', namespace '" + namespace + "' and context '" + context
                + "'");

        MetafacadeMappings mappings = this.getNamespaceMappings(namespace);
        MetafacadeMapping mapping = null;

        // first try the namespace mappings
        if (mappings != null)
        {
            mapping = mappings.getMapping(mappingClass, context, stereotypes);
        }

        // if we've found a namespace mapping, try to get any shared mappings
        // that this namespace mapping may extend and copy over any property
        // references from the shared mapping to the namespace mapping.
        if (mapping != null)
        {
            Map propertyReferences = mapping.getPropertyReferences();
            MetafacadeMapping defaultMapping = this.getMapping(
                mappingClass,
                context,
                stereotypes);
            if (defaultMapping != null)
            {
                Map defaultPropertyReferences = defaultMapping
                    .getPropertyReferences();
                MetafacadeImpls metafacadeClasses = MetafacadeImpls.instance();
                Class metafacadeInterface = metafacadeClasses
                    .getMetafacadeClass(mapping.getMetafacadeClass().getName());
                Class defaultMetafacadeInterface = metafacadeClasses
                    .getMetafacadeClass(defaultMapping.getMetafacadeClass()
                        .getName());
                if (defaultMetafacadeInterface
                    .isAssignableFrom(metafacadeInterface))
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
            if (logger.isDebugEnabled())
                logger.debug("namespace mapping not found --> finding default");
            mapping = this.getMapping(mappingClass, context, stereotypes);
        }

        if (logger.isDebugEnabled())
            logger.debug("found mapping --> '" + mapping + "'");
        return mapping;
    }

    /**
     * Gets the MetafacadeMappings instance belonging to the
     * <code>namespace</code>.
     * 
     * @param namespace the namespace name to check.
     * @return the found MetafacadeMappings.
     */
    private MetafacadeMappings getNamespaceMappings(String namespace)
    {
        return (MetafacadeMappings)this.namespaceMetafacadeMappings
            .get(namespace);
    }

    /**
     * Stores the parent metafacade mappings (if any).
     */
    private MetafacadeMappings parent;

    /**
     * Adds another MetafacadeMappings instance to the namespace metafacade
     * mappings of this instance.
     * 
     * @param namespace the namespace name to which the <code>mappings</code>
     *        will belong.
     * @param mappings the MetafacadeMappings instance to add.
     */
    private void addNamespaceMappings(
        String namespace,
        MetafacadeMappings mappings)
    {
        if (mappings != null)
        {
            mappings.parent = this;
            this.namespaceMetafacadeMappings.put(namespace, mappings);
        }
    }

    /**
     * Discover all metafacade mapping files on the class path. You need to call
     * this anytime you want to find another metafacade library that may have
     * been made available.
     */
    public void discoverMetafacades()
    {
        final String methodName = "MetafacadeMappings.discoverMetafacadeMappings";
        URL uris[] = ResourceFinder.findResources(METAFACADES_URI);
        if (uris == null || uris.length == 0)
        {
            logger
                .error("ERROR!! No metafacades found, please check your classpath");
        }
        else
        {
            AndroMDALogger.info("-- discovering metafacades --");
            try
            {
                // will store all namespaces (other than default)
                Collection namespaces = new ArrayList();
                for (int ctr = 0; ctr < uris.length; ctr++)
                {
                    MetafacadeMappings mappings = MetafacadeMappings
                        .getInstance(uris[ctr]);
                    String namespace = mappings.getNamespace();
                    if (StringUtils.isEmpty(namespace))
                    {
                        throw new MetafacadeMappingsException(
                            methodName
                                + " no 'namespace' has been set for metafacades --> '"
                                + mappings.getResource() + "'");
                    }

                    // 'shared' mappings are copied
                    // to this shared mappings instance.
                    if (mappings.isShared())
                    {
                        // copy over any 'shared' mappings
                        this.copyMappings(mappings);
                    }
                    else
                    {
                        // add all others as namespace mappings
                        this.addNamespaceMappings(
                            mappings.getNamespace(),
                            mappings);
                        namespaces.add(mappings.getNamespace());
                    }
                    // construct the found informational based
                    // on whether or not the mappings are shared.
                    StringBuffer foundMessage = new StringBuffer("found");
                    if (mappings.isShared())
                    {
                        foundMessage.append(" shared");
                    }
                    foundMessage.append(" metafacades --> '"
                        + mappings.getNamespace() + "'");
                    if (mappings.isShared())
                    {
                        foundMessage.append(" - adding to '"
                            + Namespaces.DEFAULT + "' namespace");
                    }
                    AndroMDALogger.info(foundMessage);
                }
            }
            catch (Throwable th)
            {
                String errMsg = "Error performing " + methodName;
                logger.error(errMsg, th);
                throw new MetafacadeMappingsException(errMsg, th);
            }
            if (StringUtils.isEmpty(this.namespace))
            {
                String errMsg = "No shared metafacades "
                    + "found, please check your classpath, at least "
                    + "one set of metafacades must be marked as 'shared'";
                throw new MetafacadeMappingsException(errMsg);
            }
        }
    }

    /**
     * Gets the defaultMetafacadeClass, first looks for it in the namespace
     * mapping, if it can't find it it then takes the default mappings, setting.
     * 
     * @return Returns the defaultMetafacadeClass.
     */
    public Class getDefaultMetafacadeClass(String namespace)
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
     * @param defaultMetafacadeClass The defaultMetafacadeClass to set.
     */
    public void setDefaultMetafacadeClass(String defaultMetafacadeClass)
    {
        try
        {
            this.defaultMetafacadeClass = ClassUtils.loadClass(StringUtils
                .trimToEmpty(defaultMetafacadeClass));
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing MetafacadeMappings.setDefaultMetafacadeClass";
            throw new MetafacadeMappingsException(errMsg, th);
        }
    }

    /**
     * Performs shutdown procedures for the factory. This should be called
     * <strong>ONLY</code> when {@link MetafacadeFactory#shutdown()}is called.
     */
    void shutdown()
    {
        this.mappings.clear();
        this.namespaceMetafacadeMappings.clear();
        this.propertyReferences.clear();
        this.namespacePropertyReferences.clear();
        this.mappingsByMetafacadeClass.clear();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}