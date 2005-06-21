package org.andromda.modules.xmilink.headless;

import java.io.File;
import java.net.MalformedURLException;

import org.andromda.modules.xmilink.action.XMIExportWorker;
import org.andromda.modules.xmilink.io.Writer;
import org.andromda.modules.xmilink.io.XMLWriter;

import com.togethersoft.modules.primetimebridge.PrimetimeAccess;
import com.togethersoft.platform.modulemanager.ModuleManagerAccess;
import com.togethersoft.platform.modulemanager.service.Application;
import com.togethersoft.platform.project.Project;

/**
 * TODO Please enter the purpose of this class.
 * 
 * @author Peter Friese
 */
public class XMIExport4AndroMDA
        implements Application
{

    /**
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        String parameters[] = ModuleManagerAccess.getManager().getCommandLine();
        if (parameters.length != 2)
        {
            System.out.println("Usage: XMIExport <project file> <xmi output file>");
            System.exit(1);
        }
        else
        {
            long startTime = System.currentTimeMillis();
            System.out.println("XMIExport4AndroMDA...");
            String projectFile = parameters[0];
            String outputFileName = parameters[1];

            System.out.println("... loading project");
            Project project = getProject(projectFile);
            if (project != null)
            {
                System.out.println("... project [" + project.getName() + "] loaded successfully");
                Writer writer = new XMLWriter();
                System.out.println("... exporting model to XMI file [" + outputFileName + "]");
                XMIExportWorker.invokeExport(writer, outputFileName);
                System.out.println("...done.");
                long endTime = System.currentTimeMillis();
                long secs = endTime - startTime;
                System.out.println("Export took " + secs + " miliseconds.");
            }
        }
    }

    private Project getProject(String fileName) throws IllegalArgumentException
    {
        java.net.URL url = null;
        try
        {
            url = (new File(fileName)).toURL();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        Project project = PrimetimeAccess.getPrimetime().getProjectBridge().openUILessProject(url);
        if (project == null)
        {
            throw new IllegalArgumentException("Could not open project.");
        }
        else
        {
            return project;
        }
    }

}
