package org.andromda.cartridges.interfaces;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.andromda.core.anttasks.UserProperty;
import org.andromda.core.common.CodeGenerationContext;
import org.andromda.core.common.Logger;
import org.andromda.core.common.ScriptHelper;
import org.andromda.core.common.StringUtilsHelper;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * Default implementation of standard AndroMDA cartridge behaviour.
 * Can be customized by derived cartridge classes.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class DefaultAndroMDACartridge implements IAndroMDACartridge
{
    private ICartridgeDescriptor desc = null;

    private VelocityEngine ve = null;

    /**
     * @see org.andromda.cartridges.interfaces.IAndroMDACartridge#getDescriptor()
     */
    public ICartridgeDescriptor getDescriptor()
    {
        return desc;
    }

    /**
     * @see org.andromda.cartridges.interfaces.IAndroMDACartridge#setDescriptor(org.andromda.cartridges.interfaces.ICartridgeDescriptor)
     */
    public void setDescriptor(ICartridgeDescriptor d)
    {
        this.desc = d;
    }

    /**
     * <p>
     * Processes one model element with exactly one stereotype.
     * May be called several times for the same model element
     * because since UML 1.4, a model element can have multiple
     * stereotypes.
     * </p>
     *
     * @param  context         context for code generation
     * @param  modelElement    the model element for which code should be
     *                         generated
     * @param  stereotypeName  name of the stereotype which should trigger code
     *                         generation
     * @throws  CartridgeException  if something goes wrong
     */
    public void processModelElement(
        CodeGenerationContext context,
        Object modelElement,
        String stereotypeName)
        throws CartridgeException
    {
        String name = context.getScriptHelper().getName(modelElement);
        String packageName =
            context.getScriptHelper().getPackageName(modelElement);
        long modelLastModified = context.getRepository().getLastModified();

        List templates = getDescriptor().getTemplateConfigurations();
        for (Iterator it = templates.iterator(); it.hasNext();)
        {
            TemplateConfiguration tc = (TemplateConfiguration) it.next();
            if (tc.getStereotype().equals(stereotypeName))
            {
                ScriptHelper scriptHelper = context.getScriptHelper();

                if (tc.getTransformClass() != null)
                {
                    // template has its own custom script helper
                    try
                    {
                        context.setScriptHelper(
                            (ScriptHelper) tc
                                .getTransformClass()
                                .newInstance());
                        context.getScriptHelper().setModel(
                            context.getRepository().getModel());
                        context.getScriptHelper().setTypeMappings(
                            context.getTypeMappings());
                    }
                    catch (IllegalAccessException iae)
                    {
                        throw new CartridgeException(iae);
                    }
                    catch (InstantiationException ie)
                    {
                        throw new CartridgeException(ie);
                    }
                }

                File outFile =
                    tc.getFullyQualifiedOutputFile(
                        name,
                        packageName,
                        context.getOutletDictionary());

                if (outFile != null)
                {
                    try
                    {
                        // do not overwrite already generated file,
                        // if that is a file that the user wants to edit.
                        boolean writeOutputFile =
                            !outFile.exists() || tc.isOverwrite();
                        // only process files that have changed
                        if (writeOutputFile
                            && (!context.isLastModifiedCheck()
                                || modelLastModified > outFile.lastModified()
                            /*
                        *  || styleSheetLastModified > outFile.lastModified()
                        */
                            ))
                        {
                            processModelElementWithOneTemplate(
                                context,
                                modelElement,
                                tc.getSheet(),
                                outFile,
                                tc.isGenerateEmptyFiles());
                        }
                    }
                    catch (CartridgeException e)
                    {
                        outFile.delete();
                        throw new CartridgeException(e);
                    }
                }

                // restore original script helper in case we were
                // using a custom template script helper
                context.setScriptHelper(scriptHelper);
            }
        }
    }

    /**
     * <p>
     * Processes one model element with exactly one template script.
     * </p>
     *
     * @param  context         context for code generation
     * @param  modelElement    the model element for which code should be
     *                         generated
     * @param  styleSheetName  name of the Velocity style sheet
     * @param  outFile         file to which to write the output
     * @param  generateEmptyFile flag, tells whether to generate empty
     *                         files or not.
     * @throws  CartridgeException  if something goes wrong
     */
    private void processModelElementWithOneTemplate(
        CodeGenerationContext context,
        Object modelElement,
        String styleSheetName,
        File outFile,
        boolean generateEmptyFile)
        throws CartridgeException
    {
        Writer writer = null;
        ByteArrayOutputStream content = null;

        ensureDirectoryFor(outFile);
        try
        {
            if (generateEmptyFile)
            {
                writer =
                    new BufferedWriter(
                        new OutputStreamWriter(
                            new FileOutputStream(outFile)));
            }
            else
            {
                content = new ByteArrayOutputStream();
                writer = new OutputStreamWriter(content);
            }
        }
        catch (Exception e)
        {
            throw new CartridgeException(
                "Error opening output file " + outFile.getName(),
                e);
        }

        try
        {
            VelocityContext velocityContext = new VelocityContext();

            // put some objects into the velocity context
            velocityContext.put(
                "model",
                context.getScriptHelper().getModel());
            velocityContext.put("transform", context.getScriptHelper());
            velocityContext.put("str", new StringUtilsHelper());
            velocityContext.put("class", modelElement);
            velocityContext.put("date", new java.util.Date());

            addUserPropertiesToContext(
                velocityContext,
                context.getUserProperties());

            // get the template to process
            Template template = ve.getTemplate(styleSheetName);

            // Process the VSL template with the context and write out
            // the result as the outFile.
            template.merge(velocityContext, writer);

            writer.flush();
            writer.close();
        }
        catch (Exception e)
        {
            try
            {
                writer.flush();
                writer.close();
            }
            catch (Exception e2)
            {
            }

            e.printStackTrace();

            throw new CartridgeException(
                "Error processing velocity script on " + outFile.getName(),
                e);
        }

        // Handle file generation/removal if no files should be generated for
        // empty output.
        if (!generateEmptyFile)
        {
            byte[] result = content.toByteArray();
            if (result.length > 0)
            {
                try
                {
                    OutputStream out = new FileOutputStream(outFile);
                    out.write(result);
                    Logger.log("Output: " + outFile, Logger.MSG_INFO);
                }
                catch (Exception e)
                {
                    throw new CartridgeException(
                        "Error writing output file " + outFile.getName(),
                        e);
                }
            }
            else
            {
                if (outFile.exists())
                {
                    if (!outFile.delete())
                    {
                        throw new CartridgeException(
                            "Error removing output file "
                                + outFile.getName());
                    }
                    Logger.log("Remove: " + outFile, Logger.MSG_INFO);
                }
            }
        }
        else
        {
            Logger.log("Output: " + outFile, Logger.MSG_INFO);
        }
    }

    /**
     * Takes all the UserProperty values that were defined in the ant build.xml
     * ile and adds them to the Velocity context.
     *
     * @param  context  the Velocity context
     * @param  userProperties the user properties
     */
    private void addUserPropertiesToContext(
        VelocityContext context,
        Collection userProperties)
    {
        for (Iterator it = userProperties.iterator(); it.hasNext();)
        {
            UserProperty up = (UserProperty) it.next();
            context.put(up.getName(), up.getValue());
        }
    }

    /**
     * <p>
     *  Creates  directories as needed.
     * </p>
     *
     *@param  targetFile a <code>File</code> whose parent directories need to
     *exist
     *@exception CartridgeException if the parent directories couldn't be created
     */
    private void ensureDirectoryFor(File targetFile)
        throws CartridgeException
    {
        File directory = new File(targetFile.getParent());
        if (!directory.exists())
        {
            if (!directory.mkdirs())
            {
                throw new CartridgeException(
                    "Unable to create directory: "
                        + directory.getAbsolutePath());
            }
        }
    }

    /**
     * @see org.andromda.cartridges.interfaces.IAndroMDACartridge#init(Properties)
     */
    public void init(Properties velocityProperties)
        throws Exception
    {
        ve = new VelocityEngine();

        // Tell Velocity it should also use the classpath when searching for templates
        ExtendedProperties ep =
            ExtendedProperties.convertProperties(velocityProperties);

        ep.addProperty(
            VelocityEngine.RESOURCE_LOADER,
            "andromda.cartridges,file");

        ep.setProperty(
            "andromda.cartridges."
                + VelocityEngine.RESOURCE_LOADER
                + ".class",
            ClasspathResourceLoader.class.getName());

        // Let Velocity know about the macro libraries.
        for (Iterator iter = getDescriptor().getMacroLibraries().iterator(); iter.hasNext();)
        {
            String libraryName = (String) iter.next();
            ep.addProperty(VelocityEngine.VM_LIBRARY, libraryName);
        }

        ve.setExtendedProperties(ep);
        ve.init();
    }

}
