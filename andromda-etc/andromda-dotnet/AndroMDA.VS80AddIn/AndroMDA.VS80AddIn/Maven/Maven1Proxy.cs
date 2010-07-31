
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

    public class Maven1Proxy : MavenProxy
    {

        public Maven1Proxy(EnvDTE.DTE applicationObject, AddInSettings addInSettings)
            : base(applicationObject, addInSettings) { }

        public Maven1Proxy(ISynchronizeInvoke target, EnvDTE.DTE applicationObject, AddInSettings addInSettings) : base(target, applicationObject, addInSettings) { }

        protected override void Initialize(DTE applicationObject, AddInSettings addInSettings)
        {
            base.Initialize(applicationObject, addInSettings);
            this.ProcessStdOut += new ProcessTextDelegate(MavenProxy_ProcessStdOut);
            this.ProcessStdErr += new ProcessTextDelegate(MavenProxy_ProcessStdErr);
        }

        void MavenProxy_ProcessStdErr(ref string text, ref bool cancelOutput)
        {
            ProcessOutput(ref text, ref cancelOutput);
            if (text.Contains("unsatisfied dependency") || text.Contains("unsatisfied dependencies"))
            {
                m_mavenStatus = MavenStatus.UnsatisfiedDependency;
            }
        }

        void MavenProxy_ProcessStdOut(ref string text, ref bool cancelOutput)
        {
            ProcessOutput(ref text, ref cancelOutput);
        }

        private void ProcessOutput(ref string text, ref bool cancelOutput)
        {
            if (text == string.Empty) return;
            else
            {
                if (text.EndsWith("K"))
                {
                    string[] t = text.Split('/');
                    if (t.Length == 2)
                    {
                        int current = int.Parse(t[0]);
                        int total = int.Parse(t[1].Remove(t[1].Length - 1));
                        OutputWindow.DTE.StatusBar.Progress(true, "AndroMDA: Maven: Downloading file... " + text, current, total);
                        cancelOutput = true;
                    }
                }
                else if (text.EndsWith("K downloaded"))
                {
                    OutputWindow.DTE.StatusBar.Progress(false, "Android/VS: Generation Progress", 0, 0);
                }
            }
        }

        protected override void BeforeExecution()
        {
            m_mavenOptions = "mda";

            base.BeforeExecution();

            this.OutputWindowPane.OutputString("Launching 'maven " + m_mavenOptions + "'...\n\n");

            string java_home = System.Environment.GetEnvironmentVariable("JAVA_HOME");
            string maven_home = System.Environment.GetEnvironmentVariable("MAVEN_HOME");
            string maven_home_local = System.Environment.GetEnvironmentVariable("MAVEN_HOME_LOCAL");
            string maven_java_exe = java_home + "\\bin\\java.exe";
            string maven_classpath = maven_home + "\\lib\\forehead-1.0-beta-5.jar";
            string maven_forehead_conf = maven_home + "\\bin\\forehead.conf";
            string maven_main_class = "com.werken.forehead.Forehead";
            string maven_endorsed = java_home + "\\lib\\endorsed;" + maven_home + "\\lib\\endorsed";
            string maven_opts = System.Environment.GetEnvironmentVariable("MAVEN_OPTS");
            string maven_cmd_line_args = m_mavenOptions;

            if (java_home == null || java_home == string.Empty)
            {
                throw new MavenProxyKnownException("Error: Maven cannot run because the JAVA_HOME environment variable is not set.\nError: Please check your java installation.");
            }

            if (maven_home == null || maven_home == string.Empty)
            {
                throw new MavenProxyKnownException("Error: Maven cannot run because the MAVEN_HOME environment variable is not set.\nError: Please check that maven 1.x has been correctly installed.");
            }

            if (!System.IO.File.Exists(maven_classpath) || !System.IO.File.Exists(maven_forehead_conf))
            {
                throw new MavenProxyKnownException("Error: Although the MAVEN_HOME variable was set, maven could not be found.\nError: Please check that maven 1.x has been correctly installed.");
            }

            if (maven_opts == null || maven_opts == string.Empty)
            {
                maven_opts = "-Xmx256m";
            }

            this.ExecutablePath = maven_java_exe;
            this.CommandLine = "-Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl " +
                                "-Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl " +
                                "\"-Dmaven.home=" + maven_home + "\" ";

            if (maven_home_local != null && maven_home_local != string.Empty)
            {
                this.CommandLine += "\"-Dmaven.home.local=" + maven_home_local + "\" ";
            }

            this.CommandLine = this.CommandLine +
                                "\"-Dtools.jar=" + java_home + "\\lib\\tools.jar\" " +
                                "\"-Dforehead.conf.file=" + maven_forehead_conf + "\" " +
                                "-Djava.endorsed.dirs=\"" + maven_endorsed + "\" " +
                                maven_opts + " " +
                                "-classpath \"" + maven_classpath + "\" \"" + maven_main_class + "\" ";

            this.CommandLine += maven_cmd_line_args;

            m_mavenStatus = MavenStatus.None;

        }

    }
}
