package org.andromda.core.cartridge;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.andromda.core.cartridge.template.ModelElement;
import org.andromda.core.cartridge.template.ModelElements;
import org.andromda.core.cartridge.template.Template;
import org.andromda.core.cartridge.template.Type;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.BasePlugin;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Introspector;
import org.andromda.core.common.PathMatcher;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.ResourceWriter;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;


/**
 * The AndroMDA Cartridge implementation of the Plugin. Cartridge instances are configured from
 * <code>META-INF/andromda/cartridge.xml</code> files discovered on the classpath.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 * @author Bob Fields
 */
public class Cartridge
    extends BasePlugin
{
    /** The logger instance. */
    private static final Logger logger = Logger.getLogger(Cartridge.class);

    /**
     * Processes all model elements with relevant stereotypes by retrieving the model elements from the model facade
     * contained within the context.
     *
     * @param factory the metafacade factory (which is used to manage the lifecycle of metafacades).
     */
    public void processModelElements(final MetafacadeFactory factory)
    {
        ExceptionUtils.checkNull(
            "factory",
            factory);
        final Collection<Resource> resources = this.getResources();
        if (resources != null && !resources.isEmpty())
        {
            for (final Iterator<Resource> iterator = resources.iterator(); iterator.hasNext();)
            {
                final Resource resource = (Resource)iterator.next();
                if (resource instanceof Template)
                {
                    this.processTemplate(
                        factory,
                        (Template)resource);
                }
                else
                {
                    this.processResource(resource);
                }
            }
        }
    }

    /**
     * Processes the given <code>template</code>.
     *
     * @param factory the metafacade factory instance.
     * @param template the Template instance to process.
     */
    protected void processTemplate(
        final MetafacadeFactory factory,
        final Template template)
    {
        ExceptionUtils.checkNull(
            "template",
            template);
        final ModelElements templateModelElements = template.getSupportedModeElements();

        // - handle the templates WITH model elements
        if (templateModelElements != null && !templateModelElements.isEmpty())
        {
            for (final Iterator iterator = templateModelElements.getModelElements().iterator(); iterator.hasNext();)
            {
                final ModelElement templateModelElement = (ModelElement)iterator.next();

                // - if the template model element has a stereotype
                //   defined, then we filter the metafacades based
                //   on that stereotype, otherwise we get all metafacades
                //   and let the templateModelElement perform filtering on the
                //   metafacades by type and properties
                if (templateModelElement.hasStereotype())
                {
                    templateModelElement.setMetafacades(
                        factory.getMetafacadesByStereotype(templateModelElement.getStereotype()));
                }
                else if (templateModelElement.hasTypes())
                {
                    templateModelElement.setMetafacades(factory.getAllMetafacades());
                }
            }
            this.processTemplateWithMetafacades(
                factory,
                template);
        }
        else
        {
            // - handle any templates WITHOUT metafacades.
            this.processTemplateWithoutMetafacades(template);
        }
    }

    /**
     * Processes all <code>modelElements</code> for this template.
     *
     * @param factory the metafacade factory
     * @param template the template to process
     */
    protected void processTemplateWithMetafacades(
        final MetafacadeFactory factory,
        final Template template)
    {
        ExceptionUtils.checkNull(
            "template",
            template);
        final ModelElements modelElements = template.getSupportedModeElements();
        if (modelElements != null && !modelElements.isEmpty())
        {
            try
            {
                final Collection allMetafacades = modelElements.getAllMetafacades();
                // Tell us which template is processed against how many metafacade elements
                logger.info("Processing " + template.getPath() + " with " + allMetafacades.size() + " metafacades from " + modelElements.getModelElements().size() + " model elements");

                // - if outputToSingleFile is true AND outputOnEmptyElements
                //   is true or we have at least one metafacade in the
                //   allMetafacades collection, then we collect the template
                //   model elements and place them into the template context
                //   by their variable names.
                if (template.isOutputToSingleFile() &&
                    (template.isOutputOnEmptyElements() || !allMetafacades.isEmpty()))
                {
                    final Map templateContext = new LinkedHashMap();

                    // - first place all relevant model elements by the
                    //   <modelElements/> variable name. If the variable
                    //   isn't defined (which is possible), ignore.
                    final String modelElementsVariable = modelElements.getVariable();
                    if (modelElementsVariable != null && modelElementsVariable.trim().length() > 0)
                    {
                        templateContext.put(
                            modelElements.getVariable(),
                            allMetafacades);
                    }

                    // - now place the collections of elements by the given variable names. 
                    //   (skip if the variable is NOT defined)
                    for (final Iterator iterator = modelElements.getModelElements().iterator(); iterator.hasNext();)
                    {
                        final ModelElement modelElement = (ModelElement)iterator.next();
                        final String modelElementVariable = modelElement.getVariable();
                        if (modelElementVariable != null && modelElementVariable.trim().length() > 0)
                        {
                            // - if a modelElement has the same variable defined
                            //   more than one time, then get the existing
                            //   model elements added from the last iteration
                            //   and add the new ones to that collection
                            Collection metafacades = (Collection)templateContext.get(modelElementVariable);
                            if (metafacades != null)
                            {
                                metafacades.addAll(modelElement.getMetafacades());
                            }
                            else
                            {
                                metafacades = modelElement.getMetafacades();
                                templateContext.put(
                                    modelElementVariable,
                                    new LinkedHashSet(metafacades));
                            }
                        }
                    }
                    this.processWithTemplate(
                        template,
                        templateContext,
                        null,
                        null);
                }
                else
                {
                    // - if outputToSingleFile isn't true, then
                    //   we just place the model element with the default
                    //   variable defined on the <modelElements/> into the
                    //   template.
                    for (final Iterator iterator = allMetafacades.iterator(); iterator.hasNext();)
                    {
                        final Map templateContext = new LinkedHashMap();
                        final Object metafacade = iterator.next();
                        final ModelAccessFacade model = factory.getModel();
                        for (final Iterator elements = modelElements.getModelElements().iterator(); elements.hasNext();)
                        {
                            final ModelElement modelElement = (ModelElement)elements.next();
                            String variable = modelElement.getVariable();

                            // - if the variable isn't defined on the <modelElement/>, try
                            //   the <modelElements/>
                            if (variable == null || variable.trim().length() == 0)
                            {
                                variable = modelElements.getVariable();
                            }

                            // - only add the metafacade to the template context if the variable
                            //   is defined (which is possible)
                            if (variable != null && variable.trim().length() > 0)
                            {
                                templateContext.put(
                                    variable,
                                    metafacade);
                            }

                            // - now we process any property templates (if any 'variable' attributes are defined on one or
                            //   more type's given properties), otherwise we process the single metafacade as usual
                            if (!this.processPropertyTemplates(
                                    template,
                                    metafacade,
                                    templateContext,
                                    modelElement))
                            {
                                this.processWithTemplate(
                                    template,
                                    templateContext,
                                    model.getName(metafacade),
                                    model.getPackageName(metafacade));
                            }
                        }
                    }
                }
            }
            catch (final Throwable throwable)
            {
                throw new CartridgeException(throwable);
            }
        }
    }

    /**
     * Determines if any property templates need to be processed (that is templates
     * that are processed given related <em>properties</em> of a metafacade).
     *
     * @param template the template to use for processing.
     * @param metafacade the metafacade instance (the property value is retrieved from this).
     * @param templateContext the template context containing the instance to pass to the template.
     * @param modelElement the model element from which we retrieve the corresponding types and then
     *        properties to determine if any properties have been mapped for template processing.
     * @return true if any property templates have been evaluated (false otherwise).
     */
    private boolean processPropertyTemplates(
        final Template template,
        final Object metafacade,
        final Map templateContext,
        final ModelElement modelElement)
    {
        boolean propertyTemplatesEvaluated = false;
        for (final Iterator types = modelElement.getTypes().iterator(); types.hasNext();)
        {
            final Type type = (Type)types.next();
            for (final Iterator properties = type.getProperties().iterator(); properties.hasNext();)
            {
                final Type.Property property = (Type.Property)properties.next();
                final String variable = property.getVariable();
                propertyTemplatesEvaluated = variable != null && variable.trim().length() > 0;
                if (propertyTemplatesEvaluated)
                {
                    final Object value = Introspector.instance().getProperty(
                            metafacade,
                            property.getName());
                    if (value instanceof Collection)
                    {
                        for (final Iterator values = ((Collection)value).iterator(); values.hasNext();)
                        {
                            templateContext.put(
                                variable,
                                values.next());
                            this.processWithTemplate(
                                template,
                                templateContext,
                                null,
                                null);
                        }
                    }
                    else
                    {
                        templateContext.put(
                            variable,
                            value);
                        this.processWithTemplate(
                            template,
                            templateContext,
                            null,
                            null);
                    }
                }
            }
        }
        return propertyTemplatesEvaluated;
    }

    /**
     * Processes the <code>template</code> without metafacades. This is useful if you need to generate something that
     * is part of your cartridge, however you only need to use a property passed in from a namespace or a template
     * object defined in your cartridge descriptor.
     *
     * @param template the template to process.
     */
    protected void processTemplateWithoutMetafacades(final Template template)
    {
        ExceptionUtils.checkNull(
            "template",
            template);
        final Map templateContext = new LinkedHashMap();
        this.processWithTemplate(
            template,
            templateContext,
            null,
            null);
    }

    /**
     * <p>
     * Perform processing with the <code>template</code>.
     * </p>
     *
     * @param template the Template containing the template path to process.
     * @param templateContext the context to which variables are added and made
     *        available to the template engine for processing. This will contain
     *        any model elements being made available to the template(s) as well
     *        as properties/template objects.
     * @param metafacadeName the name of the model element (if we are
     *        processing a single model element, otherwise this will be
     *        ignored).
     * @param metafacadePackage the name of the package (if we are processing
     *        a single model element, otherwise this will be ignored).
     */
    private void processWithTemplate(
        final Template template,
        final Map templateContext,
        final String metafacadeName,
        final String metafacadePackage)
    {
        ExceptionUtils.checkNull(
            "template",
            template);
        ExceptionUtils.checkNull(
            "templateContext",
            templateContext);

        File outputFile = null;
        try
        {
            // - populate the template context with cartridge descriptor
            //   properties and template objects
            this.populateTemplateContext(templateContext);

            final StringWriter output = new StringWriter();

            // - process the template with the set TemplateEngine
            this.getTemplateEngine().processTemplate(
                template.getPath(),
                templateContext,
                output);
            
            // - if we have an outputCondition defined make sure it evaluates to true before continuing
            if (this.isValidOutputCondition(template.getOutputCondition(), templateContext))
            {
                // - get the location and at the same time evaluate the outlet as a template engine variable (in case
                //   its defined as that).
                final String location =
                    Namespaces.instance().getPropertyValue(
                        this.getNamespace(),
                        this.getTemplateEngine().getEvaluatedExpression(
                            template.getOutlet(),
                            templateContext));
    
                if (location != null)
                {
                    outputFile =
                        template.getOutputLocation(
                            metafacadeName,
                            metafacadePackage,
                            new File(location),
                            this.getTemplateEngine().getEvaluatedExpression(
                                template.getOutputPattern(),
                                templateContext));
                    if (outputFile != null)
                    {
                        // - only write files that do NOT exist, and
                        //   those that have overwrite set to 'true'
                        if (!outputFile.exists() || template.isOverwrite())
                        {
                            final String outputString = output.toString();
                            AndroMDALogger.setSuffix(this.getNamespace());
    
                            // - check to see if generateEmptyFiles is true and if
                            //   outString is not blank
                            if ((outputString != null && outputString.trim().length() > 0) ||
                                template.isGenerateEmptyFiles())
                            {
                                ResourceWriter.instance().writeStringToFile(
                                    outputString,
                                    outputFile,
                                    this.getNamespace());
                                AndroMDALogger.info("Output: '" + outputFile.toURI() + "'");
                            }
                            else
                            {
                                if (this.getLogger().isDebugEnabled())
                                {
                                    this.getLogger().debug("Empty Output: '" + outputFile.toURI() + "' --> not writing");
                                }
                            }
                            AndroMDALogger.reset();
                        }
                    }
                }
            }
        }
        catch (final Throwable throwable)
        {
            if (outputFile != null)
            {
                outputFile.delete();
                this.getLogger().info("Removed: '" + outputFile + "'");
            }
            final String message =
                "Error processing template '" + template.getPath() + "' with template context '" + templateContext +
                "' using cartridge '" + this.getNamespace() + "'";
            throw new CartridgeException(message, throwable);
        }
    }

    /**
     * Processes the given <code>resource</code>
     *
     * @param resource the resource to process.
     */
    protected void processResource(final Resource resource)
    {
        ExceptionUtils.checkNull(
            "resource",
            resource);

        URL resourceUrl = ResourceUtils.getResource(
                resource.getPath(),
                this.getMergeLocation());
        if (resourceUrl == null)
        {
            // - if the resourceUrl is null, the path is probably a regular
            //   outputCondition pattern so we'll see if we can match it against
            //   the contents of the plugin and write any contents that do match
            final List contents = this.getContents();
            if (contents != null)
            {
                AndroMDALogger.setSuffix(this.getNamespace());
                for (final Iterator iterator = contents.iterator(); iterator.hasNext();)
                {
                    final String content = (String)iterator.next();
                    if (content != null && content.trim().length() > 0)
                    {
                        if (PathMatcher.wildcardMatch(
                                content,
                                resource.getPath()))
                        {
                            resourceUrl = ResourceUtils.getResource(
                                    content,
                                    this.getMergeLocation());
                            // - don't attempt to write the directories within the resource
                            if (!resourceUrl.toString().endsWith(FORWARD_SLASH))
                            {
                                this.writeResource(
                                    resource,
                                    resourceUrl);                                
                            }
                        }
                    }
                }
                AndroMDALogger.reset();
            }
        }
        else
        {
            this.writeResource(
                resource,
                resourceUrl);
        }
    }

    /**
     * The forward slash constant.
     */
    private static final String FORWARD_SLASH = "/";
    
    private static final String PATH_PATTERN = "\\*.*";

    /**
     * Writes the contents of <code>resourceUrl</code> to the outlet specified by <code>resource</code>.
     *
     * @param resource contains the outlet where the resource is written.
     * @param resourceUrl the URL contents to write.
     */
    private void writeResource(
        final Resource resource,
        final URL resourceUrl)
    {
        File outputFile = null;
        try
        {
            // - make sure we don't have any back slashes
            final String resourceUri = ResourceUtils.normalizePath(resourceUrl.toString());
            String uriSuffix = resource.getPath().replaceAll(PATH_PATTERN, "");;
            if (resourceUri.indexOf(uriSuffix) != -1)
            {
                uriSuffix = resourceUri.substring(resourceUri.indexOf(uriSuffix) + uriSuffix.length(), resourceUri.length());
            }       
            else
            {
                uriSuffix =
                    resourceUri.substring(
                        resourceUri.lastIndexOf(FORWARD_SLASH),
                        resourceUri.length());
            }

            final Map templateContext = new LinkedHashMap();
            this.populateTemplateContext(templateContext);
            
            // - if we have an outputCondition defined make sure it evaluates to true before continuing
            if (this.isValidOutputCondition(resource.getOutputCondition(), templateContext))
            {
                // - get the location and at the same time evaluate the outlet as a template engine variable (in case
                //   its defined as that).
                final String location =
                    Namespaces.instance().getPropertyValue(
                        this.getNamespace(),
                        this.getTemplateEngine().getEvaluatedExpression(
                            resource.getOutlet(),
                            templateContext));
    
                if (location != null)
                {
                    outputFile =
                        resource.getOutputLocation(
                            new String[] {uriSuffix},
                            new File(location),
                            this.getTemplateEngine().getEvaluatedExpression(
                                resource.getOutputPattern(),
                                templateContext));

                    final boolean lastModifiedCheck = resource.isLastModifiedCheck();
                    // - if we have the last modified check set, then make sure the last modified time is greater than the outputFile
                    if (!lastModifiedCheck || (lastModifiedCheck && ResourceUtils.getLastModifiedTime(resourceUrl) > outputFile.lastModified()))
                    {    
                        // - only write files that do NOT exist, and
                        //   those that have overwrite set to 'true'
                        if (!outputFile.exists() || resource.isOverwrite())
                        {
                            ResourceWriter.instance().writeUrlToFile(
                                resourceUrl,
                                outputFile.toString());
                            AndroMDALogger.info("Output: '" + outputFile.toURI() + "'");
                        }
                    }
                }
            }
        }
        catch (final Throwable throwable)
        {
            if (outputFile != null)
            {
                outputFile.delete();
                this.getLogger().info("Removed: '" + outputFile + "'");
            }
            throw new CartridgeException(throwable);
        }
    }

    /**
     * Stores the loaded resources to be processed by this cartridge instance.
     */
    private final List<Resource> resources = new ArrayList<Resource>();

    /**
     * Returns the list of templates configured in this cartridge.
     *
     * @return List the template list.
     */
    public List<Resource> getResources()
    {
        return this.resources;
    }

    /**
     * Adds a resource to the list of defined resources.
     *
     * @param resource the new resource to add
     */
    public void addResource(final Resource resource)
    {
        ExceptionUtils.checkNull(
            "resource",
            resource);
        resource.setCartridge(this);
        resources.add(resource);
    }
    
    /**
     * Populates the <code>templateContext</code> with the properties and template objects defined in the
     * <code>plugin</code>'s descriptor. If the <code>templateContext</code> is null, a new Map instance will be created
     * before populating the context.
     *
     * @param templateContext the context of the template to populate.
     */
    protected void populateTemplateContext(Map templateContext)
    {
        super.populateTemplateContext(templateContext);
        templateContext.putAll(this.getEvaluatedConditions(templateContext));
    }
    
    /**
     * Stores the global conditions from cartridge.xml condition expressions
     */
    //TODO Evaluate String condition as Boolean BEFORE adding to conditions Map. Currently values can be either Boolean or String - confusing.
    private final Map conditions = new LinkedHashMap();
    
    /**
     * Adds the outputCondition given the <code>name</code> and <code>value</code>
     * to the outputConditions map.
     * 
     * @param name the name of the outputCondition.
     * @param value the value of the outputCondition.
     */
    public void addCondition(final String name, final String value)
    {
        this.conditions.put(name, value != null ? value.trim() : "");
    }
    
    /**
     * Gets the current outputConditions defined within this cartridge
     * @return this.conditions
     */
    public Map getConditions()
    {
        return this.conditions;
    }
    
    /**
     * Indicates whether or not the global outputConditions have been evaluated.
     */
    private boolean conditionsEvaluated = false;
    
    /**
     * Evaluates all conditions and stores the results in the <code>conditions</code>
     * and returns that Map
     * 
     * @param templateContext the template context used to evaluate the conditions.
     * @param the map containing the evaluated conditions.
     */
    private Map getEvaluatedConditions(final Map templateContext)
    {
        if (!this.conditionsEvaluated)
        {
            for (final Iterator iterator = this.conditions.keySet().iterator(); iterator.hasNext();)
            {
                final String name = (String)iterator.next();
                final String value = (String)this.conditions.get(name);
                String evaluationResult = value != null ? value.trim() : null;
                if (evaluationResult != null && evaluationResult.trim().length() > 0)
                {
                    evaluationResult = this.getTemplateEngine().getEvaluatedExpression(
                        evaluationResult,
                        templateContext);
                }
                this.conditions.put(name, Boolean.valueOf(BooleanUtils.toBoolean(evaluationResult)));
            }
            this.conditionsEvaluated = true;
        }
        return this.conditions;
    }
    
    /**
     * Gets the evaluated outputCondition result of a global outputCondition.
     * 
     * @param templateContext the current template context to pass the template engine if 
     *        evaluation has yet to occur.
     * @return the evaluated outputCondition results.
     */
    private Boolean getGlobalConditionResult(final String outputCondition, final Map templateContext) 
    {
        return (Boolean)this.getEvaluatedConditions(templateContext).get(outputCondition);
    }
    
    /**
     * Indicates whether or not the given <code>outputCondition</code> is a valid
     * outputCondition, that is, whether or not it returns true.
     * 
     * @param outputCondition the outputCondition to evaluate.
     * @param templateContext the template context containing the variables to use.
     * @return true/false
     */
    private boolean isValidOutputCondition(final String outputCondition, final Map templateContext)
    {
        boolean validOutputCondition = true;
        if (outputCondition != null && outputCondition.trim().length() > 0)
        {
            Boolean result = this.getGlobalConditionResult(outputCondition, templateContext);
            if (result == null)
            {
                final String outputConditionResult = this.getTemplateEngine().getEvaluatedExpression(
                    outputCondition,
                    templateContext);
                result = Boolean.valueOf(BooleanUtils.toBoolean(outputConditionResult != null ? outputConditionResult.trim() : null));
            }
            validOutputCondition = result != null ? result.booleanValue() : false;
        }
        return validOutputCondition;
    }

    /**
     * Override to provide cartridge specific shutdown (
     *
     * @see org.andromda.core.common.Plugin#shutdown()
     */
    public void shutdown()
    {
        super.shutdown();
        this.conditions.clear();
    }
}