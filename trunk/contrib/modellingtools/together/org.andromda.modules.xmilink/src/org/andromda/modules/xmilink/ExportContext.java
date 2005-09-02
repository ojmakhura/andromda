package org.andromda.modules.xmilink;

import java.util.ArrayList;

import org.andromda.modules.xmilink.io.Writer;

import com.togethersoft.openapi.ide.progress.IdeProgress;
import com.togethersoft.openapi.model.elements.Entity;
import com.togethersoft.openapi.model.elements.Model;

/**
 * TODO Specify purpose, please.
 *
 * @author Peter Friese
 * @version 1.0
 * @since 17.09.2004
 */
public final class ExportContext
{

    private static Writer writer;

    private static boolean initialized = false;

    private static ArrayList exportedElements = new ArrayList();

    private static IdeProgress progress;

    /**
     * @stereotype uses
     */
    /* #org.andromda.modules.xmilink.ExportStrategyFactory Dependency_Link */

    /**
     * @stereotype uses
     */
    /* #org.andromda.modules.xmilink.IExportStrategy Dependency_Link1 */

    /**
     * Hidden constructor.
     */
    private ExportContext()
    {
    }

    public static void export(Model model)
    {
        long start = System.currentTimeMillis();
        init();
        if (model != null)
        {
            String metaclass = model.getPropertyValue("$metaclass");
            Logger.info("Metaclass of the MODEL: " + metaclass);
            IExportStrategy strategy = ExportStrategyFactory.getInstance().getStrategy(metaclass);
            if (strategy != null)
            {
                strategy.export(model);
            }
        }
        long duration = System.currentTimeMillis() - start;
        // IdeMessageManagerAccess.printMessage(1, "Export took " + duration +
        // "ms.");
    }

    public static boolean export(Entity entity)
    {
        init();
        if (entity != null)
        {
            String metaclass = entity.getPropertyValue("$metaclass");

            /*
             * Special handling for classes marked with the stereotype <<DataType>>:
             * Since Together 2005 doesn't support modelling UML natively, we
             * treat all classes marked as <<DataType>> as if they weren't
             * classes, but Datatypes.
             */
            if (metaclass != null)
            {
                if (metaclass.startsWith("Class"))
                {
                    if (entity.hasPropertyValue("stereotype", "DataType"))
                    {
                        metaclass = "DataType";
                    }
                }
            }

            IExportStrategy strategy = ExportStrategyFactory.getInstance().getStrategy(metaclass);
            if (strategy != null)
            {
                if (progress != null)
                {
                    progress.setStep("Exporting model element " + entity.getUniqueName());
                }
                strategy.export(entity);
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    private static void init()
    {
        exportedElements.clear();
        if (!initialized)
        {
            registerStrategies();
        }
    }

    /**
     *
     */
    private static void registerStrategies()
    {
        try
        {
            // UML 1.4
            Class.forName("org.andromda.modules.xmilink.uml14.UMLProjectExportStrategy");
            Class.forName("org.andromda.modules.xmilink.uml14.UMLModelExportStrategy");
            Class.forName("org.andromda.modules.xmilink.uml14.UMLPackageExportStrategy");
            Class.forName("org.andromda.modules.xmilink.uml14.UMLClassExportStrategy");
            Class.forName("org.andromda.modules.xmilink.uml14.UMLAttributeExportStrategy");
            Class.forName("org.andromda.modules.xmilink.uml14.UMLOperationExportStrategy");
            Class.forName("org.andromda.modules.xmilink.uml14.UMLParameterExportStrategy");
            Class.forName("org.andromda.modules.xmilink.uml14.UMLDataTypeExportStrategy");

            // UML 2.0
            Class.forName("org.andromda.modules.xmilink.uml20.UML20ClassExportStrategy");
            Class.forName("org.andromda.modules.xmilink.uml20.UML20PropertyExportStrategy");
            Class.forName("org.andromda.modules.xmilink.uml20.UML20OperationExportStrategy");

            // Use Cases
            Class.forName("org.andromda.modules.xmilink.uml20.UML20ActorExportStrategy");

            // Links
            Class.forName("org.andromda.modules.xmilink.links.UML20DependencyExportStrategy");
            Class.forName("org.andromda.modules.xmilink.links.UML20GeneralizationLinkExportStrategy");
            Class.forName("org.andromda.modules.xmilink.links.UML20AssociatesLinkExportStrategy");
        }
        catch (ClassNotFoundException e)
        {
            Logger.error("Could not register export strategies.");
            e.printStackTrace();
        }
    }

    /**
     * @return Returns the writer.
     */
    public static Writer getWriter()
    {
        return writer;
    }

    /**
     * @param writer
     *            The writer to set.
     */
    public static void setWriter(Writer writer)
    {
        ExportContext.writer = writer;
    }

    public static void setProgress(IdeProgress progress)
    {
        ExportContext.progress = progress;
    }
}
