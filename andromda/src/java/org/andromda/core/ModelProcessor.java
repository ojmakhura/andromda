package org.andromda.core;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.andromda.core.cartridge.Cartridge;
import org.andromda.core.common.CodeGenerationContext;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Namespace;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.PluginDiscoverer;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelValidationMessage;
import org.andromda.core.repository.RepositoryFacade;
import org.apache.log4j.Logger;

/**
 * <p>
 * The main entry point to the framework, handles the loading/processing of
 * models by plugins. Facilitates Model Driven Architecture by enabling the
 * generation of source code, configuration files, and other such artifacts from
 * a single or multiple <code>MOF</code> models.
 * </p>
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author <A HREF="http://www.amowers.com">Anthony Mowers </A>
 * @author Chad Brandon
 */
public class ModelProcessor
{
    private static final Logger logger = Logger.getLogger(ModelProcessor.class);

    private static ModelProcessor instance = null;
    
    /**
     * Stores the current version of AndroMDA
     */
    private static final String VERSION;
    
    /**
     * Find and load the version.
     */
    static 
    {
        final String versionPropertiesUri = 
            "META-INF/andromda-version.properties";
        final String versionPropertyName = "andromda.version";
        try 
        {
	        URL versionUri = 
	            ResourceUtils.getResource(versionPropertiesUri);
	        if (versionUri == null)
	        {
	            throw new ModelProcessorException(
	                "Could not load file --> '" + versionPropertiesUri + "'");
	        }
	        Properties properties = new Properties();
	        properties.load(versionUri.openStream());
	        VERSION = properties.getProperty("andromda.version");
	        if (VERSION == null)
	        {
	            throw new ModelProcessorException("Could not find '"
	                + versionPropertyName 
	                + "' in '" + versionPropertiesUri + "'");
	        }
        } 
        catch (Throwable th) 
        {
            throw new ModelProcessorException(th);
        }
    }

    /**
     * Gets the shared instance of the ModelProcessor.
     * 
     * @return the shared ModelProcessor instance.
     */
    public static ModelProcessor instance()
    {
        if (instance == null)
        {
            instance = new ModelProcessor();
        }
        return instance;
    }

    /**
     * Processes all <code>models</code> with the given
     * <code>cartridges</code>.
     * 
     * @param models an array of URLs to models.
     * @param cartridges the Cartridge instances to process with.
     */
    public void process(Model[] models)
    {
        final String methodName = "ModelProcessor.process";
        ExceptionUtils.checkNull(methodName, "models", models);

        this.printConsoleHeader();
        
        long startTime = System.currentTimeMillis();

        try
        {
            PluginDiscoverer.instance().discoverPlugins();

            Collection cartridges = PluginDiscoverer.instance().findPlugins(
                Cartridge.class);

            if (cartridges.size() <= 0)
            {
                AndroMDALogger
                    .warn("WARNING! No cartridges found, check your classpath!");
            }

            RepositoryFacade repository = (RepositoryFacade)ComponentContainer
                .instance().findComponent(RepositoryFacade.class);
            if (repository == null)
            {
                throw new ModelProcessorException(
                    "No Repository could be found, "
                        + "please make sure you have a "
                        + RepositoryFacade.class.getName()
                        + " instance on your classpath");
            }
            repository.open();

            if (models != null && cartridges != null)
            {
                for (int ctr = 0; ctr < models.length; ctr++)
                {
                    process(repository, models[ctr], cartridges);
                }
            }

            repository.close();
            repository = null;

        }
        finally
        {
            // log all the error messages
            Collection messages = MetafacadeFactory.getInstance()
                .getValidationMessages();
            if (messages != null && !messages.isEmpty())
            {
                AndroMDALogger.setSuffix("VALIDATION:ERROR");
                Iterator messageIt = messages.iterator();
                while (messageIt.hasNext())
                {
                    ModelValidationMessage message = (ModelValidationMessage)messageIt
                        .next();
                    AndroMDALogger.error(message);
                }
                AndroMDALogger.reset();
            }
            AndroMDALogger.info("completed model processing, TIME --> "
                + ((System.currentTimeMillis() - startTime) / 1000.0) + "[s]");
        }
    }

    /**
     * Processes a single <code>model</code>
     * 
     * @param repository the RepositoryFacade that will be used to read/load the
     *        model
     * @param model the Model to process.
     */
    private void process(
        RepositoryFacade repository,
        Model model,
        Collection cartridges)
    {
        final String methodName = "ModelProcessor.process";
        try
        {
            //-- command line status
            AndroMDALogger.info("Input model --> '" + model.getUrl() + "'");

            repository.readModel(model.getUrl(), model.getModuleSearchPath());

            CodeGenerationContext context = new CodeGenerationContext(
                repository,
                model.isLastModifiedCheck(),
                model.getPackages());

            Namespace defaultNamespace = Namespaces.instance().findNamespace(
                Namespaces.DEFAULT);

            for (Iterator cartridgeIt = cartridges.iterator(); cartridgeIt
                .hasNext();)
            {
                Cartridge cartridge = (Cartridge)cartridgeIt.next();

                String cartridgeName = cartridge.getName();

                Namespace namespace = Namespaces.instance().findNamespace(
                    cartridgeName);

                boolean ignoreNamespace = false;
                if (namespace != null)
                {
                    ignoreNamespace = namespace.isIgnore();
                }

                // make sure we ignore the cartridge if the namespace
                // is set to 'ignore'
                if ((namespace != null || defaultNamespace != null)
                    && !ignoreNamespace)
                {
                    cartridge.init();
                    cartridge.processModelElements(context);
                    cartridge.shutdown();
                }
                else
                {
                    AndroMDALogger
                        .info("namespace for '"
                            + cartridgeName
                            + "' cartridge is either not defined, or has the ignore "
                            + "attribute set to 'true' --> skipping processing");
                }
            }
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing " + methodName
                + " with model --> '" + model.getUrl() + "'";
            logger.error(errMsg, th);
            throw new ModelProcessorException(errMsg, th);

        }
    }
    
    /**
     * Prints the console header.
     */
    protected void printConsoleHeader()
    {
        AndroMDALogger.info("");
        AndroMDALogger.info("A n d r o M D A  -  " + VERSION);
        AndroMDALogger.info("");
    }


}