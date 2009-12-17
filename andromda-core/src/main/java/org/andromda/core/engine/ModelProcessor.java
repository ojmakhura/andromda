package org.andromda.core.engine;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.andromda.core.ModelValidationException;
import org.andromda.core.cartridge.Cartridge;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.BuildInformation;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionRecorder;
import org.andromda.core.common.Introspector;
import org.andromda.core.common.ResourceWriter;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.configuration.Filters;
import org.andromda.core.configuration.Model;
import org.andromda.core.configuration.Namespace;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;
import org.andromda.core.configuration.Repository;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.core.metafacade.ModelValidationMessage;
import org.andromda.core.namespace.NamespaceComponents;
import org.andromda.core.repository.Repositories;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>
 * Handles the processing of models. Facilitates Model Driven
 * Architecture by enabling the generation of source code, configuration files, and other such artifacts from a single
 * or multiple models. </p>
 *
 * @author Chad Brandon
 * @author Bob Fields
 * @author Michail Plushnikov
 */
public class ModelProcessor
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(ModelProcessor.class);

    /**
     * Creates a new instance the ModelProcessor.
     *
     * @return the shared ModelProcessor instance.
     */
    public static ModelProcessor newInstance()
    {
        return new ModelProcessor();
    }

    private ModelProcessor()
    {
        // - do not allow instantiation
    }

    /**
     * Re-configures this model processor from the given <code>configuration</code>
     * instance (if different from that of the one passed in during the call to
     * {@link #initialize(Configuration)}), and runs the model processor.
     *
     * @param configuration the configuration from which to configure this model
     *        processor instance.
     * @return any model validation messages collected during model processing (if
     *         model validation is enabled).
     */
    public ModelValidationMessage[] process(final Configuration configuration)
    {
        this.configure(configuration);
        final List<ModelValidationMessage> messages = this.process(configuration.getRepositories());
        return messages != null ? messages.toArray(new ModelValidationMessage[messages.size()])
                                : new ModelValidationMessage[0];
    }

    /**
     * Configures (or re-configures) the model processor if configuration
     * is required (the configuration has changed since the previous, or has
     * yet to be used).
     *
     * @param configuration the AndroMDA configuration instance.
     */
    private void configure(final Configuration configuration)
    {
        if (this.requiresConfiguration(configuration))
        {
            configuration.initialize();
            this.reset();
            final Property[] properties = configuration.getProperties();
            final Introspector introspector = Introspector.instance();
            for (Property property : properties)
            {
                try
                {
                    introspector.setProperty(
                        this,
                        property.getName(),
                        property.getValue());
                }
                catch (final Throwable throwable)
                {
                    AndroMDALogger.warn(
                        "Could not set model processor property '" + property.getName() + "' with a value of '" +
                        property.getValue() + '\'');
                }
            }
            this.currentConfiguration = configuration;
        }
    }

    /**
     * Processes all models contained within the <code>repositories</code>
     * with the discovered cartridges.
     *
     * @return any model validation messages that may have been collected during model loading/validation.
     */
    private List<ModelValidationMessage> process(final org.andromda.core.configuration.Repository[] repositories)
    {
        List<ModelValidationMessage> messages = null;
        final long startTime = System.currentTimeMillis();
        for (Repository repository : repositories)
        {
            if (repository != null)
            {
                final String repositoryName = repository.getName();

                // - filter out any invalid models (ones that don't have any uris defined)
                final Model[] models = this.filterInvalidModels(repository.getModels());
                if (models.length > 0)
                {
                    messages = this.processModels(
                            repositoryName,
                            models);
                    AndroMDALogger.info(
                        "completed model processing --> TIME: " + this.getDurationInSeconds(startTime) +
                        "[s], RESOURCES WRITTEN: " + ResourceWriter.instance().getWrittenCount());
                }
                else
                {
                    AndroMDALogger.warn("No model(s) found to process for repository '" + repositoryName + '\'');
                }
            }
        }
        if(messages == null)
        {
            messages = Collections.emptyList();
        }
        return messages;
    }

    /**
     * The shared metafacade factory instance.
     */
    private final MetafacadeFactory factory = MetafacadeFactory.getInstance();

    /**
     * The shared namespaces instance.
     */
    private final Namespaces namespaces = Namespaces.instance();

    /**
     * The shared repositories instance.
     */
    private final Repositories repositories = Repositories.instance();

    /**
     * Processes multiple <code>models</code>.
     *
     * @param repositoryName the name of the repository that loads/reads the model.
     * @param models the Model(s) to process.
     * @return any model validation messages that may have been collected during validation/loading of
     *         the <code>models</code>.
     */
    private List<ModelValidationMessage> processModels(
        final String repositoryName,
        final Model[] models)
    {
        List<ModelValidationMessage> messages = null;
        String cartridgeName = null;
        try
        {
            boolean lastModifiedCheck = true;
            long lastModified = 0;
            final ResourceWriter writer = ResourceWriter.instance();

            // - get the time from the model that has the latest modified time
            for (Model model : models)
            {
                writer.resetHistory(model.getUris()[0]);
                lastModifiedCheck = model.isLastModifiedCheck() && lastModifiedCheck;

                // - we go off the model that was most recently modified.
                if (model.getLastModified() > lastModified)
                {
                    lastModified = model.getLastModified();
                }
            }

            if (!lastModifiedCheck || writer.isHistoryBefore(lastModified))
            {
                final Collection<Cartridge> cartridges = ComponentContainer.instance().findComponentsOfType(Cartridge.class);
                if (cartridges.isEmpty())
                {
                    AndroMDALogger.warn("WARNING! No cartridges found, check your classpath!");
                }
                
                final Map<String, Cartridge> cartridgesByNamespace = this.loadCartridgesByNamespace(cartridges);
                
                // - we want to process by namespace so that the order within the configuration is kept
                final Collection<Namespace> namespaces = this.namespaces.getNamespaces();

                // - pre-load the models
                messages = this.loadIfNecessary(models);
                for (Namespace namespace : namespaces)
                {
                    final Cartridge cartridge = cartridgesByNamespace.get(namespace.getName());
                    if (cartridge != null)
                    {
                        cartridgeName = cartridge.getNamespace();
                        if (this.shouldProcess(cartridgeName))
                        {
                             // - set the active namespace on the shared factory and profile instances
                            this.factory.setNamespace(cartridgeName);
                            cartridge.initialize();
    
                            // - process each model with the cartridge
                            for (Model model : models)
                            {
                                AndroMDALogger.info("Processing cartridge " + cartridge.getNamespace() + " on model " + model);
   
                                // - set the namespace on the metafacades instance so we know the 
                                //   correct facades to use
                                this.factory.setModel(
                                    this.repositories.getImplementation(repositoryName).getModel(),
                                    model.getType());
                                cartridge.processModelElements(this.factory);
                                writer.writeHistory();
                            }
                            cartridge.shutdown();
                        }
                    }
                }
            }
        }
        catch (final ModelValidationException exception)
        {
            // - we don't want to record model validation exceptions
            throw exception;
        }
        catch (final Throwable throwable)
        {
            final String messsage =
                "Error performing ModelProcessor.process with model(s) --> '" + StringUtils.join(
                    models,
                    ",") + '\'';
            logger.error(messsage);
            ExceptionRecorder.instance().record(
                messsage,
                throwable,
                cartridgeName);
            throw new ModelProcessorException(messsage, throwable);
        }
        if(messages == null)
        {
            messages = Collections.emptyList();
        }
        return messages;
    }
    
    /**
     * Loads the given list of <code>cartridges</code> into a map keyed by namespace.
     * 
     * @param cartridges the cartridges loaded.
     * @return the loaded cartridge map.
     */
    private Map<String, Cartridge> loadCartridgesByNamespace(final Collection<Cartridge> cartridges)
    {
        final Map<String, Cartridge> cartridgesByNamespace = new LinkedHashMap<String, Cartridge>();
        for (Cartridge cartridge : cartridges)
        {
            cartridgesByNamespace.put(cartridge.getNamespace(), cartridge);
        }
        return cartridgesByNamespace;
    }

    /**
     * Initializes this model processor instance with the given
     * configuration.  This configuration information is overridden (if changed)
     * when calling {@link #process(Configuration)}
     *
     * @param configuration the configuration instance by which to initialize this
     *        model processor instance.
     */
    public void initialize(final Configuration configuration)
    {
        final long startTime = System.currentTimeMillis();

        // - first, print the AndroMDA header
        this.printConsoleHeader();

        // - second, configure this model processor 
        // - the ordering of this step is important: it needs to occur
        //   before everything else in the framework is initialized so that 
        //   we have all configuration information available (such as the
        //   namespace properties)
        this.configure(configuration);

        // - the logger configuration may have changed - re-init the logger.
        AndroMDALogger.initialize();

        // - discover all namespace components
        NamespaceComponents.instance().discover();

        // - find and initialize any repositories
        repositories.initialize();

        // - finally initialize the metafacade factory
        this.factory.initialize();
        this.printWorkCompleteMessage(
            "core initialization",
            startTime);
    }

    /**
     * Loads the model into the repository only when necessary (the model has a timestamp
     * later than the last timestamp of the loaded model).
     *
     * @param model the model to be loaded.
     * @return List validation messages
     */
    protected final List<ModelValidationMessage> loadModelIfNecessary(final Model model)
    {
        final List<ModelValidationMessage> validationMessages = new ArrayList<ModelValidationMessage>();
        final long startTime = System.currentTimeMillis();
        if (this.repositories.loadModel(model))
        {
            this.printWorkCompleteMessage(
                "loading",
                startTime);

            // - validate the model since loading has successfully occurred
            final Repository repository = model.getRepository();
            final String repositoryName = repository != null ? repository.getName() : null;
            validationMessages.addAll(this.validateModel(
                    repositoryName,
                    model));
        }
        return validationMessages;
    }

    /**
     * Validates the entire model with each cartridge namespace,
     * and returns any validation messages that occurred during validation
     * (also logs any validation failures).
     *
     * @param repositoryName the name of the repository storing the model to validate.
     * @param model the model to validate
     * @return any {@link ModelValidationMessage} instances that may have been collected
     *         during validation.
     */
    private List<ModelValidationMessage> validateModel(
        final String repositoryName,
        final Model model)
    {
        final Filters constraints = model != null ? model.getConstraints() : null;
        final List<ModelValidationMessage> validationMessages = new ArrayList<ModelValidationMessage>();
        if (ModelProcessor.modelValidation && model != null)
        {
            final long startTime = System.currentTimeMillis();
            AndroMDALogger.info("- validating model -");
            final Collection<Cartridge> cartridges = ComponentContainer.instance().findComponentsOfType(Cartridge.class);
            final ModelAccessFacade modelAccessFacade =
                this.repositories.getImplementation(repositoryName).getModel();

            // - clear out the factory's caches (such as any previous validation messages, etc.)
            this.factory.clearCaches();
            this.factory.setModel(
                modelAccessFacade,
                model.getType());
            for (Cartridge cartridge : cartridges)
            {
                final String cartridgeName = cartridge.getNamespace();
                if (this.shouldProcess(cartridgeName))
                {
                    // - set the active namespace on the shared factory and profile instances
                    this.factory.setNamespace(cartridgeName);
                    this.factory.validateAllMetafacades();
                }
            }
            final List<ModelValidationMessage> messages = this.factory.getValidationMessages();
            this.filterAndSortValidationMessages(
                messages,
                constraints);
            this.printValidationMessages(messages);
            this.printWorkCompleteMessage(
                "validation",
                startTime);
            if (messages != null && !messages.isEmpty())
            {
                validationMessages.addAll(messages);
            }
        }
        return validationMessages;
    }

    /**
     * Prints a work complete message using the type of <code>unitOfWork</code> and
     * <code>startTime</code> as input.
     * @param unitOfWork the type of unit of work that was completed
     * @param startTime the time the unit of work was started.
     */
    private void printWorkCompleteMessage(
        final String unitOfWork,
        final long startTime)
    {
        AndroMDALogger.info("- " + unitOfWork + " complete: " + this.getDurationInSeconds(startTime) + "[s] -");
    }

    /**
     * Calculates the duration in seconds between the
     * given <code>startTime</code> and the current time.
     * @param startTime the time to compare against.
     * @return the duration of time in seconds.
     */
    private double getDurationInSeconds(final long startTime)
    {
        return ((System.currentTimeMillis() - startTime) / 1000.0);
    }

    /**
     * Prints any model validation errors stored within the <code>factory</code>.
     */
    private void printValidationMessages(final List<ModelValidationMessage> messages)
    {
        // - log all error messages
        if (messages != null && !messages.isEmpty())
        {
            final StringBuffer header =
                new StringBuffer("Model Validation Failed - " + messages.size() + " VALIDATION ERROR");
            if (messages.size() > 1)
            {
                header.append('S');
            }
            AndroMDALogger.error(header);
            int ctr = 1;
            for (ModelValidationMessage message : messages)
            {
                AndroMDALogger.error(ctr + ") " + message);
                ctr++;
            }
            AndroMDALogger.reset();
            if (this.failOnValidationErrors)
            {
                throw new ModelValidationException("Model validation failed!");
            }
        }
    }

    /**
     * The current configuration of this model processor.
     */
    private Configuration currentConfiguration;

    /**
     * Determines whether or not this model processor needs to be reconfigured.
     * This is based on whether or not the new configuration is different
     * than the <code>currentConfiguration</code>.  We determine this checking
     * if their contents are equal or not, if not equal this method will
     * return true, otherwise false.
     *
     * @param configuration the configuration to compare to the lastConfiguration.
     * @return true/false
     */
    private boolean requiresConfiguration(final Configuration configuration)
    {
        boolean requiresConfiguration =
            this.currentConfiguration == null || this.currentConfiguration.getContents() == null ||
            configuration.getContents() == null;
        if (!requiresConfiguration)
        {
            requiresConfiguration = !this.currentConfiguration.getContents().equals(configuration.getContents());
        }
        return requiresConfiguration;
    }

    /**
     * Checks to see if <em>any</em> of the repositories contain models
     * that need to be reloaded, and if so, re-loads them.
     *
     * @param repositories the repositories from which to load the model(s).
     * @return messages
     */
    final List<ModelValidationMessage> loadIfNecessary(final org.andromda.core.configuration.Repository[] repositories)
    {
        final List<ModelValidationMessage> messages = new ArrayList<ModelValidationMessage>();
        if (repositories != null)
        {
            for (Repository repository : repositories)
            {
                if (repository != null)
                {
                    messages.addAll(this.loadIfNecessary(repository.getModels()));
                }
            }
        }
        return messages;
    }

    /**
     * Checks to see if <em>any</em> of the models need to be reloaded, and if so, re-loads them.
     *
     * @param models that will be loaded (if necessary).
     * @return any validation messages collected during loading.
     */
    private List<ModelValidationMessage> loadIfNecessary(final Model[] models)
    {
        final List<ModelValidationMessage> messages = new ArrayList<ModelValidationMessage>();
        if (models != null && models.length > 0)
        {
            for (Model model : models)
            {
                messages.addAll(this.loadModelIfNecessary(model));
            }
        }
        return messages;
    }

    /**
     * Stores the current version of AndroMDA.
     */
    private static final String VERSION = BuildInformation.instance().getBuildVersion();

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
     * Whether or not model validation should be performed.
     */
    private static boolean modelValidation = true;

    /**
     * Sets whether or not model validation should occur. This is useful for
     * performance reasons (i.e. if you have a large model it can significantly decrease the amount of time it takes for
     * AndroMDA to process a model). By default this is set to <code>true</code>.
     *
     * @param modelValidationIn true/false on whether model validation should be performed or not.
     */
    public void setModelValidation(final boolean modelValidationIn)
    {
        ModelProcessor.modelValidation = modelValidationIn;
    }

    /**
     * Gets whether or not model validation should occur. This is useful for
     * performance reasons (i.e. if you have a large model it can significantly decrease the amount of time it takes for
     * AndroMDA to process a model). By default this is set to <code>true</code>.
     *
     * @return modelValidation true/false on whether model validation should be performed or not.
     */
    public static boolean getModelValidation()
    {
        return ModelProcessor.modelValidation;
    }

    /**
     * A flag indicating whether or not failure should occur
     * when model validation errors are present.
     */
    private boolean failOnValidationErrors = true;

    /**
     * Sets whether or not processing should fail when validation errors occur, default is <code>true</code>.
     *
     * @param failOnValidationErrors whether or not processing should fail if any validation errors are present.
     */
    public void setFailOnValidationErrors(final boolean failOnValidationErrors)
    {
        this.failOnValidationErrors = failOnValidationErrors;
    }

    /**
     * Stores the cartridge filter.
     */
    private List cartridgeFilter;

    /**
     * Denotes whether or not the complement of filtered cartridges should be processed
     */
    private boolean negateCartridgeFilter;

    /**
     * Indicates whether or not the <code>namespace</code> should be processed. This is determined in conjunction with
     * {@link #setCartridgeFilter(String)}. If the <code>cartridgeFilter</code> is not defined and the namespace is
     * present within the configuration, then this method will <strong>ALWAYS </strong> return true.
     *
     * @param namespace the name of the namespace to check whether or not it should be processed.
     * @return true/false on whether or not it should be processed.
     */
    protected boolean shouldProcess(final String namespace)
    {
        boolean shouldProcess = this.namespaces.namespacePresent(namespace);
        if (shouldProcess)
        {
            shouldProcess = this.cartridgeFilter == null || this.cartridgeFilter.isEmpty();
            if (!shouldProcess)
            {
                shouldProcess =
                    this.negateCartridgeFilter ^ this.cartridgeFilter.contains(StringUtils.trimToEmpty(namespace));
            }
        }
        return shouldProcess;
    }

    /**
     * The prefix used for cartridge filter negation.
     */
    private static final String CARTRIDGE_FILTER_NEGATOR = "~";

    /**
     * <p/>
     * Sets the current cartridge filter. This is a comma separated list of namespaces (matching cartridges names) that
     * should be processed. </p>
     * <p/>
     * If this filter is defined, then any cartridge names found in this list <strong>will be processed </strong>, while
     * any other discovered cartridges <strong>will not be processed </strong>. </p>
     *
     * @param namespaces a comma separated list of the cartridge namespaces to be processed.
     */
    public void setCartridgeFilter(String namespaces)
    {
        if (namespaces != null)
        {
            namespaces = StringUtils.deleteWhitespace(namespaces);
            if (namespaces.startsWith(CARTRIDGE_FILTER_NEGATOR))
            {
                this.negateCartridgeFilter = true;
                namespaces = namespaces.substring(1);
            }
            else
            {
                this.negateCartridgeFilter = false;
            }
            if (StringUtils.isNotBlank(namespaces))
            {
                this.cartridgeFilter = Arrays.asList(namespaces.split(","));
            }
        }
    }

    /**
     * Sets the encoding (UTF-8, ISO-8859-1, etc) for all output
     * produced during model processing.
     *
     * @param outputEncoding the encoding.
     */
    public void setOutputEncoding(final String outputEncoding)
    {
        ResourceWriter.instance().setEncoding(outputEncoding);
    }

    /**
     * Sets <code>xmlValidation</code> to be true/false. This defines whether XML resources loaded by AndroMDA (such as
     * plugin descriptors) should be validated. Sometimes underlying parsers don't support XML Schema validation and in
     * that case, we want to be able to turn it off.
     *
     * @param xmlValidation true/false on whether we should validate XML resources used by AndroMDA
     */
    public void setXmlValidation(final boolean xmlValidation)
    {
        XmlObjectFactory.setDefaultValidating(xmlValidation);
    }

    /**
     * <p/>
     * Sets the <code>loggingConfigurationUri</code> for AndroMDA. This is the URI to an external logging configuration
     * file. This is useful when you want to override the default logging configuration of AndroMDA. </p>
     * <p/>
     * You can retrieve the default log4j.xml contained within the {@link org.andromda.core.common}package, customize
     * it, and then specify the location of this logging file with this operation. </p>
     *
     * @param loggingConfigurationUri the URI to the external logging configuation file.
     */
    public void setLoggingConfigurationUri(final String loggingConfigurationUri)
    {
        AndroMDALogger.setLoggingConfigurationUri(loggingConfigurationUri);
    }

    /**
     * Filters out any <em>invalid</em> models. This means models that either are null within the specified
     * <code>models</code> array or those that don't have URLs set.
     *
     * @param models the models to filter.
     * @return the array of valid models
     */
    private Model[] filterInvalidModels(final Model[] models)
    {
        final Collection<Model> validModels = new ArrayList<Model>(Arrays.asList(models));
        CollectionUtils.filter(validModels, new Predicate() {
            public boolean evaluate(Object o) {
                final Model model = (Model)o;
                return model != null && model.getUris() != null && model.getUris().length > 0;
            }
        });

        return validModels.toArray(new Model[validModels.size()]);
    }

    /**
     * Shuts down the model processor (reclaims any
     * resources).
     */
    public void shutdown()
    {
        // - shutdown the metafacade factory instance
        this.factory.shutdown();

        // - shutdown the configuration namespaces instance
        this.namespaces.clear();

        // - shutdown the container instance
        ComponentContainer.instance().shutdown();

        // - shutdown the namespace components registry
        NamespaceComponents.instance().shutdown();

        // - shutdown the introspector
        Introspector.instance().shutdown();

        // - clear out any caches used by the configuration
        Configuration.clearCaches();

        // - clear out any repositories
        this.repositories.clear();
    }

    /**
     * Reinitializes the model processor's resources.
     */
    private void reset()
    {
        this.factory.reset();
        this.cartridgeFilter = null;
        this.setXmlValidation(true);
        this.setOutputEncoding(null);
        this.setModelValidation(true);
        this.setLoggingConfigurationUri(null);
        this.setFailOnValidationErrors(true);
    }

    /**
     * Filters out any messages that should not be applied according to the AndroMDA configuration's
     * constraints and sorts the resulting <code>messages</code> first by type (i.e. the metafacade class)
     * and then by the <code>name</code> of the model element to which the validation message applies.
     *
     * @param messages the collection of messages to sort.
     * @param constraints any constraint filters to apply to the validation messages.
     */
    protected void filterAndSortValidationMessages(
        final List<ModelValidationMessage> messages,
        final Filters constraints)
    {
        if (constraints != null)
        {
            // - perform constraint filtering (if any applies)
            CollectionUtils.filter(messages, new Predicate() {
                public boolean evaluate(Object o) {
                    ModelValidationMessage message = (ModelValidationMessage)o;
                    return constraints.isApply(message.getName());
                }
            });
        }

        if (messages != null && !messages.isEmpty())
        {
            final ComparatorChain chain = new ComparatorChain();
            chain.addComparator(new ValidationMessageTypeComparator());
            chain.addComparator(new ValidationMessageNameComparator());
            Collections.sort(
                messages,
                chain);
        }
    }

    /**
     * Used to sort validation messages by <code>metafacadeClass</code>.
     */
    private static final class ValidationMessageTypeComparator
        implements Comparator<ModelValidationMessage>
    {
        private final Collator collator = Collator.getInstance();

        ValidationMessageTypeComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        public int compare(
            final ModelValidationMessage objectA,
            final ModelValidationMessage objectB)
        {            
            return collator.compare(
                objectA.getMetafacadeClass().getName(),
                objectB.getMetafacadeClass().getName());
        }
    }

    /**
     * Used to sort validation messages by <code>modelElementName</code>.
     */
    private static final class ValidationMessageNameComparator
        implements Comparator<ModelValidationMessage>
    {
        private final Collator collator = Collator.getInstance();

        ValidationMessageNameComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        public int compare(
            final ModelValidationMessage objectA,
            final ModelValidationMessage objectB)
        {
            return collator.compare(
                StringUtils.trimToEmpty(objectA.getMetafacadeName()),
                StringUtils.trimToEmpty(objectB.getMetafacadeName()));
        }
    }
}