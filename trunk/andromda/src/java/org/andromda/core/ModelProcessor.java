package org.andromda.core;

import java.io.InputStream;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.andromda.core.cartridge.Cartridge;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.CodeGenerationContext;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ModelPackages;
import org.andromda.core.common.Namespace;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.PluginDiscoverer;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.MetafacadeImpls;
import org.andromda.core.metafacade.MetafacadeMappings;
import org.andromda.core.metafacade.ModelValidationMessage;
import org.andromda.core.repository.RepositoryFacade;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
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
 * @author <a href="http://www.amowers.com">Anthony Mowers </a>
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
     * Stores whether or not to process all model packages
     */
    private boolean processAllModelPackages = true;

    /**
     * Find and load the version.
     */
    static
    {
        final String versionPropertiesUri = "META-INF/andromda-version.properties";
        final String versionPropertyName = "andromda.version";
        try
        {
            URL versionUri = ResourceUtils.getResource(versionPropertiesUri);
            if (versionUri == null)
            {
                throw new ModelProcessorException("Could not load file --> '"
                    + versionPropertiesUri + "'");
            }
            Properties properties = new Properties();
            InputStream stream = versionUri.openStream();
            properties.load(stream);
            stream.close();
            stream = null;
            VERSION = properties.getProperty("andromda.version");
            if (VERSION == null)
            {
                throw new ModelProcessorException("Could not find '"
                    + versionPropertyName + "' in '" + versionPropertiesUri
                    + "'");
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
     * Processes all <code>models</code> with the the discovered plugins.
     * 
     * @param models an array of URLs to models.
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
            MetafacadeMappings.instance().discoverMetafacades();
            MetafacadeImpls.instance().discoverMetafacadeImpls();

            Collection cartridges = PluginDiscoverer.instance().findPlugins(
                Cartridge.class);

            if (cartridges.isEmpty())
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

            if (models != null && cartridges != null)
            {
                repository.open();
                process(repository, models, cartridges);
                repository.close();
                repository = null;
            }
        }
        finally
        {
            // log all the error messages
            Collection messages = MetafacadeFactory.getInstance()
                .getValidationMessages();
            StringBuffer totalMessagesMessage = new StringBuffer();
            if (messages != null && !messages.isEmpty())
            {
                totalMessagesMessage.append(" - ");
                totalMessagesMessage.append(messages.size());
                totalMessagesMessage.append(" VALIDATION ERROR(S)");
                messages = this.sortValidationMessages(messages);
                AndroMDALogger.setSuffix("VALIDATION:ERROR");
                Iterator messageIt = messages.iterator();
                for (int ctr = 1; messageIt.hasNext(); ctr++)
                {
                    ModelValidationMessage message = (ModelValidationMessage)messageIt
                        .next();
                    AndroMDALogger.error(ctr + ") " + message);
                }
                AndroMDALogger.reset();
            }
            AndroMDALogger.info("completed model processing, TIME --> "
                + ((System.currentTimeMillis() - startTime) / 1000.0) + "[s]"
                + totalMessagesMessage);
        }
    }

    /**
     * Processes multiple <code>models</code>.
     * 
     * @param repository the RepositoryFacade that will be used to read/load the
     *        model
     * @param models the Model(s) to process.
     * @param cartridges the collection of cartridge used to process the models.
     */
    private void process(
        RepositoryFacade repository,
        Model[] models,
        Collection cartridges)
    {
        final String methodName = "ModelProcessor.process";
        try
        {
            boolean lastModifiedCheck = true;
            long lastModified = 0;
            ModelPackages modelPackages = new ModelPackages();
            modelPackages.setProcessAllPackages(this.processAllModelPackages);

            for (int ctr = 0; ctr < models.length; ctr++)
            {
                Model model = models[ctr];
                AndroMDALogger.info("Input model --> '" + model.getUrl() + "'");
                // read the model into the repository
                repository.readModel(model.getUrl(), model
                    .getModuleSearchPath());
                // @todo lastModifiedDate needs to be handled correctly
                // for multiple models currently if one is set to false,
                // all will be false
                lastModifiedCheck = model.isLastModifiedCheck()
                    && lastModifiedCheck;
                // we go off the model that was most recently modified.
                if (model.getLastModified() > lastModified)
                {
                    lastModified = model.getLastModified();
                }
                modelPackages.addPackages(model.getPackages());
            }

            CodeGenerationContext context = new CodeGenerationContext(
                repository,
                lastModified,
                lastModifiedCheck,
                modelPackages);

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
                + " with model(s) --> '" + StringUtils.join(models, ",") + "'";
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

    /**
     * Sets whether or not AndroMDA should process all packages. If this is set
     * to true, then package elements should be specified if you want to keep
     * certain packages from being processed. If this is set to false, then you
     * would want to define package elements to specify which packages
     * <strong>SHOULD BE </strong> processed. This is useful if you need to
     * reference model elements from other packages but you don't want to
     * perform any generation from them. The default is <strong>true </strong>.
     * 
     * @param processAllModelPackages The processAllModelPackages to set.
     */
    public void setProcessAllModelPackages(boolean processAllModelPackages)
    {
        this.processAllModelPackages = processAllModelPackages;
    }

    /**
     * Sets <code>modelValidation</code> to be true/false. This defines
     * whether model validation should occur when AndroMDA processes model(s).
     * 
     * @param modelValidation true/false on whether model validation should be
     *        performed or not.
     * @see org.andromda.core.metafacade.MetafacadeFactory#setModelValidation(boolean)
     */
    public void setModelValidation(boolean modelValidation)
    {
        MetafacadeFactory.getInstance().setModelValidation(modelValidation);
    }

    /**
     * Sorts the validation <code>messages</code> first by type (i.e. the
     * metafacade class) and then by the <code>name</code> of the model
     * element to which the validation message applies.
     * 
     * @param messages the collection of messages to sort.
     * @return the sorted <code>messages</code> collection.
     */
    protected Collection sortValidationMessages(Collection messages)
    {
        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(new ValidationMessageTypeComparator());
        chain.addComparator(new ValidationMessageNameComparator());
        messages = new ArrayList(messages);
        Collections.sort((List)messages, chain);
        return messages;
    }

    /**
     * Used to sort validation messages by <code>metafacadeClass</code>.
     */
    private final static class ValidationMessageTypeComparator
        implements Comparator
    {
        private final Collator collator = Collator.getInstance();

        private ValidationMessageTypeComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        public int compare(Object objectA, Object objectB)
        {
            ModelValidationMessage a = (ModelValidationMessage)objectA;
            ModelValidationMessage b = (ModelValidationMessage)objectB;

            return collator.compare(a.getMetafacadeClass().getName(), b
                .getMetafacadeClass().getName());
        }
    }

    /**
     * Used to sort validation messages by <code>modelElementName</code>.
     */
    private final static class ValidationMessageNameComparator
        implements Comparator
    {
        private final Collator collator = Collator.getInstance();

        private ValidationMessageNameComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        public int compare(Object objectA, Object objectB)
        {
            ModelValidationMessage a = (ModelValidationMessage)objectA;
            ModelValidationMessage b = (ModelValidationMessage)objectB;

            return collator.compare(StringUtils.trimToEmpty(a
                .getModelElementName()), StringUtils.trimToEmpty(b
                .getModelElementName()));
        }
    }
}