package org.andromda.maven;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.anttasks.AndroMDAGenTask;
import org.andromda.core.cartridge.AndroMDACartridge;
import org.andromda.core.cartridge.CartridgeDescriptor;
import org.andromda.core.cartridge.CartridgeFinder;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceFinder;
import org.andromda.core.mapping.Mappings;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Just contains some simple utilities used within 
 * the AndroMDA maven plugin.
 * 
 * @author Chad Brandon
 */
public class MavenPluginUtils {
    
    private static final Logger logger = 
        Logger.getLogger(MavenPluginUtils.class);
           
    /**
     * Stores the property values, keyed by logical names.
     */
    private Map namespaceProperties;
    
    /**
     * Stores the available cartridge named, keyed by location.
     */
    private Map cartridgeNames;
    
    /**
     * Where we store andromda plugin resources.
     */
    private static final String PLUGIN_RESOURCES = "plugin-resources/andromda";
    
    /**
     * The prefix for logical property definitions.
     */
    private static final String LOGICAL_PREFIX = "logical.";
    
    /**
     * The prefix for physical property defintions.
     */
    private static final String PHYSICAL_PREFIX = "physical.";
    
    /**
     * The suffix for a property that should be ignored.
     */
    private static final String IGNORE_SUFFIX = ".ignore";
    
    /**
     * The seperator character.
     */
    private static final char SEPERATOR = ':';
    
    /**
     * Constructs an instance of this class.
     */
    public MavenPluginUtils() {
        try {
            // we need to set the correct context class loader
            Thread.currentThread().setContextClassLoader(
               AndroMDAGenTask.class.getClassLoader());
            initializeNamespaceProperties();
            initializeCartridgeNames();
        } catch (Throwable th) {
            String errMsg = "Error constructing MavenPlugUtils";
            logger.error(errMsg, th);
            throw new MavenPluginUtilsException(errMsg, th);
        }
    }
    
    /**
     * Retrieves the artifactId from the passed in
     * <code>artifact</code> string formatted 
     * like &lt;groupId&gt;:&lt;artifactId&gt;.
     * @param artifact the string from which to retrieve
     *        the artifactId.
     * @return String
     */
    public String getArtifactId(String artifact) {
        String artifactId = "";
        if (StringUtils.isNotBlank(artifact)) {
            artifactId = StringUtils.trimToEmpty(artifact);
            int index = artifact.indexOf(SEPERATOR);
            if (index != -1) {
                artifactId = artifact.substring(index + 1, artifact.length());
            }
        }
        return artifactId;
    }
    
    /**
     * Retrieves the groupId from the passed in
     * <code>artifact</code> string formatted 
     * like &lt;groupId&gt;:&lt;artifactId&gt;.
     * @param artifact the string from which to retrieve
     *        the groupId.
     * @return String
     */
    public String getGroupId(String artifact) {
        String groupId = "";
        if (StringUtils.isNotBlank(artifact)) {
            groupId = StringUtils.trimToEmpty(artifact);
            int index = artifact.indexOf(SEPERATOR);
            if (index != -1) {
                groupId = artifact.substring(0, index);
            }
        }
        return groupId;   
    }
    
    /**
     * The prefix contained in maven dependency properties.
     */
    private static final String PROPERTY_PREFIX = "property:";
    
    /**
     * Gets the name of the maven dependency 
     * property assuming the property is in the format 
     * &lt;name&gt;:&lt;value&gt;.  Strips off the 
     * <code>logical</code> or <code>physical</code>
     * prefix on the property name if <code>stripPrefix</code>
     * is true.
     * 
     * @param property the property.
     * @param stripPrefix if true, will strip the prefix off (i.e. strip of
     *        'logical.' etc) before returnning the name.
     * @return the name
     */
    protected String getDependencyPropertyName(String property, boolean stripPrefix) {
        property = StringUtils.trimToEmpty(property);
        String name = null;
        if (StringUtils.isNotEmpty(property)) {
            property = property.replaceFirst(PROPERTY_PREFIX, "");            
            int index = property.indexOf(SEPERATOR);
            if (index != -1) {
                name = property.substring(0, index);
            }
        }
        if (stripPrefix) {
            name = StringUtils.replaceOnce(name, LOGICAL_PREFIX, "");
            name = StringUtils.replaceOnce(name, PHYSICAL_PREFIX, "");
            name = StringUtils.replaceOnce(name, IGNORE_SUFFIX, "");
        }
        return StringUtils.trimToEmpty(name);       
    }
    
    /**
     * Gets the name of the maven dependency 
     * property assuming the property is in the format 
     * &lt;name&gt;:&lt;value&gt;.  Strips off the 
     * <code>logical</code> or <code>physical</code>
     * prefix on the property name.
     * 
     * @param property the property.
     * @return the name
     */
    public String getDependencyPropertyName(String property) {
        return this.getDependencyPropertyName(property, true);   
    }
    
    /**
     * Gets the value of the maven dependency property 
     * assuming the property is in the format 
     * &lt;name&gt;:&lt;value&gt;. Can return both
     * physical and logical properties. Physical properties
     * are prefixed with 'physical.' and logical properties
     * are prefixed with 'logical.', if no prefix
     * is found, physical is assumed.
     * 
     * @param property the property.
     * @return the value 
     */
    public Object getDependencyPropertyValue(String property) {
        property = StringUtils.trimToEmpty(property);
        Object value = null;
        if (StringUtils.isNotEmpty(property)) {
            property = property.replaceFirst(PROPERTY_PREFIX, "");
            
            int index = property.indexOf(SEPERATOR);
            if (index != -1) {
                value = 
                    StringUtils.trimToEmpty(
                        property.substring(index + 1, property.length()));
            }
            // if the property is logical, we attempt to 
            // look up its mapped physical property
            String propertyName = this.getDependencyPropertyName(property, false);
            if (propertyName.startsWith(LOGICAL_PREFIX)) {
                if (logger.isDebugEnabled())
                    logger.debug("looking up logical key --> '" + value + "'");
                value = this.getNamespaceProperty(value);
                if (logger.isDebugEnabled())
                    logger.debug("found value --> '" + value + "'");
            }
        }
        return value;
    }  
    
    /**
     * Looks up the namespace property value.  The 
     * <code>logicalKey</code> is the reference to
     * the physical property value stored within the
     * <code>namespaceProperties</code>.
     * @param logicalKey the logical key of the namespace
     *        property  to retrieve.
     * @return the namespace property value or null if one
     *         can't be found.
     */
    protected Object getNamespaceProperty(Object logicalKey) {
        return namespaceProperties.get(logicalKey);
    }
    
    /**
     * Initializes the namespace properties, these
     * are the properties which are used to store
     * physical property names keyed by logical names.
     */
    protected void initializeNamespaceProperties() {
        final String methodName = 
            "MavenPluginUtils.initializeNamespaceProperties";
        try {
            this.namespaceProperties = new HashMap();
            initalizeMappingLocations();
        } catch (Throwable th) {
            String errMsg = "Error performing " + methodName; 
            logger.error(errMsg, th);    
            throw new MavenPluginUtilsException(errMsg, th);
        }
    }
    
    /**
     * Initializes the maven plugin mappings.
     * 
     * @throws MalformedURLException
     */
    private void initalizeMappingLocations() throws MalformedURLException {
        String mappingsUri = PLUGIN_RESOURCES + "/mappings";
        URL[] mappingResources = ResourceFinder.findResources(mappingsUri);
        if (mappingResources != null) {
            if (logger.isDebugEnabled())
                logger.debug("found '" 
                    + mappingResources.length 
                    + "' mapping directories --> '" 
                    + mappingsUri + "'");
            for (int ctr = 0; mappingResources.length > ctr; ctr++) {
                URL mappingResource = mappingResources[ctr];
		        if (mappingResource == null) {
		            logger.error("Could not find --> '" + mappingsUri + "'");
		        } else {
		            File mappingsDir = new File(mappingResource.getFile());
		            File[] mappingFiles = mappingsDir.listFiles();
		            if (mappingFiles != null && mappingFiles.length > 0) {
		                File mappingFile = mappingFiles[ctr];
		                for (int ctr2 = 0; ctr2 < mappingFiles.length; ctr2++) {
		                    if (logger.isDebugEnabled())
		                        logger.debug("loading mapping --> '" + mappingFile + "'");
		                    Mappings mappings = 
		                        Mappings.getInstance(mappingFiles[ctr2].toURL());
		                    namespaceProperties.put(mappings.getName(), mappings.getResource());
		                }
		            }
		        } 
            }
        }
    }
    
    /**
     * Initializes the <code>cartridgeNames</code> map.
     */
    private void initializeCartridgeNames() {
        this.cartridgeNames = new HashMap();
        CartridgeFinder.instance().discoverCartridges(false);
        Collection cartridges = 
            CartridgeFinder.instance().getCartridges();
        if (cartridges != null && !cartridges.isEmpty()) {
            Iterator cartridgeIt = cartridges.iterator();
            while (cartridgeIt.hasNext()) {
	            AndroMDACartridge cartridge = 
	                (AndroMDACartridge)cartridgeIt.next();
	            CartridgeDescriptor descriptor = 
	                cartridge.getDescriptor();
	            if (descriptor != null) {
	                cartridgeNames.put(
	                    descriptor.getDefinitionURL(), 
	                    descriptor.getCartridgeName());		                    
	            }
            }
        }
    }
    
    /**
     * Removes the <code>remove</code> from the
     * given <code>path</code> and returns the resulting
     * string.
     * @param path the path from which to remove.
     * @param remove the value to remove.
     * @return the resulting path with <code>remove</code>
     *         removed.
     */
    public String removeFromPath(String path, String remove) {
        path = StringUtils.trimToEmpty(path);
        remove = StringUtils.trimToEmpty(remove);
        remove = remove.replace('\\', '/');
        path = path.replace('\\', '/');
        return StringUtils.replace(path, remove, "");
    }
    
    /**
     * Returns <code>true</code> if the 
     * specified <code>property</code> is
     * ignored.  A property will be ignored if it
     * has the 'ignore.' suffix.
     * @param property
     * @return
     */
    public boolean isDependencyPropertyIgnored(String property) {
        return this.getDependencyPropertyName(property, false).endsWith(IGNORE_SUFFIX);
    }
    
    /**
     * Gets the name of the cartridge for the given
     * location.  Since the cartridge is found on the classpath,
     * a cartridge will have one and only one location, therefore
     * we can uses the <code>location</code> as the key.
     * @param location the location of the cartidge.
     * @return the cartridge name
     */
    public String getCartridgeName(URL dependencyUri) {
        final String methodName = "MavenPluginUtils.getCartridgeName";
        ExceptionUtils.checkNull(methodName, "dependencyUri", dependencyUri);
        String cartridgeName = "";
        // now we loop through the map contents and find the one
        // that has a location LIKE the dependencyUri (since we won't
        // find an exact match)
        Iterator cartridgeLocationIt = cartridgeNames.keySet().iterator();
        while (cartridgeLocationIt.hasNext()) {
            URL cartridgeXmlUri = (URL)cartridgeLocationIt.next();
            String replacePatterns = "[\\\\/]";
            String cartridgeXml = cartridgeXmlUri.toString().replaceAll(replacePatterns, "");
            String cartridgeDependencyUri = 
                dependencyUri.toString().replaceAll(replacePatterns, "");
            if (cartridgeXml.indexOf(cartridgeDependencyUri) != -1) {
                cartridgeName = (String)cartridgeNames.get(cartridgeXmlUri);
                break;
            }
        }
        return cartridgeName;
    }
    
}
