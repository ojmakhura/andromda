package org.andromda.modules.xmilink.action;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.andromda.modules.xmilink.ExportContext;
import org.andromda.modules.xmilink.Logger;
import org.andromda.modules.xmilink.io.Writer;

import com.togethersoft.modules.project.navigation.NavigationFeature;
import com.togethersoft.openapi.ide.progress.IdeProgress;
import com.togethersoft.openapi.ide.progress.IdeProgressIndicator;
import com.togethersoft.openapi.ide.progress.IdeProgressIndicatorAccess;
import com.togethersoft.openapi.model.elements.Model;
import com.togethersoft.platform.project.Project;
import com.togethersoft.platform.project.ProjectManagerAccess;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 19.10.2004
 */
public class XMIExportWorker
{

    public static void invokeExport(Writer writer, String fileName)
    {
        IdeProgressIndicator progressIndicator = IdeProgressIndicatorAccess.getProgressIndicator();
        IdeProgress progress = progressIndicator.start("XMI Export filter...", true, 100,
                "Exporting to XMI...");

        progress.setCount(10);
        progress.setStep("Walking the model...");
        Model model = getModel();
        performExport(model, writer, progress);

        progress.setCount(50);
        progress.setStep("Saving to XMI file: " + fileName);
        saveToFile(writer, fileName);

        progress.setCount(100);
        progress.setStep("Done.");
        progress.stop();
    }

    /**
     * @param writer
     * @param string
     */
    private static void saveToFile(Writer writer, String fileName)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(fileName);
            PrintWriter printer = new PrintWriter(fos);
            printer.print(writer.getContents());
            printer.close();
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param model
     * @param writer
     * @param progress
     */
    private static void performExport(Model model, Writer writer, IdeProgress progress)
    {
        ExportContext.setWriter(writer);
        ExportContext.setProgress(progress);

        Logger.info("Starting export...");
        ExportContext.export(model);
        Logger.info("Finished export.");

        Logger.info(writer.getContents());
    }

    /**
     * @return
     */
    private static Model getModel()
    {
        Project project = ProjectManagerAccess.getManager().getActiveProject();
        Model model = ((NavigationFeature)project
                .getFeature(com.togethersoft.modules.project.navigation.NavigationFeature.class))
                .getModel();
        return model;
    }

}
