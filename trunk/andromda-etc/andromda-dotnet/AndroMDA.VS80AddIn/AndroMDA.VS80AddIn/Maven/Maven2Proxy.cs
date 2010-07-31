
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections;
using System.Text;
using System.Threading;
using System.ComponentModel;
using System.Diagnostics;

using EnvDTE;
using EnvDTE80;

using Microsoft.VisualStudio.CommandBars;

#endregion

namespace AndroMDA.VS80AddIn
{

    public class Maven2Proxy : MavenProxy
    {

        public Maven2Proxy(EnvDTE.DTE applicationObject, AddInSettings addInSettings)
            : base(applicationObject, addInSettings) { }

        public Maven2Proxy(ISynchronizeInvoke target, EnvDTE.DTE applicationObject, AddInSettings addInSettings) : base(target, applicationObject, addInSettings) { }

        protected override void Initialize(DTE applicationObject, AddInSettings addInSettings)
        {
            base.Initialize(applicationObject, addInSettings);
            this.ProcessStdOut += new ProcessTextDelegate(ProcessOutput);
            this.ProcessStdErr += new ProcessTextDelegate(ProcessOutput);
        }

        private void ProcessOutput(ref string text, ref bool cancelOutput)
        {
            if (text == string.Empty) return;
            else
            {
                if (text.EndsWith("K") || text.EndsWith("b"))
                {
                    bool isBytes = false;
                    if (text.EndsWith("b")) isBytes = true;
                    string[] t = text.Split('/');
                    if (t.Length == 2)
                    {
                        int current = int.Parse(t[0]);
                        int total = int.Parse(t[1].Remove(t[1].Length - 1));
                        if (current > total) total = current;
                        OutputWindow.DTE.StatusBar.Progress(true, "AndroMDA: Maven: Downloading file... " + text, current, total);
                        cancelOutput = true;
                    }
                }
                else if (text.EndsWith("K downloaded") || text.EndsWith("b downloaded"))
                {
                    OutputWindow.DTE.StatusBar.Progress(false, "Android/VS: Generation Progress",0,0);
                }
            }
        }

        protected override void BeforeExecution()
        {
            m_mavenOptions = "install";

            base.BeforeExecution();

            this.OutputWindowPane.OutputString("Launching 'mvn " + m_mavenOptions + "'...\n\n");

            string java_home = System.Environment.GetEnvironmentVariable("JAVA_HOME");
            string maven_home = System.Environment.GetEnvironmentVariable("M2_HOME");
            string maven_java_exe = java_home + "\\bin\\java.exe";
            string maven_opts = System.Environment.GetEnvironmentVariable("MAVEN_OPTS");
            string maven_cmd_line_args = m_mavenOptions;
            string classworlds_jar = string.Empty;

            if (string.IsNullOrEmpty(java_home))
            {
                throw new MavenProxyKnownException(
                    "Error: Maven cannot run because the JAVA_HOME environment variable is not set.\n"
                  + "Error: Please check your java installation.");
            }

            if (string.IsNullOrEmpty(maven_home))
            {
                throw new MavenProxyKnownException(
                    "Error: Maven cannot run because the M2_HOME environment variable is not set.\n"
                  + "Error: Please check that maven 2.x has been correctly installed.");
            }

            if (string.IsNullOrEmpty(maven_opts))
            {
                maven_opts = "-Xmx256m";
            }

            string bootPath = maven_home + @"\core\boot";

            if (!System.IO.Directory.Exists(bootPath))
            {
                bootPath = maven_home + @"\boot";
            }

            if (!System.IO.Directory.Exists(bootPath))
            {
                throw new MavenProxyKnownException(
                    "Error: Maven cannot run because the M2_HOME boot folder does not exist.\n"
                  + "Error: Please check that maven 2.x has been correctly installed.");
            }

            string[] classworldJars = null;

            try
            {
                classworldJars = System.IO.Directory.GetFiles(
                    bootPath,
                    "classworlds-*.jar",
                    System.IO.SearchOption.TopDirectoryOnly);
            }
            catch (Exception ex)
            {
                throw new MavenProxyKnownException(
                    "Error: " + ex.Message + "\n"
                  + "Error: Please check that maven 2.x has been correctly installed.");
            }

            if (classworldJars == null
                || classworldJars.Length < 1
                || string.IsNullOrEmpty(classworldJars[0]))
            {
                throw new MavenProxyKnownException(
                    "Error: Maven cannot run because the boot class path cannot be constructed.\n"
                  + "Error: Please check that maven 2.x has been correctly installed.");
            }


            classworlds_jar = classworldJars[0];
            this.ExecutablePath = maven_java_exe;
            this.CommandLine =
                  maven_opts
                + @" -classpath """ + classworlds_jar + @""""
                + @" ""-Dclassworlds.conf=" + maven_home + @"\bin\m2.conf"""
                + @" ""-Dmaven.home=" + maven_home + @""""
                + @" org.codehaus.classworlds.Launcher ";

            this.CommandLine += maven_cmd_line_args;

            m_mavenStatus = MavenStatus.None;

        }

        protected override void AfterExecution()
        {
            VSExternalToolEventArgs args = (VSExternalToolEventArgs)this.EventArguments;
            foreach (string line in OutputLines)
            {
                if (line.Contains("does not exist or no valid version could be found") ||
                    line.Contains("required artifacts are missing") ||
                    line.Contains("required artifact is missing") ||
                    line.Contains("not found in repository") ||
                    line.Contains("Failed to resolve artifact"))
                {
                    m_mavenStatus = MavenStatus.UnsatisfiedDependency;
                }
                if (args.ExitStatus != ToolExitStatus.Error && line.Contains("[ERROR]"))
                {
                    args.ExitStatus = ToolExitStatus.Error;
                }
            }
        }

    }
}