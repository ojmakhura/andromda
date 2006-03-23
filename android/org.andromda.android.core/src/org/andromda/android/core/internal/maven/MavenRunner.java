package org.andromda.android.core.internal.maven;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.android.core.AndroidCore;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.osgi.framework.Bundle;

/**
 * TODO: Hava a look at AntLaunchDelegate.runInSeparateVM()
 *
 * @author Peter Friese
 * @since 17.06.2005
 */
public class MavenRunner
        implements IDebugEventSetListener
{

    /** Contains the project properties the user has entered in the wizard. */
    private Map projectProperties;

    private IProcess[] processes;

    private final IProject project;

    /**
     * A <code>MavenRunner</code> can execute maven in order to attain maven goals. Project specific properties can be
     * set using the <code>projectProperties</code> parameter.
     *
     * @param projectProperties The project properties as entered by the user.
     * @param project TODO
     */
    public MavenRunner(Map projectProperties,
        IProject project)
    {
        this.projectProperties = projectProperties;
        this.project = project;
    }

    /**
     * Runs maven.
     *
     * @param monitor The progress monitor to be used.
     */
    public void execute(IProgressMonitor monitor)
    {

        try
        {
            monitor.beginTask("Creating project.", 10);

            monitor.subTask("Preparing to start maven.");

            // classpath
            String[] foreheadClasspath = getForeheadClasspath();

            // script options
            String[] options = buildOptions();

            String[] goals = { "andromdapp:generate" };
            String[] environment = { "MAVEN_HOME=" + getMavenHome() };

            String projectPath = (String)projectProperties.get("projectPath");
            File projectParentDir = new File(projectPath).getParentFile();

            // VM runner
            VMRunnerConfiguration vmConfig = new VMRunnerConfiguration("com.werken.forehead.Forehead",
                    foreheadClasspath);

            vmConfig.setVMArguments(options);
            vmConfig.setProgramArguments(goals);
            vmConfig.setWorkingDirectory(projectParentDir.getAbsolutePath());
            vmConfig.setEnvironment(environment);

            String launchMode = ILaunchManager.RUN_MODE;
            IVMRunner vmRunner = getJRE().getVMRunner(launchMode);

            if (vmRunner != null)
            {
                // launch manager
                ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
                ILaunchConfigurationType type = manager
                        .getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);

                ILaunchConfigurationWorkingCopy launchWorkingCopy = type.newInstance(null, "Create AndroMDA project.");
                launchWorkingCopy.setAttribute(IDebugUIConstants.ATTR_PRIVATE, true);

                monitor.worked(2);
                monitor.subTask("Starting maven.");

                ILaunch newLaunch = new Launch(launchWorkingCopy, ILaunchManager.RUN_MODE, null);
                DebugPlugin.getDefault().getLaunchManager().addLaunch(newLaunch);
                vmRunner.run(vmConfig, newLaunch, monitor);
                processes = newLaunch.getProcesses();
                DebugPlugin.getDefault().addDebugEventListener(this);
                monitor.worked(8);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            monitor.done();
        }

    }

    private String[] buildOptions()
    {
        // options and arguments
        List optionList = new ArrayList();
        optionList.add("-Dmaven.home=" + getMavenHome());
        optionList.add("-Dtools.jar=" + getToolsJarPath());
        optionList.add("-Dforehead.conf.file=" + getForeheadConfigFile());

        for (Iterator iter = projectProperties.entrySet().iterator(); iter.hasNext();)
        {
            Map.Entry entry = (Map.Entry)iter.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            optionList.add("-D" + key + "=" + value);
        }
        String[] options = (String[])optionList.toArray(new String[optionList.size()]);
        return options;
    }

    private String getForeheadConfigFile()
    {
        return getMavenHome() + "/bin/forehead.conf";
    }

    private String[] getForeheadClasspath()
    {
        List classpath = new ArrayList();

        // // forehead libraries
        // Bundle bundle = Platform.getBundle("org.apache.maven");
        // URL foreheadURL = bundle.getEntry("/lib/forehead-1.0-beta-5.jar");
        // classpath.add(transformToAbsolutePath(foreheadURL));
        //
        classpath.add(getForeHeadJarPath());

        return (String[])classpath.toArray(new String[classpath.size()]);
    }

    private String[] getMavenClasspath() throws CoreException, IOException
    {
        List classpath = new ArrayList();

        // tools.jar
        IPath toolsPath = new Path(getJDKHome()).append("lib").append("tools.jar");
        IRuntimeClasspathEntry toolsEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(toolsPath);
        toolsEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
        classpath.add(toolsEntry.getPath().toFile().getAbsolutePath());

        // maven libraries
        Bundle bundle = Platform.getBundle("org.apache.maven");
        Enumeration mavenJars = bundle.findEntries("lib", "*.jar", true);
        while (mavenJars.hasMoreElements())
        {
            URL jarFileURL = (URL)mavenJars.nextElement();
            String absolutePath = transformToAbsolutePath(jarFileURL);
            classpath.add(absolutePath);
        }

        return (String[])classpath.toArray(new String[classpath.size()]);
    }

    private String transformToAbsolutePath(URL url)
    {
        String absolutePath;
        try
        {
            url = Platform.asLocalURL(url);
            File file = new File(url.getFile());
            absolutePath = file.getAbsolutePath();
        }
        catch (IOException e)
        {
            absolutePath = "";
            e.printStackTrace();
        }
        return absolutePath;
    }

    private IVMInstall getJRE()
    {
        return JavaRuntime.getDefaultVMInstall();
    }

    private String getMavenHome()
    {
        return AndroidCore.getAndroidSettings().getMavenLocation();
        // return "C:/Programme/Maven 1.0.2";
        // Bundle bundle = Platform.getBundle("org.apache.maven");
        // URL rootURL = bundle.getEntry("/");
        // return transformToAbsolutePath(rootURL);
    }

    private String getMavenEndorsedHome()
    {
        return getMavenHome() + "/lib/endorsed";
    }

    private String getJDKHome()
    {
        File jdkHome = getJRE().getInstallLocation();
        return jdkHome.getAbsolutePath();
    }

    private String getToolsJarPath()
    {
        return getJDKHome() + "/lib/tools.jar";
    }

    private String getForeHeadJarPath()
    {
        return getMavenHome() + "/lib/forehead-1.0-beta-5.jar";
    }

    /**
     * This method is here to refresh the fresh project in the workspace as soon as maven has finished creating it.
     *
     * @param events An array of debug events. We are looking for a <code>DebugEvent#TERMINATE</code> event.
     */
    public void handleDebugEvents(DebugEvent[] events)
    {
        for (int i = 0; i < events.length; i++)
        {
            DebugEvent event = events[i];
            if (ArrayUtils.contains(getProcesses(), event.getSource()))
            {

                if (event.getKind() == DebugEvent.TERMINATE)
                {
                    DebugPlugin.getDefault().removeDebugEventListener(this);
                    try
                    {
                        project.refreshLocal(IResource.DEPTH_INFINITE, null);
                    }
                    catch (CoreException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @return
     */
    private IProcess[] getProcesses()
    {
        if (processes != null)
        {
            return processes;
        }
        else
        {
            return new IProcess[0];
        }
    }

}
