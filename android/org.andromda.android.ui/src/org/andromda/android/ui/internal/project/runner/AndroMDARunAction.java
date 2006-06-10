package org.andromda.android.ui.internal.project.runner;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.andromda.android.core.internal.project.runner.AndroMDARunner;
import org.andromda.android.ui.AndroidUIPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;

/**
 * 
 * @author Peter Friese
 * @since 04.06.2006
 */
public class AndroMDARunAction
        extends Action
{

    private final IFile file;

    /**
     * Creates a new AndroMDARunAction.
     * 
     * @param file The configuration file.
     */
    public AndroMDARunAction(IFile file)
    {
        super();
        this.file = file;
        this.setText("Run AndroMDA...");
    }

    /**
     * {@inheritDoc}
     */
    public void run()
    {
        super.run();
        if (file.getLocation() != null)
        {
            Job runAndroMDAJob = new Job("Running AndroMDA...")
            {

                protected IStatus run(IProgressMonitor monitor)
                {
                    IPath location = file.getLocation();
                    File file2 = location.toFile();
                    URL url;
                    try
                    {
                        url = file2.toURL();
                        System.out.println("Running on : " + url.toString());
                        AndroMDARunner runner = new AndroMDARunner();
                        runner.setConfiguration(url);
                        runner.run();
                    }
                    catch (MalformedURLException e)
                    {
                        AndroidUIPlugin.log(e);
                    }
                    return Status.OK_STATUS;
                }

            };
            runAndroMDAJob.schedule();
        }
    }

}
