package org.andromda.core.cartridge;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.BasePlugin;
import org.andromda.core.common.CodeGenerationContext;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.OutputUtils;
import org.andromda.core.common.Property;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * The AndroMDA Cartridge implementation of the Plugin. Cartridge instances are
 * configured from <code>META-INF/andromda-cartridge.xml</code> files
 * discovered on the classpath.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public class Cartridge
    extends BasePlugin
{
    private List templates = new ArrayList();
    private CodeGenerationContext context;

    /**
     * The default Cartridge constructor.
     */
    public Cartridge()
    {
        super();
    }

    /**
     * Cache for saving previously found model elements.
     */
    private Map elementCache = new HashMap();

    /**
     * Processes all model elements with relevant stereotypes by retrieving the
     * model elements from the model facade contained within the context.
     * 
     * @param context
     *            the context containing the RepositoryFacade (among other
     *            things).
     */
    public void processModelElements(CodeGenerationContext context)
    {
        final String methodName = "Cartridge.processModelElements";
        ExceptionUtils.checkNull(methodName, "context", context);

        this.context = context;

        Collection templates = this.getTemplateConfigurations();

        if (templates != null && !templates.isEmpty())
        {         
            MetafacadeFactory factory = MetafacadeFactory.getInstance();
    
            factory.setModel(context.getModelFacade());
    
            String previousNamespace = factory.getActiveNamespace();
            factory.setActiveNamespace(this.getName());
    
            Iterator templateIt = templates.iterator();
            while (templateIt.hasNext())
            {
                TemplateConfiguration template = 
                    (TemplateConfiguration)templateIt.next();
                TemplateModelElements templateModelElements = 
                    template.getSupportedModeElements();
                if (templateModelElements != null
                    && !templateModelElements.isEmpty())
                {
                    Iterator stereotypeIt = templateModelElements.stereotypeNames();
                    while (stereotypeIt.hasNext())
                    {
                        String stereotypeName = (String)stereotypeIt.next();
                        Collection modelElements = 
                            (Collection)this.elementCache.get(stereotypeName);
                        if (modelElements == null)
                        {
                            modelElements = 
                                context.getModelFacade().findByStereotype(
                                    stereotypeName);
                            elementCache.put(stereotypeName, modelElements);
                        }
    
                        TemplateModelElement templateModelElement = templateModelElements
                            .getModelElement(stereotypeName);
    
                        Collection metafacades = 
                            MetafacadeFactory.getInstance().createMetafacades(modelElements);
    
                        this.filterModelPackages(metafacades);
    
                        templateModelElement.setModelElements(metafacades);
                    }
                    processModelElements(template, context);
                }
            }
            //set the namespace back
            factory.setActiveNamespace(previousNamespace);
        }
    }

    /**
     * Processes all <code>modelElements</code> for this template.
     * 
     * @param template
     *            the TemplateConfiguration object from which we process.
     * @param context
     *            the context for the cartridge
     */
    protected void processModelElements(
        TemplateConfiguration template,
        CodeGenerationContext context)
    {
        final String methodName = "Cartridge.processModelElements";
        ExceptionUtils.checkNull(methodName, "template", template);
        ExceptionUtils.checkNull(methodName, "context", context);

        if (logger.isDebugEnabled())
            logger.debug("performing " + methodName + " with template '"
                + template + "' and context ' " + context + "'");

        TemplateModelElements templateModelElements = 
            template.getSupportedModeElements();

        if (templateModelElements != null && !templateModelElements.isEmpty())
        {
            Property outletProperty = 
                Namespaces.instance().findNamespaceProperty(this.getName(), template.getOutlet());

            if (outletProperty != null && !outletProperty.isIgnore())
            {
                try
                {
                    Collection allModelElements = 
                        templateModelElements.getAllModelElements();

                    // if isOutputToSingleFile flag is true, then
                    // we get the collections of templateModelElements and
                    // place them in the template context by their
                    // variable names.
                    if (template.isOutputToSingleFile())
                    {
                        Map templateContext = new HashMap();

                        // eliminate duplicates since all are
                        // output to one file
                        allModelElements = new HashSet(allModelElements);

                        // first place all relevant model elements by the
                        // <modelElements/> variable name
                        templateContext.put(
                            templateModelElements.getVariable(),
                            allModelElements);

                        // now place the collections of stereotyped elements
                        // by the given variable names. (skip it the variable
                        // was NOT defined
                        Iterator stereotypeNames = 
                            templateModelElements.stereotypeNames();
                        while (stereotypeNames.hasNext())
                        {
                            String name = (String)stereotypeNames.next();
                            TemplateModelElement templateModelElement = 
                                templateModelElements.getModelElement(name);
                            String variable = templateModelElement.getVariable();
                            if (StringUtils.isNotEmpty(variable))
                            {
                                // if a stereotype has the same variable defined
                                // more than one time, then get the existing
                                // model elements added from the last iteration
                                // and add the new ones to the collection
                                Collection modelElements = 
                                    (Collection)templateContext.get(variable);
                                if (modelElements != null)
                                {
                                    modelElements.addAll(
                                        templateModelElement.getModelElements());
                                }
                                else
                                {
                                    modelElements = 
                                        templateModelElement.getModelElements();
                                }
                                templateContext.put(variable, new HashSet(
                                    modelElements));
                            }
                        }
                        this.processWithTemplate(
                            template,
                            templateContext,
                            outletProperty,
                            null,
                            null);
                    }
                    else
                    {
                        // if outputToSingleFile isn't true, then
                        // we just place the model element with the default
                        // variable defined on the <modelElements/> into the
                        // template.
                        Iterator modelElementIt = allModelElements.iterator();

                        while (modelElementIt.hasNext())
                        {
                            Map templateContext = new HashMap();

                            Object modelElement = modelElementIt.next();

                            templateContext.put(templateModelElements
                                .getVariable(), modelElement);

                            this.processWithTemplate(
                                template,
                                templateContext,
                                outletProperty,
                                context.getModelFacade().getName(modelElement),
                                context.getModelFacade().getPackageName(
                                    modelElement));
                        }

                    }

                }
                catch (Throwable th)
                {
                    String errMsg = "Error performing " + methodName;
                    logger.error(errMsg, th);
                    throw new CartridgeException(errMsg, th);
                }
            }
        }
    }

    /**
     * <p>
     * Perform processing with the <code>template</code>.
     * </p>
     * 
     * @param template
     *            the TemplateConfiguration containing the template sheet to
     *            process.
     * @param templateContext
     *            the context to which variables are added and made avaialbe to
     *            the template engine for processing. This will contain any
     *            model elements being made avaiable to the template(s).
     * @param outletProperty
     *            the property defining the outlet to which output will be
     *            written.
     * @param modelElementName
     *            the name of the model element (if we are processing a single
     *            model element, otherwise this will be ignored).
     * @param modelElementPackage
     *            the name of the package (if we are processing a single model
     *            element, otherwise this will be ignored).
     */
    private void processWithTemplate(
        TemplateConfiguration template,
        Map templateContext,
        Property outletProperty,
        String modelElementName,
        String modelElementPackage)
    {
        final String methodName = "Cartridge.processWithTemplate";
        ExceptionUtils.checkNull(methodName, "template", template);
        ExceptionUtils.checkNull(methodName, "templateContext", templateContext);
        ExceptionUtils.checkNull(methodName, "outletProperty", outletProperty);

        File outFile = null;
        try
        {
            // populate the template context will plugin descriptor
            // properties and template objects
            this.populateTemplateContext(templateContext);

            StringWriter output = new StringWriter();

            // process the template with the set TemplateEngine
            this.getTemplateEngine().processTemplate(
                template.getSheet(),
                templateContext,
                output);

            if (template.getOutputPattern().startsWith("$"))
            {
                outFile = outputFileFromTemplateEngineContext(
                    template,
                    outletProperty.getValue());
            }
            else
            {
                outFile = this.outputFileFromTemplateConfig(
                    modelElementName,
                    modelElementPackage,
                    template,
                    outletProperty.getValue());
            }

            if (outFile != null)
            {
                // do not overWrite already generated file,
                // if that is a file that the user needs to edit
                boolean writeOutputFile = !outFile.exists()
                    || template.isOverWrite();

                long modelLastModified = context.getRepository()
                    .getLastModified();

                // only process files that have changed
                if (writeOutputFile
                    && (!context.isLastModifiedCheck() || modelLastModified > outFile
                        .lastModified()))
                {
                    String outputString = output.toString();

                    this.setLogger(this.getName());
                    //check to see if generateEmptyFiles is true and if
                    // outString (when CLEANED)
                    //isn't empty.
                    if (StringUtils.trimToEmpty(outputString).length() > 0
                        || template.isGenerateEmptyFiles())
                    {
                        OutputUtils.writeStringToFile(
                            outputString,
                            outFile,
                            true);
                        this.logger.info("Output: '" + outFile.toURI() + "'");
                    }
                    else
                    {
                        this.logger.info("Empty Output: '" + outFile.toURI()
                            + "' --> not writing");
                    }
                    this.resetLogger();
                }
            }
        }
        catch (Throwable th)
        {
            if (outFile != null)
            {
                outFile.delete();
                this.logger.info("Removed --> '" + outFile + "'");
            }

            String errMsg = "Error performing " + methodName
                + " with template '" + template.getSheet()
                + "', template context '" + templateContext
                + "' and cartridge '" + this.getName() + "'";
            logger.error(errMsg, th);
            throw new CartridgeException(errMsg, th);
        }
    }

    /**
     * Creates a File object from an output pattern in the template
     * configuration.
     * 
     * @param modelElementName
     *            the name of the model element
     * @param packageName
     *            the name of the package
     * @param tc
     *            the template configuration
     * @return File the output file
     */
    private File outputFileFromTemplateConfig(
        String modelElementName,
        String packageName,
        TemplateConfiguration tc,
        String outputLocation)
    {
        return tc.getOutputLocation(modelElementName, packageName, new File(
            outputLocation));
    }

    /**
     * Creates a File object from a variable in a TemplateEngine context.
     * 
     * @param template
     *            the template configuration
     * @return outputLocation the location to which the file will be output.
     */
    private File outputFileFromTemplateEngineContext(
        TemplateConfiguration template,
        String outputLocation)
    {
        String fileName = this.getTemplateEngine().getEvaluatedExpression(
            template.getOutputPattern());
        return new File(outputLocation, fileName);
    }

    /**
     * Filters out those model elements which <strong>should </strong> be
     * processed and returns the filtered collection
     * 
     * @param modelElements
     *            the Collection of modelElements.
     */
    protected void filterModelPackages(Collection modelElements)
    {
        CollectionUtils.filter(modelElements, new Predicate()
        {
            public boolean evaluate(Object modelElement)
            {
                return context.getModelPackages().shouldProcess(
                    context.getModelFacade().getPackageName(modelElement));
            }
        });
    }

    /**
     * Returns the list of templates configured in this cartridge.
     * 
     * @param List
     *            the template list.
     */
    public List getTemplateConfigurations()
    {
        return templates;
    }

    /**
     * Adds an item to the list of defined template configurations.
     * 
     * @param template
     *            the new configuration to add
     */
    public void addTemplateConfiguration(TemplateConfiguration template)
    {
        ExceptionUtils.checkNull(
            "Cartridge.addTemplateConfiguration",
            "template",
            template);
        template.setCartridge(this);
        templates.add(template);
    }

    /**
     * @see org.andromda.core.common.Plugin#getType()
     */
    public String getType()
    {
        return "cartridge";
    }

}