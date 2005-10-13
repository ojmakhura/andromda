package org.andromda.transformations.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.andromda.core.configuration.Configuration;
import org.andromda.core.configuration.Namespace;
import org.andromda.core.configuration.Property;
import org.atl.engine.injectors.Injector;
import org.atl.engine.vm.nativelib.ASMBoolean;
import org.atl.engine.vm.nativelib.ASMModel;
import org.atl.engine.vm.nativelib.ASMModelElement;
import org.atl.engine.vm.nativelib.ASMSet;
import org.atl.engine.vm.nativelib.ASMString;

/**
 * Converts an AndroMDA configuration to an ASM Model.
 * 
 * @since 12.10.2005
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class ConfigurationInjector implements Injector
{
    /**
     * The root of the ASMModel (e.g. the Configuration object).
     */
    private ASMModelElement modelRoot;
    
    /**
     * The AndroMDA configuration to be processed.
     */
    private Configuration configuration;
    
    /**
     * Constructs an Injector for AndroMDA configuration models.
     * @param configuration the Configuration to inject
     */
    public ConfigurationInjector(Configuration configuration)
    {
        this.configuration = configuration;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.atl.engine.injectors.Injector#performImportation(org.atl.engine.vm.nativelib.ASMModel,
     *      org.atl.engine.vm.nativelib.ASMModel, java.io.InputStream,
     *      java.lang.String)
     */
    public void performImportation(ASMModel format, ASMModel extent,
            InputStream in, String other) throws IOException
    {
        // create the configuration root of the model
        ASMModelElement config = extent.newModelElement("Configuration");
        
        // inject all the global properties
        Set injectedGlobalProperties = new HashSet();
        Property globalProperties[] = configuration.getProperties();
        for (int i = 0; i < globalProperties.length; i++)
        {
            Property p = globalProperties[i];
            injectedGlobalProperties.add(injectProperty(extent, p));
        }
        
        // connect to the configuration 
        config.set(null, "globalProperties", new ASMSet(injectedGlobalProperties));
    
        // inject all the namespaces
        Set injectedNamespaces = new HashSet();
        Namespace namespaces[] = configuration.getNamespaces();
        for (int i = 0; i < namespaces.length; i++)
        {
            injectedNamespaces.add(injectNamespace(extent, namespaces[i]));
        }
        
        // connect to the configuration
        config.set(null, "namespaces", new ASMSet(injectedNamespaces));
        
        // store to return it later
        modelRoot = config;
    }

    /**
     * Injects a Namespace with all its Properties.
     * 
     * @param extent the target model extent
     * @param namespace the namespace to inject
     * @return the Namespace model element that has been created
     */
    private ASMModelElement injectNamespace(ASMModel extent, Namespace namespace)
    {
        ASMModelElement namespaceModelElement = extent.newModelElement("Namespace");
        namespaceModelElement.set(null, "name", new ASMString(namespace.getName()));
        
        Set injectedProperties = new HashSet();
        for (Iterator iter = namespace.getProperties().iterator(); iter.hasNext();)
        {
            Property p = (Property) iter.next();
            injectedProperties.add(injectProperty(extent, p));
        }
        
        namespaceModelElement.set(null, "properties", new ASMSet(injectedProperties));
        
        return namespaceModelElement;
    }

    /**
     * Injects a single Property into the model.
     * 
     * @param extent the target model extent
     * @param p the property to inject
     * @return the Property model element that has been created
     */
    private ASMModelElement injectProperty(ASMModel extent, Property p)
    {
        ASMModelElement propertyModelElement = extent.newModelElement("Property");
        propertyModelElement.set(null, "name", new ASMString(p.getName()));
        propertyModelElement.set(null, "value", new ASMString(p.getValue()));
        propertyModelElement.set(null, "ignore", new ASMBoolean(p.isIgnore()));
        return propertyModelElement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.atl.engine.injectors.Injector#getParameterTypes()
     */
    public Map getParameterTypes()
    {
        return Collections.EMPTY_MAP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.atl.engine.injectors.Injector#inject(org.atl.engine.vm.nativelib.ASMModel,
     *      java.io.InputStream, java.util.Map)
     */
    public ASMModelElement inject(ASMModel targetModel, InputStream source,
            Map params) throws IOException
    {
        performImportation(null, targetModel, source, null);
        return modelRoot;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.atl.engine.injectors.Injector#getPrefix()
     */
    public String getPrefix()
    {
        return "config";
    }

}
