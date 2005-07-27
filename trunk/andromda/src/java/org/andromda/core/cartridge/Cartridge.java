package org.andromda.core.cartridge;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.andromda.core.cartridge.template.ModelElement;
import org.andromda.core.cartridge.template.ModelElements;
import org.andromda.core.cartridge.template.Template;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.BasePlugin;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.PathMatcher;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.ResourceWriter;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.apache.commons.lang.StringUtils;


/**
 * The AndroMDA Cartridge implementation of the Plugin. Cartridge instances are configured from
 * <code>META-INF/andromda-cartridge.xml</code> files discovered on the classpath.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public class Cartridge
    extends BasePlugin
{
    /**
     * Processes all model elements with relevant stereotypes by retrieving the model elements from the model facade
     * contained within the context.
     *
     * @param factory the metafacade factory (which is used to manage the lifecycle of metafacades).
     */
    public void processModelElements(final MetafacadeFactory factory)
    {
        final String methodName = "Cartridge.processModelElements";
        ExceptionUtils.checkNull(methodName, "factory", factory);
        final Collection resources = this.getResources();
        if (resources != null && !resources.isEmpty())
        {
            for (final Iterator iterator = resources.iterator(); iterator.hasNext();)
            {
                final Resource resource = (Resource)iterator.next();
                if (resource instanceof Template)
                {
                    this.processTemplate(factory, (Template)resource);
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
        final String methodName = "Cartridge.processTemplate";
        ExceptionUtils.checkNull(methodName, "template", template);
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
            this.processTemplateWithMetafacades(factory, template);
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
     * @param context the context for the cartridge
     */
    protected void processTemplateWithMetafacades(
        final MetafacadeFactory factory,
        final Template template)
    {
        final String methodName = "Cartridge.processTemplateWithMetafacades";
        ExceptionUtils.checkNull(methodName, "template", template);
        final ModelElements modelElements = template.getSupportedModeElements();
        if (modelElements != null && !modelElements.isEmpty())
        {
            final String outlet = Namespaces.instance().getPropertyValue(
                    this.getNamespace(),
                    template.getOutlet());
            if (outlet != null)
            {
                try
                {
                    final Collection allMetafacades = modelElements.getAllMetafacades();

                    // - if outputToSingleFile is true AND outputOnEmptyElements
                    //   is true or we have at least one metafacade in the
                    //   allMetafacades collection, then we collect the template
                    //   model elements and place them into the template context
                    //   by their variable names.
                    if (template.isOutputToSingleFile() &&
                        (template.isOutputOnEmptyElements() || !allMetafacades.isEmpty()))
                    {
                        final Map templateContext = new HashMap();

                        // - first place all relevant model elements by the
                        //   <modelElements/> variable name. If the variable
                        //   isn't defined (which is possible), ignore.
                        if (StringUtils.isNotBlank(modelElements.getVariable()))
                        {
                            templateContext.put(
                                modelElements.getVariable(),
                                allMetafacades);
                        }

                        // - now place the collections of elements
                        //   by the given variable names. (skip if the variable
                        //   was NOT defined)
                        for (final Iterator iterator = modelElements.getModelElements().iterator(); iterator.hasNext();)
                        {
                            final ModelElement modelElement = (ModelElement)iterator.next();
                            final String variable = modelElement.getVariable();
                            if (StringUtils.isNotEmpty(variable))
                            {
                                // - if a stereotype has the same variable defined
                                //   more than one time, then get the existing
                                //   model elements added from the last iteration
                                //   and add the new ones to that collection
                                Collection metafacades = (Collection)templateContext.get(variable);
                                if (metafacades != null)
                                {
                                    metafacades.addAll(modelElement.getMetafacades());
                                }
                                else
                                {
                                    metafacades = modelElement.getMetafacades();
                                }
                                templateContext.put(
                                    variable,
                                    new LinkedHashSet(metafacades));
                            }
                        }
                        this.processWithTemplate(template, templateContext, outlet, null, null);
                    }
                    else
                    {
                        // - if outputToSingleFile isn't true, then
                        //   we just place the model element with the default
                        //   variable defined on the <modelElements/> into the
                        //   template.
                        for (final Iterator iterator = allMetafacades.iterator(); iterator.hasNext();)
                        {
                            final Map templateContext = new HashMap();
                            final Object metafacade = iterator.next();
                            templateContext.put(
                                modelElements.getVariable(),
                                metafacade);

                            this.processWithTemplate(
                                template,
                                templateContext,
                                outlet,
                                factory.getModel().getName(metafacade),
                                factory.getModel().getPackageName(metafacade));
                        }
                    }
                }
                catch (final Throwable throwable)
                {
                    throw new CartridgeException(throwable);
                }
            }
        }
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
        final String methodName = "Cartridge.processTemplateWithoutMetafacades";
        ExceptionUtils.checkNull(methodName, "template", template);
        final String outlet = Namespaces.instance().getPropertyValue(
                this.getNamespace(),
                template.getOutlet());
        if (outlet != null)
        {
            final Map templateContext = new HashMap();
            this.processWithTemplate(template, templateContext, outlet, null, null);
        }
    }

    /**
     * <p> 
     * Perform processing with the <code>template</code>.
     * </p>
     *
     * @param template the Template containing the template path to process.
     * @param templateContext the context to which variables are added and made
     *        available to the template engine for processing. This will contain
     *        any model elements being made avaiable to the template(s) as well
     *        as properties/template objects.
     * @param outlet the location or pattern defining where output will be written.
     * @param metafacadeName the name of the model element (if we are
     *        processing a single model element, otherwise this will be
     *        ignored).
     * @param metafacadePackage the name of the package (if we are processing
     *        a single model element, otherwise this will be ignored).
     */
    private final void processWithTemplate(
        final Template template,
        final Map templateContext,
        final String outlet,
        final String metafacadeName,
        final String metafacadePackage)
    {
        final String methodName = "Cartridge.processWithTemplate";
        ExceptionUtils.checkNull(methodName, "template", template);
        ExceptionUtils.checkNull(methodName, "templateContext", templateContext);
        ExceptionUtils.checkNull(methodName, "outlet", outlet);

        File outputFile = null;
        try
        {
            // populate the template context will cartridge descriptor
            // properties and template objects
            this.populateTemplateContext(templateContext);

            final StringWriter output = new StringWriter();

            // process the template with the set TemplateEngine
            this.getTemplateEngine().processTemplate(
                template.getPath(),
                templateContext,
                output);

            outputFile =
                template.getOutputLocation(
                    metafacadeName,
                    metafacadePackage,
                    new File(outlet),
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
                    if (StringUtils.isNotBlank(outputString) || template.isGenerateEmptyFiles())
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
        catch (final Throwable throwable)
        {
            if (outputFile != null)
            {
                outputFile.delete();
                this.getLogger().info("Removed: '" + outputFile + "'");
            }
            final String message =
                "Error performing " + methodName + " with template '" + template.getPath() + "', template context '" +
                templateContext + "' and cartridge '" + this.getNamespace() + "'";
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
        final String methodName = "Cartridge.processResource";
        ExceptionUtils.checkNull(methodName, "resource", resource);

        URL resourceUrl = ResourceUtils.getResource(
                resource.getPath(),
                this.getMergeLocation());
        if (resourceUrl == null)
        {
            // - if the resourceUrl is null, the path is probably a regular
            //   expression pattern so we'll see if we can match it against
            //   the contents of the plugin and write any contents that do match
            final List contents = this.getContents();
            if (contents != null)
            {
                AndroMDALogger.setSuffix(this.getNamespace());
                for (final Iterator iterator = contents.iterator(); iterator.hasNext();)
                {
                    final String content = (String)iterator.next();
                    if (StringUtils.isNotEmpty(content))
                    {
                        if (PathMatcher.wildcardMatch(
                                content,
                                resource.getPath()))
                        {
                            resourceUrl = ResourceUtils.getResource(
                                    content,
                                    this.getMergeLocation());
                            this.writeResource(resource, resourceUrl);
                        }
                    }
                }
                AndroMDALogger.reset();
            }
        }
        else
        {
            this.writeResource(resource, resourceUrl);
        }
    }

    /**
     * The forward slash constant.
     */
    private static final String FORWARD_SLASH = "/";

    /**
     * Writes the contents of <code>resourceUrl</code> to the outlet specified by <code>resource</code>.
     *
     * @param resource contains the outlet where the resource is written.
     * @param resourceUrl the URL contents to write.
     */
    private final void writeResource(
        final Resource resource,
        final URL resourceUrl)
    {
        File outFile = null;
        try
        {
            String outlet = Namespaces.instance().getPropertyValue(
                    this.getNamespace(),
                    resource.getOutlet());
            if (outlet != null)
            {
                // - make sure we don't have any back slashes
                final String resourceUri = resourceUrl.toString().replaceAll("\\\\", FORWARD_SLASH);
                final String uriSuffix =
                    resourceUri.substring(
                        resourceUri.lastIndexOf(FORWARD_SLASH),
                        resourceUri.length());
                if (outlet.endsWith(FORWARD_SLASH))
                {
                    // - remove the extra slash
                    outlet = outlet.replaceFirst(FORWARD_SLASH, "");
                }

                final Map templateContext = new HashMap();
                this.populateTemplateContext(templateContext);
                outFile =
                    resource.getOutputLocation(
                        new String[] {uriSuffix},
                        new File(outlet),
                        this.getTemplateEngine().getEvaluatedExpression(
                            resource.getOutputPattern(),
                            templateContext));

                // - only write files that do NOT exist, and
                //   those that have overwrite set to 'true'
                if (!outFile.exists() || resource.isOverwrite())
                {
                    ResourceWriter.instance().writeUrlToFile(
                        resourceUrl,
                        outFile.toString());
                    AndroMDALogger.info("Output: '" + outFile.toURI() + "'");
                }
            }
        }
        catch (final Throwable throwable)
        {
            if (outFile != null)
            {
                outFile.delete();
                this.getLogger().info("Removed: '" + outFile + "'");
            }
            throw new CartridgeException(throwable);
        }
    }

    /**
     * Stores the loaded resources to be processed by this cartridge intance.
     */
    private final List resources = new ArrayList();

    /**
     * Returns the list of templates configured in this cartridge.
     *
     * @return List the template list.
     */
    public List getResources()
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
        ExceptionUtils.checkNull("Cartridge.addResource", "resource", resource);
        resource.setCartridge(this);
        resources.add(resource);
    }

    /**
     * Override to provide cartridge specific shutdown (
     *
     * @see org.andromda.core.common.Plugin#shutdown()
     */
    public void shutdown()
    {
        super.shutdown();
    }
}